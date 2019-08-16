package top.auzqy.spring.context.support;

import com.sun.istack.internal.Nullable;
import top.auzqy.spring.beans.AuBeansException;
import top.auzqy.spring.beans.factory.AuBeanFactory;
import top.auzqy.spring.beans.factory.support.AuDefaultListableBeanFactory;
import top.auzqy.spring.context.AuApplicationContext;
import top.auzqy.spring.util.AuAssert;

import java.io.IOException;

/**
 * description:  spring 中依据 Class Path Xml 创建出来的一个容器
 *
 * createTime: 2019-08-15 19:54
 * @author au
 */
public class AuClassPathXmlApplicationContext implements AuApplicationContext {

    /**
     * xml配置文件所在位置的数组
     */
    private String[] configLocations;

    /**
     * Synchronization monitor for the "refresh" and "destroy".
     */
    private final Object startupShutdownMonitor = new Object();

    private AuBeanFactory auBeanFactory;

    /**
     * beanFactory 的锁对象
     */
    private final Object beanFactoryMonitor = new Object();

    public AuClassPathXmlApplicationContext(String  configLocation)
            throws AuBeansException {
        // 注意 refresh = true
        this(new String[] {configLocation}, true, null);
    }

    public AuClassPathXmlApplicationContext(String[] configLocations,
                                            boolean refresh,
                                            @Nullable AuApplicationContext parent)
            throws AuBeansException {
//        /*
//			其中
//			1。 设置好资源加载器（ResourcePatternResolver）
//			2。 设置好应用上下文（ApplicationContext），这个可以为 null
//		 */
//        super(parent);

        // 设置配置文件位置
        setConfigLocations(configLocations);

        if (refresh) {
            /*
             *	description:  这里翻译成中文是刷新，或者是更新，au觉得理解成更新更合适，
             * 				它的作用就是重新初始化/加载 Spring 容器。
             *
             * 				该方法是个很核心的方法
             */
            refresh();
        }
    }

    /**
     * Set the config locations for this application context.
     * <p>If not set, the implementation may use a default as appropriate.
     */
    private void setConfigLocations(@Nullable String... locations) {
        if (locations != null) {
            AuAssert.noNullElements(locations,
                    "Config locations must not be null");
            this.configLocations = new String[locations.length];
            for (int i = 0; i < locations.length; i++) {
				/*
					resolvePath(locations[i])
					依据相应的环境解析路径
				 */
                this.configLocations[i] = locations[i].trim();
            }
        }
        else {
            this.configLocations = null;
        }
    }

    /**
     * description:  创建 spring 容器的核心方法
     *
     *          该方法中 在 v5.1.8.RELEASE 版本中，有11个步骤，
     *          在此实现相对很核心的两个步骤
     *
     * createTime: 2019-08-16 12:45
     * @author au
     * @throws AuBeansException
     * @throws IllegalStateException
     */
    private void refresh() throws AuBeansException, IllegalStateException {

        // 线程同步 todo au对这里的同步，感觉理解的还很不全面
        synchronized (this.startupShutdownMonitor) {

            /**
             *      Tell the subclass to refresh the internal bean factory.
             *  2. 通知子类更新内部的 bean 工厂, 并返回该对象
             *
             * 	小结
             * 			1、ClassPathXmlApplicationContext
             * 			2、DefaultListableBeanFactory
             * 			3、GenericBeanDefinition
             */
            AuBeanFactory beanFactory = obtainFreshBeanFactory();

//            /**
//             *    Instantiate all remaining (non-lazy-init) singletons.
//             * todo 11. 初始化所有剩余的非懒加载的单利对象
//             * 			(非常重要)
//             */
//            //
//            finishBeanFactoryInitialization(beanFactory);
        }
    }

    /**
     * Tell the subclass to refresh the internal bean factory.
     * @return the fresh BeanFactory instance
     */
    private AuBeanFactory obtainFreshBeanFactory() {

        //判断是否在当前上下文拥有一个 bean factory，
        // 比如有至少一次刷新的和未关闭的bean factory
        if (auBeanFactory != null) {
            //销毁所有的在这个上下文管理的beans
            auBeanFactory = null;
        }

        try {
            AuBeanFactory beanFactory = new AuDefaultListableBeanFactory();;

            /**
             * todo 加载 beanFactory （这一步是比较关键的）
              */
            loadBeanDefinitions(beanFactory);

            synchronized (this.beanFactoryMonitor) {
                // 初始化当前类的成员变量
                this.auBeanFactory = beanFactory;
            }
            return beanFactory;
        }
        catch (IOException ex) {
            throw new AuBeansException(
                    "I/O error parsing bean definition source " +
                            "for loadBeanDefinitions", ex);
        }
    }

}
