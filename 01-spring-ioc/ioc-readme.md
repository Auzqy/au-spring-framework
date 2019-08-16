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