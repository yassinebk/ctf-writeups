package org.springframework.web.servlet.config.annotation;

import java.util.Collections;
import javax.servlet.ServletContext;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/config/annotation/DefaultServletHandlerConfigurer.class */
public class DefaultServletHandlerConfigurer {
    private final ServletContext servletContext;
    @Nullable
    private DefaultServletHttpRequestHandler handler;

    public DefaultServletHandlerConfigurer(ServletContext servletContext) {
        Assert.notNull(servletContext, "ServletContext is required");
        this.servletContext = servletContext;
    }

    public void enable() {
        enable(null);
    }

    public void enable(@Nullable String defaultServletName) {
        this.handler = new DefaultServletHttpRequestHandler();
        if (defaultServletName != null) {
            this.handler.setDefaultServletName(defaultServletName);
        }
        this.handler.setServletContext(this.servletContext);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Nullable
    public SimpleUrlHandlerMapping buildHandlerMapping() {
        if (this.handler == null) {
            return null;
        }
        return new SimpleUrlHandlerMapping(Collections.singletonMap("/**", this.handler), Integer.MAX_VALUE);
    }
}
