package org.springframework.http.server.reactive;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/server/reactive/HttpHeadResponseDecorator.class */
public class HttpHeadResponseDecorator extends ServerHttpResponseDecorator {
    public HttpHeadResponseDecorator(ServerHttpResponse delegate) {
        super(delegate);
    }

    @Override // org.springframework.http.server.reactive.ServerHttpResponseDecorator, org.springframework.http.ReactiveHttpOutputMessage
    public final Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        return Flux.from(body).reduce(0, current, buffer -> {
            int next = current.intValue() + buffer.readableByteCount();
            DataBufferUtils.release(buffer);
            return Integer.valueOf(next);
        }).doOnNext(length -> {
            if (length.intValue() > 0 || getHeaders().getFirst(HttpHeaders.CONTENT_LENGTH) == null) {
                getHeaders().setContentLength(length.intValue());
            }
        }).then();
    }

    @Override // org.springframework.http.server.reactive.ServerHttpResponseDecorator, org.springframework.http.ReactiveHttpOutputMessage
    public final Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return setComplete();
    }
}
