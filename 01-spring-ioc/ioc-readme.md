# spring-ioc 编写思路

## 说明
- 包结构 top.auzqy.spring 对应 spring框架的 org.springframework
其余的子路径要与 spring 框架建立对应的结构（目的是帮助理清及熟练其真实的包结构）
- 其中各类中的功能可以做对应的简化处理（第一次搞，要学会抓住主要矛盾）
- spring 框架的单元测试也要重视起来，也是一个学习的途径


## 编写步骤
1. 定义测试类 
    - IOCTest
    - 加入对应的配置文件
    - 定义相关的测试 bean
2. 创建对应的容器
    - top.auzqy.spring.context.AuApplicationContext
    - top.auzqy.spring.context.support.AuClassPathXmlApplicationContext
3. 设置配置文件位置
    - top.auzqy.spring.context.support.AuClassPathXmlApplicationContext.setConfigLocations
4. 创建 spring 容器
    - refresh()
    - 4.1 获取更新后的 bean factory
        - obtainFreshBeanFactory()
    - 4.2 初始化以及实例化所有剩余的非懒加载的单利对象
        - finishBeanFactoryInitialization()

### 详细内容
4.1 获取更新后的 bean factory `obtainFreshBeanFactory()`
- 创建 DefaultListableBeanFactory 对象，将加载完 bean definition 的 bean factory 赋值给 beanFactory 
- 通过 XmlBeanDefinitionReader 对象加载 bean Definitions。`loadBeanDefinitions(xmlBeanDefinitionReader);`
- 解析 xml 配置文件，并且注册 bean definition
```
1. 依据给定的 applicationContext.xml 的位置，加载这个配置文件
2. 使用 org.w3c.dom.Document 加载并解析这个配置文件
3. 使用 GenericBeanDefinition 对象封装解析后的结果，并封装于 Map<String,AuGenericBeanDefinition> beanDefinitionMap 的结构中。
```

4.2 初始化所有剩余的非懒加载的单利对象 `finishBeanFactoryInitialization(BeanFactory beanFactory)`
```
1. 实例化 bean 对象
2. 给 bean 对象赋值，并实现依赖注入 (todo 这里有个循环依赖的问题待解决)
```

4.3 给容器提供一个类似于 `getBean(String, Class<T>)` 的方法。