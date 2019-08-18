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
     */
    void preInstantiateSingletons();
}
