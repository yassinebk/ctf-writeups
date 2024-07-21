package org.springframework.web.servlet.function;

import java.net.URI;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.function.Consumer;
import javax.servlet.http.Cookie;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/EntityResponse.class */
public interface EntityResponse<T> extends ServerResponse {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/EntityResponse$Builder.class */
    public interface Builder<T> {
        Builder<T> header(String str, String... strArr);

        Builder<T> headers(Consumer<HttpHeaders> consumer);

        Builder<T> status(HttpStatus httpStatus);

        Builder<T> status(int i);

        Builder<T> cookie(Cookie cookie);

        Builder<T> cookies(Consumer<MultiValueMap<String, Cookie>> consumer);

        Builder<T> allow(HttpMethod... httpMethodArr);

        Builder<T> allow(Set<HttpMethod> set);

        Builder<T> eTag(String str);

        Builder<T> lastModified(ZonedDateTime zonedDateTime);

        Builder<T> lastModified(Instant instant);

        Builder<T> location(URI uri);

        Builder<T> cacheControl(CacheControl cacheControl);

        Builder<T> varyBy(String... strArr);

        Builder<T> contentLength(long j);

        Builder<T> contentType(MediaType mediaType);

        EntityResponse<T> build();
    }

    T entity();

    static <T> Builder<T> fromObject(T t) {
        return DefaultEntityResponseBuilder.fromObject(t);
    }

    static <T> Builder<T> fromObject(T t, ParameterizedTypeReference<T> entityType) {
        return DefaultEntityResponseBuilder.fromObject(t, entityType);
    }
}
