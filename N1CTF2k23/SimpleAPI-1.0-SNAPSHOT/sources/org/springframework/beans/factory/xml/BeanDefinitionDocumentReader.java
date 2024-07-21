package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.w3c.dom.Document;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/xml/BeanDefinitionDocumentReader.class */
public interface BeanDefinitionDocumentReader {
    void registerBeanDefinitions(Document document, XmlReaderContext xmlReaderContext) throws BeanDefinitionStoreException;
}
