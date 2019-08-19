package top.auzqy.spring;

import org.junit.jupiter.api.Test;
import top.auzqy.bean.MyBean01;
import top.auzqy.spring.context.AuApplicationContext;
import top.auzqy.spring.context.support.AuClassPathXmlApplicationContext;

/**
 * description:  ioc 容器的 test
 *          在练习手写 spring-ioc 框架时，同时训练 TDD
 *
 * createTime: 2019-08-15 19:40
 * @author au
 */
public class IOCTest {

    @Test
    void shouldLoadSuccessAndInitializationBeans() {
        AuApplicationContext applicationContext =
                new AuClassPathXmlApplicationContext(
                        "applicationContext_test.xml");

        MyBean01 myBean01 = applicationContext
                .getBean("myBean01", MyBean01.class);
    }
}
