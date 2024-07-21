package org.springframework.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/WebApplicationInitializer.class */
public interface WebApplicationInitializer {
    void onStartup(ServletContext servletContext) throws ServletException;
}
