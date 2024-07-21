package org.springframework.web.servlet.function;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.servlet.function.DefaultServerRequest;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/DefaultServerRequestBuilder.class */
class DefaultServerRequestBuilder implements ServerRequest.Builder {
    private final List<HttpMessageConverter<?>> messageConverters;
    private HttpServletRequest servletRequest;
    private String methodName;
    private URI uri;
    private final HttpHeaders headers = new HttpHeaders();
    private final MultiValueMap<String, Cookie> cookies = new LinkedMultiValueMap();
    private final Map<String, Object> attributes = new LinkedHashMap();
    private byte[] body = new byte[0];

    public DefaultServerRequestBuilder(ServerRequest other) {
        Assert.notNull(other, "ServerRequest must not be null");
        this.messageConverters = other.messageConverters();
        this.servletRequest = other.servletRequest();
        this.methodName = other.methodName();
        this.uri = other.uri();
        headers(headers -> {
            headers.addAll(other.headers().asHttpHeaders());
        });
        cookies(cookies -> {
            cookies.addAll(other.cookies());
        });
        attributes(attributes -> {
            attributes.putAll(other.attributes());
        });
    }

    @Override // org.springframework.web.servlet.function.ServerRequest.Builder
    public ServerRequest.Builder method(HttpMethod method) {
        Assert.notNull(method, "HttpMethod must not be null");
        this.methodName = method.name();
        return this;
    }

    @Override // org.springframework.web.servlet.function.ServerRequest.Builder
    public ServerRequest.Builder uri(URI uri) {
        Assert.notNull(uri, "URI must not be null");
        this.uri = uri;
        return this;
    }

    @Override // org.springframework.web.servlet.function.ServerRequest.Builder
    public ServerRequest.Builder header(String headerName, String... headerValues) {
        for (String headerValue : headerValues) {
            this.headers.add(headerName, headerValue);
        }
        return this;
    }

    @Override // org.springframework.web.servlet.function.ServerRequest.Builder
    public ServerRequest.Builder headers(Consumer<HttpHeaders> headersConsumer) {
        headersConsumer.accept(this.headers);
        return this;
    }

    @Override // org.springframework.web.servlet.function.ServerRequest.Builder
    public ServerRequest.Builder cookie(String name, String... values) {
        for (String value : values) {
            this.cookies.add(name, new Cookie(name, value));
        }
        return this;
    }

    @Override // org.springframework.web.servlet.function.ServerRequest.Builder
    public ServerRequest.Builder cookies(Consumer<MultiValueMap<String, Cookie>> cookiesConsumer) {
        cookiesConsumer.accept(this.cookies);
        return this;
    }

    @Override // org.springframework.web.servlet.function.ServerRequest.Builder
    public ServerRequest.Builder body(byte[] body) {
        this.body = body;
        return this;
    }

    @Override // org.springframework.web.servlet.function.ServerRequest.Builder
    public ServerRequest.Builder body(String body) {
        return body(body.getBytes(StandardCharsets.UTF_8));
    }

    @Override // org.springframework.web.servlet.function.ServerRequest.Builder
    public ServerRequest.Builder attribute(String name, Object value) {
        Assert.notNull(name, "'name' must not be null");
        this.attributes.put(name, value);
        return this;
    }

    @Override // org.springframework.web.servlet.function.ServerRequest.Builder
    public ServerRequest.Builder attributes(Consumer<Map<String, Object>> attributesConsumer) {
        attributesConsumer.accept(this.attributes);
        return this;
    }

