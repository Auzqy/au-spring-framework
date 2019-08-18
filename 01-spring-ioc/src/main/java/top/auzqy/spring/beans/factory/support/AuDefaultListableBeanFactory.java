package top.auzqy.spring.beans.factory.support;

import top.auzqy.spring.beans.factory.AuBeanFactory;

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
}
