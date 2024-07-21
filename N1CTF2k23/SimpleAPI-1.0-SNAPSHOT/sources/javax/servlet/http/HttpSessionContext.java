package javax.servlet.http;

import java.util.Enumeration;
@Deprecated
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/http/HttpSessionContext.class */
public interface HttpSessionContext {
    @Deprecated
    HttpSession getSession(String str);

    @Deprecated
    Enumeration<String> getIds();
}
