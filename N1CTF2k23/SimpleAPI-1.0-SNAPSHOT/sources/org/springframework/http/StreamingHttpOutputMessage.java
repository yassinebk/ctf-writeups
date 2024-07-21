package org.springframework.http;

import java.io.IOException;
import java.io.OutputStream;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/StreamingHttpOutputMessage.class */
public interface StreamingHttpOutputMessage extends HttpOutputMessage {

    @FunctionalInterface
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/StreamingHttpOutputMessage$Body.class */
    public interface Body {
        void writeTo(OutputStream outputStream) throws IOException;
    }

    void setBody(Body body);
}
