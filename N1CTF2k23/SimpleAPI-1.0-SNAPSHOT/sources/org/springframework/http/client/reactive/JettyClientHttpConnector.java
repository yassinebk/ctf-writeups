package org.springframework.http.client.reactive;

import java.net.URI;
import java.util.function.Consumer;
import java.util.function.Function;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.reactive.client.ContentChunk;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/client/reactive/JettyClientHttpConnector.class */
public class JettyClientHttpConnector implements ClientHttpConnector {
    private final HttpClient httpClient;
    private DataBufferFactory bufferFactory;

    public JettyClientHttpConnector() {
        this(new HttpClient());
    }

    public JettyClientHttpConnector(HttpClient httpClient) {
        this(httpClient, (JettyResourceFactory) null);
    }

    public JettyClientHttpConnector(HttpClient httpClient, @Nullable JettyResourceFactory resourceFactory) {
        this.bufferFactory = new DefaultDataBufferFactory();
        Assert.notNull(httpClient, "HttpClient is required");
        if (resourceFactory != null) {
            httpClient.setExecutor(resourceFactory.getExecutor());
            httpClient.setByteBufferPool(resourceFactory.getByteBufferPool());
            httpClient.setScheduler(resourceFactory.getScheduler());
        }
        this.httpClient = httpClient;
    }

    @Deprecated
    public JettyClientHttpConnector(JettyResourceFactory resourceFactory, @Nullable Consumer<HttpClient> customizer) {
        this(new HttpClient(), resourceFactory);
        if (customizer != null) {
            customizer.accept(this.httpClient);
        }
    }

    public void setBufferFactory(DataBufferFactory bufferFactory) {
        this.bufferFactory = bufferFactory;
    }

    @Override // org.springframework.http.client.reactive.ClientHttpConnector
    public Mono<ClientHttpResponse> connect(HttpMethod method, URI uri, Function<? super ClientHttpRequest, Mono<Void>> requestCallback) {
        if (!uri.isAbsolute()) {
            return Mono.error(new IllegalArgumentException("URI is not absolute: " + uri));
        }
        if (!this.httpClient.isStarted()) {
            try {
                this.httpClient.start();
            } catch (Exception ex) {
                return Mono.error(ex);
            }
        }
        JettyClientHttpRequest clientHttpRequest = new JettyClientHttpRequest(this.httpClient.newRequest(uri).method(method.toString()), this.bufferFactory);
        return requestCallback.apply(clientHttpRequest).then(Mono.from(clientHttpRequest.getReactiveRequest().response(response, chunks -> {
            Flux<DataBuffer> content = Flux.from(chunks).map(this::toDataBuffer);
            return Mono.just(new JettyClientHttpResponse(response, content));
        })));
    }

    private DataBuffer toDataBuffer(ContentChunk chunk) {
        DataBuffer buffer = this.bufferFactory.allocateBuffer(chunk.buffer.capacity());
        buffer.write(chunk.buffer);
        chunk.callback.succeeded();
        return buffer;
    }
}
