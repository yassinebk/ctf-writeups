package javax.servlet;

import java.util.Enumeration;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/FilterConfig.class */
public interface FilterConfig {
    String getFilterName();

    ServletContext getServletContext();

    String getInitParameter(String str);

    Enumeration<String> getInitParameterNames();
}
