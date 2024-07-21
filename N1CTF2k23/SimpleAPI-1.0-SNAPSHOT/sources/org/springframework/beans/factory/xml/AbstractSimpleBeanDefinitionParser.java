package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.core.Conventions;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/xml/AbstractSimpleBeanDefinitionParser.class */
public abstract class AbstractSimpleBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser
    public void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        NamedNodeMap attributes = element.getAttributes();
        for (int x = 0; x < attributes.getLength(); x++) {
            Attr attribute = (Attr) attributes.item(x);
            if (isEligibleAttribute(attribute, parserContext)) {
                String propertyName = extractPropertyName(attribute.getLocalName());
                Assert.state(StringUtils.hasText(propertyName), "Illegal property name returned from 'extractPropertyName(String)': cannot be null or empty.");
                builder.addPropertyValue(propertyName, attribute.getValue());
            }
        }
        postProcess(builder, element);
    }

    protected boolean isEligibleAttribute(Attr attribute, ParserContext parserContext) {
        String fullName = attribute.getName();
        return (fullName.equals("xmlns") || fullName.startsWith("xmlns:") || !isEligibleAttribute(parserContext.getDelegate().getLocalName(attribute))) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isEligibleAttribute(String attributeName) {
        return !"id".equals(attributeName);
    }

    protected String extractPropertyName(String attributeName) {
        return Conventions.attributeNameToPropertyName(attributeName);
    }

    protected void postProcess(BeanDefinitionBuilder beanDefinition, Element element) {
    }
}
