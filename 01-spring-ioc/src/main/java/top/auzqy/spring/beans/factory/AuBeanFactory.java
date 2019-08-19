package top.auzqy.spring.beans.factory;

import top.auzqy.spring.beans.factory.support.AuGenericBeanDefinition;

/**
 * description:  bean factory 的顶级接口
 * createTime: 2019-08-16 12:54
 * @author au
 */
public interface AuBeanFactory {

    /**
     * description:  在 bean factory 中，注册 bean definition
     * createTime: 2019-08-18 17:17
     * @author au
     * @param beanName  bean 名称
     * @param beanDefinition    bean definition
     */
    void registerBeanDefinition(String beanName, AuGenericBeanDefinition beanDefinition);

    /**
     * description:  实例化所有单利的 bean 对象
     *
     *              注： 原 spring 框架中 用了 pre 这个前缀，
     *                  这里用了保留愿意所以也保留了，但具体的实现为了简化，
     *                  这里并没有考虑 pre。
     *
     * createTime: 2019-08-18 18:38
     * @author au
     * @param beanNameRef
     */
    void preInstantiateSingletons(String beanNameRef);

    /**
     * description:  依据 bean name 和 对应的类型，获取 bean 对象
     * createTime: 2019-08-19 22:55
     * @author au
     * @param beanName  拟获取的 bean's name
     * @param classType 拟获取的 bean 类型
     * @return
     */
    <T> T getBean(String beanName, Class<T> classType);
}
