package javax.servlet;

import java.util.EventObject;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/ServletContextEvent.class */
public class ServletContextEvent extends EventObject {
    private static final long serialVersionUID = 1;

    public ServletContextEvent(ServletContext source) {
        super(source);
    }

    public ServletContext getServletContext() {
        return (ServletContext) super.getSource();
    }
}
