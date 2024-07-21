package org.springframework.http.server.reactive;

import ch.qos.logback.classic.spi.CallerData;
import io.undertow.connector.ByteBufferPool;
import io.undertow.connector.PooledByteBuffer;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import javax.net.ssl.SSLSession;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DataBufferWrapper;
import org.springframework.core.io.buffer.PooledDataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.xnio.channels.StreamSourceChannel;
import reactor.core.publisher.Flux;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/server/reactive/UndertowServerHttpRequest.class */
public class UndertowServerHttpRequest extends AbstractServerHttpRequest {
    private final HttpServerExchange exchange;
    private final RequestBodyPublisher body;

    public UndertowServerHttpRequest(HttpServerExchange exchange, DataBufferFactory bufferFactory) throws URISyntaxException {
        super(initUri(exchange), "", initHeaders(exchange));
        this.exchange = exchange;
        this.body = new RequestBodyPublisher(exchange, bufferFactory);
        this.body.registerListeners(exchange);
    }

    private static URI initUri(HttpServerExchange exchange) throws URISyntaxException {
        Assert.notNull(exchange, "HttpServerExchange is required");
        String requestURL = exchange.getRequestURL();
        String query = exchange.getQueryString();
        String requestUriAndQuery = StringUtils.hasLength(query) ? requestURL + CallerData.NA + query : requestURL;
        return new URI(requestUriAndQuery);
    }

    private static HttpHeaders initHeaders(HttpServerExchange exchange) {
        return new HttpHeaders(new UndertowHeadersAdapter(exchange.getRequestHeaders()));
    }

    @Override // org.springframework.http.HttpRequest
    public String getMethodValue() {
        return this.exchange.getRequestMethod().toString();
    }

    @Override // org.springframework.http.server.reactive.AbstractServerHttpRequest
    protected MultiValueMap<String, HttpCookie> initCookies() {
        MultiValueMap<String, HttpCookie> cookies = new LinkedMultiValueMap<>();
        for (String name : this.exchange.getRequestCookies().keySet()) {
            Cookie cookie = (Cookie) this.exchange.getRequestCookies().get(name);
            HttpCookie httpCookie = new HttpCookie(name, cookie.getValue());
            cookies.add(name, httpCookie);
        }
        return cookies;
    }

    @Override // org.springframework.http.server.reactive.ServerHttpRequest
    public InetSocketAddress getRemoteAddress() {
        return this.exchange.getSourceAddress();
    }

    @Override // org.springframework.http.server.reactive.ServerHttpRequest
    public InetSocketAddress getLocalAddress() {
        return this.exchange.getDestinationAddress();
    }

    @Override // org.springframework.http.server.reactive.AbstractServerHttpRequest
    @Nullable
    protected SslInfo initSslInfo() {
        SSLSession session = this.exchange.getConnection().getSslSession();
        if (session != null) {
            return new DefaultSslInfo(session);
        }
        return null;
    }

    @Override // org.springframework.http.ReactiveHttpInputMessage
    public Flux<DataBuffer> getBody() {
        return Flux.from(this.body);
    }

    @Override // org.springframework.http.server.reactive.AbstractServerHttpRequest
    public <T> T getNativeRequest() {
        return (T) this.exchange;
    }

