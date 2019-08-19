package top.auzqy.spring.context;

/**
 * description:  spring 应用的上下文容器
 * createTime: 2019-08-15 19:52
 * @author au
 */
public interface AuApplicationContext {
    /**
     * description:  依据 bean name 和 bean 类型获得对应类型的 bean 对象
     * createTime: 2019-08-19 22:49
     * @author au
     * @param beanName  拟创建的 bean 名称
     * @param classType 拟创建的 bean 类型
     * @return
     */
    <T> T getBean(String beanName, Class<T> classType);
}
