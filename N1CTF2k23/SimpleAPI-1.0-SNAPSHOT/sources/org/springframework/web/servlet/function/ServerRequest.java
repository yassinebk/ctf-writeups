package org.springframework.web.servlet.function;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.Consumer;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRange;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.PathContainer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriBuilder;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/ServerRequest.class */
public interface ServerRequest {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/ServerRequest$Builder.class */
    public interface Builder {
        Builder method(HttpMethod httpMethod);

        Builder uri(URI uri);

        Builder header(String str, String... strArr);

        Builder headers(Consumer<HttpHeaders> consumer);

        Builder cookie(String str, String... strArr);

        Builder cookies(Consumer<MultiValueMap<String, Cookie>> consumer);

        Builder body(byte[] bArr);

        Builder body(String str);

        Builder attribute(String str, Object obj);

        Builder attributes(Consumer<Map<String, Object>> consumer);

        ServerRequest build();
    }

    String methodName();

    URI uri();

    UriBuilder uriBuilder();

    Headers headers();

    MultiValueMap<String, Cookie> cookies();

    Optional<InetSocketAddress> remoteAddress();

    List<HttpMessageConverter<?>> messageConverters();

    <T> T body(Class<T> cls) throws ServletException, IOException;

    <T> T body(ParameterizedTypeReference<T> parameterizedTypeReference) throws ServletException, IOException;

    Map<String, Object> attributes();

    MultiValueMap<String, String> params();

    Map<String, String> pathVariables();

    HttpSession session();

    Optional<Principal> principal();

    HttpServletRequest servletRequest();

    @Nullable
    default HttpMethod method() {
        return HttpMethod.resolve(methodName());
    }

    default String path() {
        return uri().getRawPath();
    }

    default PathContainer pathContainer() {
        return PathContainer.parsePath(path());
    }

    default Optional<Object> attribute(String name) {
        Map<String, Object> attributes = attributes();
        if (attributes.containsKey(name)) {
            return Optional.of(attributes.get(name));
        }
        return Optional.empty();
    }

    default Optional<String> param(String name) {
        List<String> paramValues = (List) params().get(name);
        if (CollectionUtils.isEmpty(paramValues)) {
            return Optional.empty();
        }
        String value = paramValues.get(0);
        if (value == null) {
            value = "";
        }
        return Optional.of(value);
    }

    default String pathVariable(String name) {
        Map<String, String> pathVariables = pathVariables();
        if (pathVariables.containsKey(name)) {
            return pathVariables().get(name);
        }
        throw new IllegalArgumentException("No path variable with name \"" + name + "\" available");
    }

    default Optional<ServerResponse> checkNotModified(Instant lastModified) {
        Assert.notNull(lastModified, "LastModified must not be null");
        return DefaultServerRequest.checkNotModified(servletRequest(), lastModified, null);
    }

    default Optional<ServerResponse> checkNotModified(String etag) {
        Assert.notNull(etag, "Etag must not be null");
        return DefaultServerRequest.checkNotModified(servletRequest(), null, etag);
    }

    default Optional<ServerResponse> checkNotModified(Instant lastModified, String etag) {
        Assert.notNull(lastModified, "LastModified must not be null");
        Assert.notNull(etag, "Etag must not be null");
        return DefaultServerRequest.checkNotModified(servletRequest(), lastModified, etag);
    }

    static ServerRequest create(HttpServletRequest servletRequest, List<HttpMessageConverter<?>> messageReaders) {
        return new DefaultServerRequest(servletRequest, messageReaders);
    }

    static Builder from(ServerRequest other) {
        return new DefaultServerRequestBuilder(other);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/ServerRequest$Headers.class */
    public interface Headers {
        List<MediaType> accept();

        List<Charset> acceptCharset();

        List<Locale.LanguageRange> acceptLanguage();

        OptionalLong contentLength();

        Optional<MediaType> contentType();

        @Nullable
        InetSocketAddress host();

        List<HttpRange> range();

        List<String> header(String str);

        HttpHeaders asHttpHeaders();

        @Nullable
        default String firstHeader(String headerName) {
            List<String> list = header(headerName);
            if (list.isEmpty()) {
                return null;
            }
            return list.get(0);
        }
    }
}
