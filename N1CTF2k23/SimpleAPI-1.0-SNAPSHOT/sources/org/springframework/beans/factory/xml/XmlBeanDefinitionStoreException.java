package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/xml/XmlBeanDefinitionStoreException.class */
public class XmlBeanDefinitionStoreException extends BeanDefinitionStoreException {
    public XmlBeanDefinitionStoreException(String resourceDescription, String msg, SAXException cause) {
        super(resourceDescription, msg, cause);
    }

    public int getLineNumber() {
        Throwable cause = getCause();
        if (cause instanceof SAXParseException) {
            return ((SAXParseException) cause).getLineNumber();
        }
        return -1;
    }
}
