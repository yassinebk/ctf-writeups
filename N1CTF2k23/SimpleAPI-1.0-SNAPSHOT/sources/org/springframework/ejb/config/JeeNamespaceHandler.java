package org.springframework.ejb.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/ejb/config/JeeNamespaceHandler.class */
public class JeeNamespaceHandler extends NamespaceHandlerSupport {
    @Override // org.springframework.beans.factory.xml.NamespaceHandler
    public void init() {
        registerBeanDefinitionParser("jndi-lookup", new JndiLookupBeanDefinitionParser());
        registerBeanDefinitionParser("local-slsb", new LocalStatelessSessionBeanDefinitionParser());
        registerBeanDefinitionParser("remote-slsb", new RemoteStatelessSessionBeanDefinitionParser());
    }
}
