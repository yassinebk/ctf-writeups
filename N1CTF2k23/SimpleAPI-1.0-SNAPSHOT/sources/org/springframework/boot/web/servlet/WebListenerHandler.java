package org.springframework.boot.web.servlet;

import java.util.Map;
import javax.servlet.annotation.WebListener;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/servlet/WebListenerHandler.class */
class WebListenerHandler extends ServletComponentHandler {
    /* JADX INFO: Access modifiers changed from: package-private */
    public WebListenerHandler() {
        super(WebListener.class);
    }

    @Override // org.springframework.boot.web.servlet.ServletComponentHandler
    protected void doHandle(Map<String, Object> attributes, AnnotatedBeanDefinition beanDefinition, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ServletListenerRegistrationBean.class);
        builder.addPropertyValue("listener", beanDefinition);
        registry.registerBeanDefinition(beanDefinition.getBeanClassName(), builder.getBeanDefinition());
    }
}
