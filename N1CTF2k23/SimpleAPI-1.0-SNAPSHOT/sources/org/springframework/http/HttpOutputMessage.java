package org.springframework.http;

import java.io.IOException;
import java.io.OutputStream;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/HttpOutputMessage.class */
public interface HttpOutputMessage extends HttpMessage {
    OutputStream getBody() throws IOException;
}
