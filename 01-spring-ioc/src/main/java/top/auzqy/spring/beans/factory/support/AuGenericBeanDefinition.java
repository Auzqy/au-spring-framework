package top.auzqy.spring.beans.factory.support;

import lombok.Data;
import top.auzqy.spring.beans.AuPropertyValue;

import java.util.List;

/**
 * description:  Bean Definition 的一个包装类 (对 xml 的以一个封装)
 * createTime: 2019-08-17 23:39
 * @author au
 */
@Data
public class AuGenericBeanDefinition {
    private Class beanClass;
    private String beanName;
    private List<AuPropertyValue> propertyValueList;
}
