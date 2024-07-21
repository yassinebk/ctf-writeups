package org.springframework.http;

import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/ReactiveHttpInputMessage.class */
public interface ReactiveHttpInputMessage extends HttpMessage {
    Flux<DataBuffer> getBody();
}
