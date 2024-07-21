package javax.servlet;

import java.io.IOException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/Servlet.class */
public interface Servlet {
    void init(ServletConfig servletConfig) throws ServletException;

    ServletConfig getServletConfig();

    void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException;

    String getServletInfo();

    void destroy();
}
