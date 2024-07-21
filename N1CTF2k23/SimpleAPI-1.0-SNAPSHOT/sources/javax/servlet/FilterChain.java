package javax.servlet;

import java.io.IOException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/FilterChain.class */
public interface FilterChain {
    void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException;
}