    @Override // org.springframework.http.server.reactive.AbstractServerHttpRequest
    protected String initId() {
        return ObjectUtils.getIdentityHexString(this.exchange.getConnection());
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/server/reactive/UndertowServerHttpRequest$RequestBodyPublisher.class */
    private class RequestBodyPublisher extends AbstractListenerReadPublisher<DataBuffer> {
        private final StreamSourceChannel channel;
        private final DataBufferFactory bufferFactory;
        private final ByteBufferPool byteBufferPool;

        public RequestBodyPublisher(HttpServerExchange exchange, DataBufferFactory bufferFactory) {
            super(UndertowServerHttpRequest.this.getLogPrefix());
            this.channel = exchange.getRequestChannel();
            this.bufferFactory = bufferFactory;
            this.byteBufferPool = exchange.getConnection().getByteBufferPool();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void registerListeners(HttpServerExchange exchange) {
            exchange.addExchangeCompleteListener(ex, next -> {
                onAllDataRead();
                next.proceed();
            });
            this.channel.getReadSetter().set(c -> {
                onDataAvailable();
            });
            this.channel.getCloseSetter().set(c2 -> {
                onAllDataRead();
            });
            this.channel.resumeReads();
        }

        @Override // org.springframework.http.server.reactive.AbstractListenerReadPublisher
        protected void checkOnDataAvailable() {
            this.channel.resumeReads();
            onDataAvailable();
        }

        @Override // org.springframework.http.server.reactive.AbstractListenerReadPublisher
        protected void readingPaused() {
            this.channel.suspendReads();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.springframework.http.server.reactive.AbstractListenerReadPublisher
        @Nullable
        public DataBuffer read() throws IOException {
            PooledByteBuffer pooledByteBuffer = this.byteBufferPool.allocate();
            try {
                ByteBuffer byteBuffer = pooledByteBuffer.getBuffer();
                int read = this.channel.read(byteBuffer);
                if (rsReadLogger.isTraceEnabled()) {
                    rsReadLogger.trace(getLogPrefix() + "Read " + read + (read != -1 ? " bytes" : ""));
                }
                if (read > 0) {
                    byteBuffer.flip();
                    DataBuffer dataBuffer = this.bufferFactory.wrap(byteBuffer);
                    UndertowDataBuffer undertowDataBuffer = new UndertowDataBuffer(dataBuffer, pooledByteBuffer);
                    if (0 != 0 && pooledByteBuffer.isOpen()) {
                        pooledByteBuffer.close();
                    }
                    return undertowDataBuffer;
                }
                if (read == -1) {
                    onAllDataRead();
                }
                return null;
            } finally {
                if (1 != 0 && pooledByteBuffer.isOpen()) {
                    pooledByteBuffer.close();
                }
            }
        }

        @Override // org.springframework.http.server.reactive.AbstractListenerReadPublisher
        protected void discardData() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/server/reactive/UndertowServerHttpRequest$UndertowDataBuffer.class */
    public static class UndertowDataBuffer extends DataBufferWrapper implements PooledDataBuffer {
        private final PooledByteBuffer pooledByteBuffer;
        private final AtomicInteger refCount;

        public UndertowDataBuffer(DataBuffer dataBuffer, PooledByteBuffer pooledByteBuffer) {
            super(dataBuffer);
            this.pooledByteBuffer = pooledByteBuffer;
            this.refCount = new AtomicInteger(1);
        }

        private UndertowDataBuffer(DataBuffer dataBuffer, PooledByteBuffer pooledByteBuffer, AtomicInteger refCount) {
            super(dataBuffer);
            this.refCount = refCount;
            this.pooledByteBuffer = pooledByteBuffer;
        }

        @Override // org.springframework.core.io.buffer.PooledDataBuffer
        public boolean isAllocated() {
            return this.refCount.get() > 0;
        }

        @Override // org.springframework.core.io.buffer.PooledDataBuffer
        public PooledDataBuffer retain() {
            this.refCount.incrementAndGet();
            DataBufferUtils.retain(dataBuffer());
            return this;
        }

        @Override // org.springframework.core.io.buffer.PooledDataBuffer
        public boolean release() {
            int refCount = this.refCount.decrementAndGet();
            if (refCount == 0) {
                try {
                    return DataBufferUtils.release(dataBuffer());
                } finally {
                    this.pooledByteBuffer.close();
                }
            }
            return false;
        }

        @Override // org.springframework.core.io.buffer.DataBufferWrapper, org.springframework.core.io.buffer.DataBuffer
        public DataBuffer slice(int index, int length) {
            DataBuffer slice = dataBuffer().slice(index, length);
            return new UndertowDataBuffer(slice, this.pooledByteBuffer, this.refCount);
        }
    }
}
