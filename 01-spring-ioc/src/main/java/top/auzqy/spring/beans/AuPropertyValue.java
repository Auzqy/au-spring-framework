package top.auzqy.spring.beans;

import lombok.Data;

/**
 * description:  bean 中的属性 (bean 标签下的 <properties></properties>)
 * createTime: 2019-08-18 09:19
 * @author au
 */
@Data
public class AuPropertyValue {
    private String name;
    private String value;
    private String ref;
}