    @Override // org.springframework.web.servlet.function.ServerRequest.Builder
    public ServerRequest build() {
        return new BuiltServerRequest(this.servletRequest, this.methodName, this.uri, this.headers, this.cookies, this.attributes, this.body, this.messageConverters);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/DefaultServerRequestBuilder$BuiltServerRequest.class */
    public static class BuiltServerRequest implements ServerRequest {
        private final String methodName;
        private final URI uri;
        private final HttpHeaders headers;
        private final HttpServletRequest servletRequest;
        private MultiValueMap<String, Cookie> cookies;
        private final Map<String, Object> attributes;
        private final byte[] body;
        private final List<HttpMessageConverter<?>> messageConverters;

        public BuiltServerRequest(HttpServletRequest servletRequest, String methodName, URI uri, HttpHeaders headers, MultiValueMap<String, Cookie> cookies, Map<String, Object> attributes, byte[] body, List<HttpMessageConverter<?>> messageConverters) {
            this.servletRequest = servletRequest;
            this.methodName = methodName;
            this.uri = uri;
            this.headers = headers;
            this.cookies = cookies;
            this.attributes = attributes;
            this.body = body;
            this.messageConverters = messageConverters;
        }

        @Override // org.springframework.web.servlet.function.ServerRequest
        public String methodName() {
            return this.methodName;
        }

        @Override // org.springframework.web.servlet.function.ServerRequest
        public URI uri() {
            return this.uri;
        }

        @Override // org.springframework.web.servlet.function.ServerRequest
        public UriBuilder uriBuilder() {
            return UriComponentsBuilder.fromUri(this.uri);
        }

        @Override // org.springframework.web.servlet.function.ServerRequest
        public ServerRequest.Headers headers() {
            return new DefaultServerRequest.DefaultRequestHeaders(this.headers);
        }

        @Override // org.springframework.web.servlet.function.ServerRequest
        public MultiValueMap<String, Cookie> cookies() {
            return this.cookies;
        }

        @Override // org.springframework.web.servlet.function.ServerRequest
        public Optional<InetSocketAddress> remoteAddress() {
            return Optional.empty();
        }

        @Override // org.springframework.web.servlet.function.ServerRequest
        public List<HttpMessageConverter<?>> messageConverters() {
            return this.messageConverters;
        }

        @Override // org.springframework.web.servlet.function.ServerRequest
        public <T> T body(Class<T> bodyType) throws IOException, ServletException {
            return (T) bodyInternal(bodyType, bodyType);
        }

        @Override // org.springframework.web.servlet.function.ServerRequest
        public <T> T body(ParameterizedTypeReference<T> bodyType) throws IOException, ServletException {
            Type type = bodyType.getType();
            return (T) bodyInternal(type, DefaultServerRequest.bodyClass(type));
        }

        private <T> T bodyInternal(Type bodyType, Class<?> bodyClass) throws ServletException, IOException {
            HttpInputMessage inputMessage = new BuiltInputMessage();
            MediaType contentType = headers().contentType().orElse(MediaType.APPLICATION_OCTET_STREAM);
            for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
                if (messageConverter instanceof GenericHttpMessageConverter) {
                    GenericHttpMessageConverter<T> genericMessageConverter = (GenericHttpMessageConverter) messageConverter;
                    if (genericMessageConverter.canRead(bodyType, bodyClass, contentType)) {
                        return genericMessageConverter.read(bodyType, bodyClass, inputMessage);
                    }
                }
                if (messageConverter.canRead(bodyClass, contentType)) {
                    return (T) messageConverter.read(bodyClass, inputMessage);
                }
            }
            throw new HttpMediaTypeNotSupportedException(contentType, Collections.emptyList());
        }

        @Override // org.springframework.web.servlet.function.ServerRequest
        public Map<String, Object> attributes() {
            return this.attributes;
        }

        @Override // org.springframework.web.servlet.function.ServerRequest
        public MultiValueMap<String, String> params() {
            return new LinkedMultiValueMap();
        }

        @Override // org.springframework.web.servlet.function.ServerRequest
        public Map<String, String> pathVariables() {
            Map<String, String> pathVariables = (Map) attributes().get(RouterFunctions.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            if (pathVariables != null) {
                return pathVariables;
            }
            return Collections.emptyMap();
        }

        @Override // org.springframework.web.servlet.function.ServerRequest
        public HttpSession session() {
            return this.servletRequest.getSession();
        }

        @Override // org.springframework.web.servlet.function.ServerRequest
        public Optional<Principal> principal() {
            return Optional.ofNullable(this.servletRequest.getUserPrincipal());
        }

        @Override // org.springframework.web.servlet.function.ServerRequest
        public HttpServletRequest servletRequest() {
            return this.servletRequest;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/DefaultServerRequestBuilder$BuiltServerRequest$BuiltInputMessage.class */
        public class BuiltInputMessage implements HttpInputMessage {
            private BuiltInputMessage() {
            }

            @Override // org.springframework.http.HttpInputMessage
            public InputStream getBody() throws IOException {
                return new BodyInputStream(BuiltServerRequest.this.body);
            }

            @Override // org.springframework.http.HttpMessage
            public HttpHeaders getHeaders() {
                return BuiltServerRequest.this.headers;
            }
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/DefaultServerRequestBuilder$BodyInputStream.class */
    private static class BodyInputStream extends ServletInputStream {
        private final InputStream delegate;

        public BodyInputStream(byte[] body) {
            this.delegate = new ByteArrayInputStream(body);
        }

        @Override // javax.servlet.ServletInputStream
        public boolean isFinished() {
            return false;
        }

        @Override // javax.servlet.ServletInputStream
        public boolean isReady() {
            return true;
        }

        @Override // javax.servlet.ServletInputStream
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException();
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            return this.delegate.read();
        }

        @Override // java.io.InputStream
        public int read(byte[] b, int off, int len) throws IOException {
            return this.delegate.read(b, off, len);
        }

        @Override // java.io.InputStream
        public int read(byte[] b) throws IOException {
            return this.delegate.read(b);
        }

        @Override // java.io.InputStream
        public long skip(long n) throws IOException {
            return this.delegate.skip(n);
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            return this.delegate.available();
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.delegate.close();
        }

        @Override // java.io.InputStream
        public synchronized void mark(int readlimit) {
            this.delegate.mark(readlimit);
        }

        @Override // java.io.InputStream
        public synchronized void reset() throws IOException {
            this.delegate.reset();
        }

        @Override // java.io.InputStream
        public boolean markSupported() {
            return this.delegate.markSupported();
        }
    }
}
