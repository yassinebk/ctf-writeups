package org.springframework.boot.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/servlet/ServletContextInitializer.class */
public interface ServletContextInitializer {
    void onStartup(ServletContext servletContext) throws ServletException;
}
