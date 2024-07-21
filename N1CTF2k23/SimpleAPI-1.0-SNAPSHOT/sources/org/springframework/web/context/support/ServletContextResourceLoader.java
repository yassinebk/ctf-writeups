package org.springframework.web.context.support;

import javax.servlet.ServletContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/context/support/ServletContextResourceLoader.class */
public class ServletContextResourceLoader extends DefaultResourceLoader {
    private final ServletContext servletContext;

    public ServletContextResourceLoader(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override // org.springframework.core.io.DefaultResourceLoader
    protected Resource getResourceByPath(String path) {
        return new ServletContextResource(this.servletContext, path);
    }
}
