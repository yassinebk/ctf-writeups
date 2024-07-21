package javax.servlet;

import java.util.Set;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/ServletContainerInitializer.class */
public interface ServletContainerInitializer {
    void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException;
}
