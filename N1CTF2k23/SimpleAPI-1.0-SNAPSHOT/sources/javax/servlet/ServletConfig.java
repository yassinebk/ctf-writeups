package javax.servlet;

import java.util.Enumeration;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/ServletConfig.class */
public interface ServletConfig {
    String getServletName();

    ServletContext getServletContext();

    String getInitParameter(String str);

    Enumeration<String> getInitParameterNames();
}
