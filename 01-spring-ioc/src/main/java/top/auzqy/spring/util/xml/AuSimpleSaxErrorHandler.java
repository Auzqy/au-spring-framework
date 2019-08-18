package top.auzqy.spring.util.xml;

import lombok.extern.slf4j.Slf4j;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * description:  自定义的 SAX 错误处理
 * createTime: 2019-08-17 16:58
 * @author au
 */
@Slf4j
public class AuSimpleSaxErrorHandler implements ErrorHandler {
    @Override
    public void warning(SAXParseException exception) throws SAXException {
        log.warn("Ignored XML validation warning: {0}", exception);
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }
}
