package org.springframework.web.context;

import javax.servlet.ServletContext;
import org.springframework.beans.factory.Aware;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/context/ServletContextAware.class */
public interface ServletContextAware extends Aware {
    void setServletContext(ServletContext servletContext);
}
