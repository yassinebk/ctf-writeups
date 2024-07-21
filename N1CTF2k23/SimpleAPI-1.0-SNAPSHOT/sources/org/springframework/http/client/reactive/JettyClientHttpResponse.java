package org.springframework.http.client.reactive;

import java.net.HttpCookie;
import java.util.List;
import org.eclipse.jetty.reactive.client.ReactiveResponse;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/client/reactive/JettyClientHttpResponse.class */
class JettyClientHttpResponse implements ClientHttpResponse {
    private final ReactiveResponse reactiveResponse;
    private final Flux<DataBuffer> content;

    public JettyClientHttpResponse(ReactiveResponse reactiveResponse, Publisher<DataBuffer> content) {
        this.reactiveResponse = reactiveResponse;
        this.content = Flux.from(content);
    }

    @Override // org.springframework.http.client.reactive.ClientHttpResponse
    public HttpStatus getStatusCode() {
        return HttpStatus.valueOf(getRawStatusCode());
    }

    @Override // org.springframework.http.client.reactive.ClientHttpResponse
    public int getRawStatusCode() {
        return this.reactiveResponse.getStatus();
    }

    @Override // org.springframework.http.client.reactive.ClientHttpResponse
    public MultiValueMap<String, ResponseCookie> getCookies() {
        MultiValueMap<String, ResponseCookie> result = new LinkedMultiValueMap<>();
        List<String> cookieHeader = getHeaders().get(HttpHeaders.SET_COOKIE);
        if (cookieHeader != null) {
            cookieHeader.forEach(header -> {
                HttpCookie.parse(header).forEach(c -> {
                    result.add(c.getName(), ResponseCookie.fromClientResponse(c.getName(), c.getValue()).domain(c.getDomain()).path(c.getPath()).maxAge(c.getMaxAge()).secure(c.getSecure()).httpOnly(c.isHttpOnly()).build());
                });
            });
        }
        return CollectionUtils.unmodifiableMultiValueMap(result);
    }

    @Override // org.springframework.http.ReactiveHttpInputMessage
    public Flux<DataBuffer> getBody() {
        return this.content;
    }

    @Override // org.springframework.http.HttpMessage
    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        this.reactiveResponse.getHeaders().stream().forEach(field -> {
            headers.add(field.getName(), field.getValue());
        });
        return headers;
    }
}
