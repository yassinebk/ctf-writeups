package javax.servlet;

import java.io.IOException;
import java.util.EventListener;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/WriteListener.class */
public interface WriteListener extends EventListener {
    void onWritePossible() throws IOException;

    void onError(Throwable th);
}
