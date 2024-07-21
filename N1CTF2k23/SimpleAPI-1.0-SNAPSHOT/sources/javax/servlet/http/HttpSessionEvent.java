package javax.servlet.http;

import java.util.EventObject;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/http/HttpSessionEvent.class */
public class HttpSessionEvent extends EventObject {
    private static final long serialVersionUID = 1;

    public HttpSessionEvent(HttpSession source) {
        super(source);
    }

    public HttpSession getSession() {
        return (HttpSession) super.getSource();
    }
}
