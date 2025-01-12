package org.springframework.cache.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/cache/config/CacheNamespaceHandler.class */
public class CacheNamespaceHandler extends NamespaceHandlerSupport {
    static final String CACHE_MANAGER_ATTRIBUTE = "cache-manager";
    static final String DEFAULT_CACHE_MANAGER_BEAN_NAME = "cacheManager";

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String extractCacheManager(Element element) {
        return element.hasAttribute(CACHE_MANAGER_ATTRIBUTE) ? element.getAttribute(CACHE_MANAGER_ATTRIBUTE) : DEFAULT_CACHE_MANAGER_BEAN_NAME;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BeanDefinition parseKeyGenerator(Element element, BeanDefinition def) {
        String name = element.getAttribute("key-generator");
        if (StringUtils.hasText(name)) {
            def.getPropertyValues().add("keyGenerator", new RuntimeBeanReference(name.trim()));
        }
        return def;
    }

    @Override // org.springframework.beans.factory.xml.NamespaceHandler
    public void init() {
        registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenCacheBeanDefinitionParser());
        registerBeanDefinitionParser("advice", new CacheAdviceParser());
    }
}
