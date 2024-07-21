package javax.servlet;

import java.util.EventObject;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/ServletRequestEvent.class */
public class ServletRequestEvent extends EventObject {
    private static final long serialVersionUID = 1;
    private final transient ServletRequest request;

    public ServletRequestEvent(ServletContext sc, ServletRequest request) {
        super(sc);
        this.request = request;
    }

    public ServletRequest getServletRequest() {
        return this.request;
    }

    public ServletContext getServletContext() {
        return (ServletContext) super.getSource();
    }
}
