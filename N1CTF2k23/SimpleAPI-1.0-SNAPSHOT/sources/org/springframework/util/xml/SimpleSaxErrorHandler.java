package org.springframework.util.xml;

import org.apache.commons.logging.Log;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/xml/SimpleSaxErrorHandler.class */
public class SimpleSaxErrorHandler implements ErrorHandler {
    private final Log logger;

    public SimpleSaxErrorHandler(Log logger) {
        this.logger = logger;
    }

    @Override // org.xml.sax.ErrorHandler
    public void warning(SAXParseException ex) throws SAXException {
        this.logger.warn("Ignored XML validation warning", ex);
    }

    @Override // org.xml.sax.ErrorHandler
    public void error(SAXParseException ex) throws SAXException {
        throw ex;
    }

    @Override // org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException ex) throws SAXException {
        throw ex;
    }
}
