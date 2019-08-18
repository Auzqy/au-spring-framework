package top.auzqy.spring.beans.factory.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;
import top.auzqy.spring.beans.AuPropertyValue;
import top.auzqy.spring.beans.factory.AuBeanDefinitionStoreException;
import top.auzqy.spring.beans.factory.AuBeanFactory;
import top.auzqy.spring.beans.factory.support.AuGenericBeanDefinition;
import top.auzqy.spring.util.AuAssert;
import top.auzqy.spring.util.xml.AuSimpleSaxErrorHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * description:  xml 格式 bean definition 的一个 读取器
 * createTime: 2019-08-17 12:13
 * @author au
 */
public class AuXmlBeanDefinitionReader {

    /**
     * JAXP attribute used to configure the schema language for validation.
     */
    private static final String SCHEMA_LANGUAGE_ATTRIBUTE =
            "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    /**
     * JAXP attribute value indicating the XSD schema language.
     */
    private static final String XSD_SCHEMA_LANGUAGE =
            "http://www.w3.org/2001/XMLSchema";

    private static final String BEAN_ELEMENT = "bean";
    private static final String ID_ATTRIBUTE = "id";
    private static final String CLASS_ATTRIBUTE = "class";
    private static final String PROPERTY_ELEMENT = "property";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String VALUE_ELEMENT = "value";
    private static final String REF_ELEMENT = "ref";

    private AuBeanFactory auBeanFactory;


    public AuXmlBeanDefinitionReader(AuBeanFactory beanFactory) {
        this.auBeanFactory = beanFactory;
    }

    public void loadBeanDefinitions(String... locations) {
        AuAssert.notNull(locations, "Location array must not be null");
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }

    /**
     * Load bean definitions from the specified XML file.
     */
    public void loadBeanDefinitions(String configLocations) {
        AuAssert.notNull(configLocations,
                "configLocations must not be null");

        try {

            /*
             * 读取xml配置文件，得到一个输入流
             * ClassLoader.getSystemClassLoader().getResourceAsStream(configLocations);
             */
            InputStream inputStream = ClassLoader
                    .getSystemResourceAsStream(configLocations);
            try {
                // 输入流转成 JDK 的 InputSource 类型
                InputSource inputSource = new InputSource(inputStream);
                // 加载 bean definitions（doXXX 中 do 开头的往往是具体执行的）
                doLoadBeanDefinitions(inputSource);
            }
            finally {
                assert inputStream != null;
                inputStream.close();
            }
        }
        catch (IOException ex) {
            throw new AuBeanDefinitionStoreException(
                    "IOException parsing XML document from "
                            + configLocations, ex);
        }
    }

    /**
     * description:   从指定的 xml file 中加载 bean definition
     *      Actually load bean definitions from the specified XML file.
     * createTime: 2019-08-17 12:41
     * @author au
     * @param inputSource the SAX InputSource to read from
     * @throws AuBeanDefinitionStoreException
     */
    private void doLoadBeanDefinitions(InputSource inputSource)
            throws AuBeanDefinitionStoreException {

        try {
            // 从 xml 配置文件加载成为一个 Document 对象
            Document doc = doLoadDocument(inputSource);

            // 注册 bean Definitions
            registerBeanDefinitions(doc);
        }
        catch (AuBeanDefinitionStoreException ex) {
            throw ex;
        }
        catch (SAXParseException ex) {
            throw new AuBeanDefinitionStoreException(
                    "Line " + ex.getLineNumber() + " in XML document from  xml config  is invalid", ex);
        }
        catch (SAXException ex) {
            throw new AuBeanDefinitionStoreException(
                    "XML document from  xml config  is invalid", ex);
        }
        catch (ParserConfigurationException ex) {
            throw new AuBeanDefinitionStoreException(
                    "Parser configuration exception parsing XML from xml config", ex);
        }
        catch (IOException ex) {
            throw new AuBeanDefinitionStoreException(
                    "IOException parsing XML document from xml config", ex);
        }
        catch (Throwable ex) {
            throw new AuBeanDefinitionStoreException(
                    "Unexpected exception parsing XML document from xml config", ex);
        }
    }

