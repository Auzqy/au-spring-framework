package top.auzqy.spring.beans.factory.support;

import top.auzqy.spring.beans.AuBeansException;
import top.auzqy.spring.beans.factory.AuBeanFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description:  默认的可列举的 bean factory
 * createTime: 2019-08-16 13:11
 * @author au
 */
public class AuDefaultListableBeanFactory implements AuBeanFactory {

    private Map<String,AuGenericBeanDefinition> beanDefinitionMap
            = new ConcurrentHashMap<>(256);

    @Override
    public void registerBeanDefinition(String beanName, AuGenericBeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    /**
     * description:  初始化所有单利的 bean 对象
     *          1。 初始化 bean 对象
     *          2。 给 bean 对象赋值，并实现依赖注入
     *
     * createTime: 2019-08-18 18:42
     * @author au
     */
    @Override
    public void preInstantiateSingletons() {
        try {

            for (Map.Entry<String, AuGenericBeanDefinition> stringAuGenericBeanDefinitionEntry
                    : this.beanDefinitionMap.entrySet()) {
                Class beanClassName = stringAuGenericBeanDefinitionEntry.getValue().getBeanClass();
                // 1。 实例化 bean 对象
                Object beanObject = beanClassName.getDeclaredConstructor().newInstance();

                // 2。 给 bean 对象赋值，并实现依赖注入

            }
        } catch (InstantiationException
                | NoSuchMethodException
                | InvocationTargetException
                | IllegalAccessException e) {
            throw new AuBeansException(e);
        }


    }
}
