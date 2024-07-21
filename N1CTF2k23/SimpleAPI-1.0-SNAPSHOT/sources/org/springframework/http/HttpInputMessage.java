package org.springframework.http;

import java.io.IOException;
import java.io.InputStream;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/HttpInputMessage.class */
public interface HttpInputMessage extends HttpMessage {
    InputStream getBody() throws IOException;
}
