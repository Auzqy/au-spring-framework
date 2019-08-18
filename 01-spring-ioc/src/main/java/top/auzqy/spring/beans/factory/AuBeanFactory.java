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
}
