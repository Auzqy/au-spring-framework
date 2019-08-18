package top.auzqy.spring.beans.factory.xml;

import lombok.extern.slf4j.Slf4j;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description:  插件化的 schema 解析器
 * createTime: 2019-08-17 16:13
 * @author au
 */
@Slf4j
public class AuPluggableSchemaResolver implements EntityResolver {

    /**
     * The location of the file that defines schema mappings.
     * Can be present in multiple JAR files.
     */
    private static final String DEFAULT_SCHEMA_MAPPINGS_LOCATION
            = "META-INF/spring.schemas";

    @Override
    public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException {

        if (systemId != null) {
            //systemId --> http://www.springframework.org/schema/beans/spring-beans.xsd
            //resourceLocation --> top/auzqy/beans/factory/xml/spring-beans.xsd

            String resourceLocation = getSchemaMappings().getProperty(systemId);
            if (resourceLocation == null && systemId.startsWith("https:")) {
                // Retrieve canonical http schema mapping even for https declaration
                resourceLocation = getSchemaMappings().getProperty("http:" + systemId.substring(6));
            }
            if (resourceLocation != null) {
                InputStream systemResourceAsStream = ClassLoader
                        .getSystemResourceAsStream(resourceLocation);
                InputSource source = new InputSource(systemResourceAsStream);
                source.setPublicId(publicId);
                source.setSystemId(systemId);
                return source;
            }
        }

        // Fall back to the parser's default behavior.
        return null;
    }

    /**
     * Load the specified schema mappings lazily.
     */
    private Properties getSchemaMappings() {
        synchronized (this) {
            try {
                Properties properties = new Properties();
                InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream(DEFAULT_SCHEMA_MAPPINGS_LOCATION);
                properties.load(systemResourceAsStream);

                return properties;
            }
            catch (IOException ex) {
                throw new IllegalStateException(
                        "Unable to load schema mappings from location ["
                                + DEFAULT_SCHEMA_MAPPINGS_LOCATION + "]", ex);
            }
        }
    }
}
