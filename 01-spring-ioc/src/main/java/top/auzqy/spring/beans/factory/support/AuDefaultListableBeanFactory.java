package top.auzqy.spring.beans.factory.support;

import lombok.extern.slf4j.Slf4j;
import top.auzqy.spring.beans.AuBeansException;
import top.auzqy.spring.beans.AuPropertyValue;
import top.auzqy.spring.beans.factory.AuBeanFactory;
import top.auzqy.spring.util.StringUtils;
import top.auzqy.spring.util.TypeCastUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description:  默认的可列举的 bean factory
 * createTime: 2019-08-16 13:11
 *
 * @author au
 */
@Slf4j
public class AuDefaultListableBeanFactory implements AuBeanFactory {

    private Map<String, AuGenericBeanDefinition> beanDefinitionMap
            = new ConcurrentHashMap<>(256);

    /**
     * description:  存放已经创建好的 bean 对象的 map
     * createTime: 2019-08-19 14:03
     * @author au
     */
    private Map<String, Object> beanObjectsMap
            = new ConcurrentHashMap<>(256);

    @Override
    public void registerBeanDefinition(String beanName, AuGenericBeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    /**
     * description:  初始化所有单利的 bean 对象
     * 1。 初始化 bean 对象
     * 2。 给 bean 对象赋值，并实现依赖注入
     *  todo 这里 bean 的循环引用的问题没有解决，再看看 Spring 是如何处理的
     *
     * createTime: 2019-08-18 18:42
     *
     * @author au
     */
    @Override
    public void preInstantiateSingletons(String beanNameRef) {
        if (StringUtils.isEmpty(beanNameRef)) {
            for (Map.Entry<String, AuGenericBeanDefinition> stringAuGenericBeanDefinitionEntry
                    : this.beanDefinitionMap.entrySet()) {
                // 即将要创建的 bean's name
                String beanName = stringAuGenericBeanDefinitionEntry.getValue()
                        .getBeanName();
                // 判断当前将要创建的对象是否已经被创建好了，如果已经被创建出来，那就 continue ，继续下一个
                if (this.beanObjectsMap.containsKey(beanName)) {
                    continue;
                }
                instantiateSingletons(stringAuGenericBeanDefinitionEntry.getValue());
            }
        } else {
            instantiateSingletons(this.beanDefinitionMap.get(beanNameRef));
        }
    }

    /**
     * description:  实例化 bean 及 依赖注入
     * createTime: 2019-08-19 22:45
     * @author au
     * @param auGenericBeanDefinition   spring 中的 GenericBeanDefinition 对象
     */
    private void instantiateSingletons(AuGenericBeanDefinition auGenericBeanDefinition) {
        try {
            // 即将要创建的 bean's name
            String beanName = auGenericBeanDefinition
                    .getBeanName();
//            // 判断当前将要创建的对象是否已经被创建好了，如果已经被创建出来，那就 continue ，继续下一个
//            if (this.beanObjectsMap.containsKey(beanName)) {
//                continue;
//            }

            Class beanClassName = auGenericBeanDefinition
                    .getBeanClass();
            // 1。 实例化 bean 对象
            Object beanObject = beanClassName.getDeclaredConstructor().newInstance();

            // 2。 给 bean 对象赋值，并实现依赖注入 todo 再仔细看一下 spring 是怎么搞的
            // 2.1 获取对象的属性值
            for (AuPropertyValue beanPropertyValue
                    : auGenericBeanDefinition
                    .getPropertyValueList()) {

                String propertyName = beanPropertyValue.getName();
                String propertyValue = beanPropertyValue.getValue();
                String propertyRef = beanPropertyValue.getRef();

                        /*
                            由于在 applicationContext.xml 中定义的 <bean></bean> 标签
                            中，一个属性的 value 和 ref 有且只能有一个，故此处做此处理
                         */
                if (StringUtils.isEmpty(propertyValue)
                        && StringUtils.isEmpty(propertyRef)) {
                    throw new AuBeansException("property 标签中的 value 和 ref 不能同时为空！");
                } else if (!StringUtils.isEmpty(propertyValue)
                        && !StringUtils.isEmpty(propertyRef)) {
                    throw new AuBeansException("property 标签中的 value 和 ref 不能同时有值！");
                }

                Field[] fields = beanClassName.getDeclaredFields();
                /**
                 * description:
                 *      这里除了通过 PropertyDescriptor 来获取 set 方法，
                 *      注意 这个方法的底层其实也是用字符串拼接的方式获取
                 *      的 set 方法，但是人家（jdk）写的别叫好，对 boolean 类型
                 *      的 is 也做了对应的处理。
                 *
                 * createTime: 2019-08-19 23:39
                 * @author au
                 */
                // 找到属性的写数据的方法
                for (Field f : fields) {
                    if (propertyName.equals(f.getName())) {
                        PropertyDescriptor pd = new PropertyDescriptor(
                                f.getName(), beanClassName);
                        // 获得写方法
                        Method writeMethod = pd.getWriteMethod();
                        if (!StringUtils.isEmpty(propertyValue)) {
                                    /*
                                        todo 这里看一下 spring 是怎么做的
                                        因为知道是int类型的属性，所以传个int过去就是了。。
                                        实际情况中需要判断下他的参数类型
                                     */
                            writeMethod.invoke(beanObject,
                                    TypeCastUtils.convertParamType(
                                            propertyValue,f.getType()));
                            // 如果已经匹配上，那么就不需要再循环该属性了，直接去找下一个属性
                            break;
                        } else if (!StringUtils.isEmpty(propertyRef)) {
                            // 查找一下 依赖的对象是否已经存在于 beanObjectsMap （即已经创建完成）
                            Object propertyRefObject = this.beanObjectsMap.get(propertyRef);

                            if (null == propertyRefObject) {
                                preInstantiateSingletons(propertyRef);
                                propertyRefObject = this.beanObjectsMap.get(propertyRef);
                            }
                                    /*
                                        todo 这里看一下 spring 是怎么做的
                                        因为知道是int类型的属性，所以传个int过去就是了。。
                                        实际情况中需要判断下他的参数类型
                                     */
                            writeMethod.invoke(beanObject, propertyRefObject);
                            // 如果已经匹配上，那么就不需要再循环该属性了，直接去找下一个属性
                            break;
                        }

                    }
                }
            }

            // 将创建出来的对象放如 beanObjectsMap 中
            this.beanObjectsMap.put(beanName, beanObject);
        } catch (InstantiationException
                | NoSuchMethodException
                | InvocationTargetException
                | IllegalAccessException
                | IntrospectionException e) {
            throw new AuBeansException(e);
        }
    }

    /**
     * description:  依据 bean name 和 对应的类型，获取 bean 对象
     * createTime: 2019-08-19 22:55
     *
     * @param beanName  拟获取的 bean's name
     * @param classType 拟获取的 bean 类型
     * @return
     * @author au
     */
    @Override
    public <T> T getBean(String beanName, Class<T> classType) {
        Object beanObject = this.beanObjectsMap.get(beanName);

        if (beanObject.getClass().isAssignableFrom(classType)) {
            return (T)beanObject;
        } else {
            throw new AuBeansException("bean class type cast exception.");
        }
    }
}