    private Document doLoadDocument(InputSource inputSource) throws Exception {

        //JDK自带的dom解析工厂，创建一个DocumentBuilderFactory工厂
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        // Enforce namespace aware for XSD...
        factory.setNamespaceAware(true);
        factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);

        //创建一个JDK自带的xml解析的DocumentBuilder对象
        DocumentBuilder docBuilder = factory.newDocumentBuilder();

        //设置离线的xsd验证解析
        docBuilder.setEntityResolver(new AuPluggableSchemaResolver());

        //设置错误处理
        docBuilder.setErrorHandler(new AuSimpleSaxErrorHandler());

        //采用JDK自带的dom解析对象DocumentBuilder解析读取的xml文件输入流，得到代表xml的文档对象Document
        return docBuilder.parse(inputSource);
    }

    /**
     * Register the bean definitions contained in the given DOM document.
     * Called by {@code loadBeanDefinitions}.
     * <p>Creates a new instance of the parser class and invokes
     * {@code registerBeanDefinitions} on it.
     */
    private void registerBeanDefinitions(Document doc)
            throws AuBeanDefinitionStoreException, ClassNotFoundException {
        Element root = doc.getDocumentElement();
        NodeList nl = root.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                //获取到一个元素节点
                Element ele = (Element) node;
                //解析默认的元素节点
                parseDefaultElement(ele);
            }
        }
    }

    private void parseDefaultElement(Element ele) throws ClassNotFoundException {
        if (BEAN_ELEMENT.equalsIgnoreCase(ele.getNodeName())) {
            // 解析 bean definition 中的元素，底层是把 <bean> 的配置定义
            // 封装到一个 GenericBeanDefinition 对象中
            parseBeanDefinitionElement(ele);
        }
    }

    /**
     * Parses the supplied {@code <bean>} element. May return {@code null}
     * if there were errors during parse. Errors are reported to the
     */
    private void parseBeanDefinitionElement(Element ele)
            throws ClassNotFoundException {
        String id = ele.getAttribute(ID_ATTRIBUTE);
        String classAttr = ele.getAttribute(CLASS_ATTRIBUTE);

        // 底层创建了一个 GenericBeanDefinition
        AuGenericBeanDefinition myGenericBeanDefinition =
                new AuGenericBeanDefinition();
        // 设置bean定义对象的beanClass类
        myGenericBeanDefinition.setBeanClass(Class.forName(classAttr));
        // 设置beanName
        myGenericBeanDefinition.setBeanName(id);
        // 设置多个属性
        myGenericBeanDefinition.setPropertyValueList(parseAuPropertyValues(ele));

        // 注册包装后的 GenericBeanDefinition 对象，将其注册到 bean factory
        this.auBeanFactory.registerBeanDefinition(id, myGenericBeanDefinition);
    }

    private List<AuPropertyValue> parseAuPropertyValues(Element ele) {
        List<AuPropertyValue> auPropertyValueList =
                new ArrayList<>();

        NodeList nl = ele.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (PROPERTY_ELEMENT.equals(node.getNodeName())) {
                String propertyName = ele.getAttribute(NAME_ATTRIBUTE);
                String propertyValue = ele.getAttribute(VALUE_ELEMENT);
                String propertyRef = ele.getAttribute(REF_ELEMENT);

                AuPropertyValue auPropertyValue = new AuPropertyValue();
                auPropertyValue.setName(propertyName);
                auPropertyValue.setValue(propertyValue);
                auPropertyValue.setRef(propertyRef);

                auPropertyValueList.add(auPropertyValue);
            }
        }
        return auPropertyValueList;
    }
}
