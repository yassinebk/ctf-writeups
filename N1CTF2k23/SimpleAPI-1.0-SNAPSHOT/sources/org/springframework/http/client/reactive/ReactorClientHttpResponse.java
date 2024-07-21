package org.springframework.http.client.reactive;

import io.netty.buffer.ByteBufAllocator;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.netty.NettyInbound;
import reactor.netty.http.client.HttpClientResponse;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/client/reactive/ReactorClientHttpResponse.class */
class ReactorClientHttpResponse implements ClientHttpResponse {
    private final NettyDataBufferFactory bufferFactory;
    private final HttpClientResponse response;
    private final NettyInbound inbound;
    private final AtomicBoolean rejectSubscribers = new AtomicBoolean();

    public ReactorClientHttpResponse(HttpClientResponse response, NettyInbound inbound, ByteBufAllocator alloc) {
        this.response = response;
        this.inbound = inbound;
        this.bufferFactory = new NettyDataBufferFactory(alloc);
    }

    @Override // org.springframework.http.ReactiveHttpInputMessage
    public Flux<DataBuffer> getBody() {
        return this.inbound.receive().doOnSubscribe(s -> {
            if (this.rejectSubscribers.get()) {
                throw new IllegalStateException("The client response body can only be consumed once.");
            }
        }).doOnCancel(() -> {
            this.rejectSubscribers.set(true);
        }).map(byteBuf -> {
            byteBuf.retain();
            return this.bufferFactory.wrap(byteBuf);
        });
    }

    @Override // org.springframework.http.HttpMessage
    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        this.response.responseHeaders().entries().forEach(e -> {
            headers.add((String) e.getKey(), (String) e.getValue());
        });
        return headers;
    }

    @Override // org.springframework.http.client.reactive.ClientHttpResponse
    public HttpStatus getStatusCode() {
        return HttpStatus.valueOf(getRawStatusCode());
    }

    @Override // org.springframework.http.client.reactive.ClientHttpResponse
    public int getRawStatusCode() {
        return this.response.status().code();
    }

    @Override // org.springframework.http.client.reactive.ClientHttpResponse
    public MultiValueMap<String, ResponseCookie> getCookies() {
        MultiValueMap<String, ResponseCookie> result = new LinkedMultiValueMap<>();
        this.response.cookies().values().stream().flatMap((v0) -> {
            return v0.stream();
        }).forEach(c -> {
            result.add(c.name(), ResponseCookie.fromClientResponse(c.name(), c.value()).domain(c.domain()).path(c.path()).maxAge(c.maxAge()).secure(c.isSecure()).httpOnly(c.isHttpOnly()).build());
        });
        return CollectionUtils.unmodifiableMultiValueMap(result);
    }

    public String toString() {
        return "ReactorClientHttpResponse{request=[" + this.response.method().name() + " " + this.response.uri() + "],status=" + getRawStatusCode() + '}';
    }
}
