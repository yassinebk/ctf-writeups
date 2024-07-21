package org.springframework.web.servlet.function;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.ModelAndView;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/ServerResponse.class */
public interface ServerResponse {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/ServerResponse$BodyBuilder.class */
    public interface BodyBuilder extends HeadersBuilder<BodyBuilder> {
        BodyBuilder contentLength(long j);

        BodyBuilder contentType(MediaType mediaType);

        ServerResponse body(Object obj);

        <T> ServerResponse body(T t, ParameterizedTypeReference<T> parameterizedTypeReference);

        ServerResponse render(String str, Object... objArr);

        ServerResponse render(String str, Map<String, ?> map);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/ServerResponse$Context.class */
    public interface Context {
        List<HttpMessageConverter<?>> messageConverters();
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/ServerResponse$HeadersBuilder.class */
    public interface HeadersBuilder<B extends HeadersBuilder<B>> {
        B header(String str, String... strArr);

        B headers(Consumer<HttpHeaders> consumer);

        B cookie(Cookie cookie);

        B cookies(Consumer<MultiValueMap<String, Cookie>> consumer);

        B allow(HttpMethod... httpMethodArr);

        B allow(Set<HttpMethod> set);

        B eTag(String str);

        B lastModified(ZonedDateTime zonedDateTime);

        B lastModified(Instant instant);

        B location(URI uri);

        B cacheControl(CacheControl cacheControl);

        B varyBy(String... strArr);

        ServerResponse build();

        ServerResponse build(BiFunction<HttpServletRequest, HttpServletResponse, ModelAndView> biFunction);
    }

    HttpStatus statusCode();

    int rawStatusCode();

    HttpHeaders headers();

    MultiValueMap<String, Cookie> cookies();

    @Nullable
    ModelAndView writeTo(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Context context) throws ServletException, IOException;

    static BodyBuilder from(ServerResponse other) {
        return new DefaultServerResponseBuilder(other);
    }

    static BodyBuilder status(HttpStatus status) {
        return new DefaultServerResponseBuilder(status);
    }

    static BodyBuilder status(int status) {
        return new DefaultServerResponseBuilder(status);
    }

    static BodyBuilder ok() {
        return status(HttpStatus.OK);
    }

    static BodyBuilder created(URI location) {
        BodyBuilder builder = status(HttpStatus.CREATED);
        return builder.location(location);
    }

    static BodyBuilder accepted() {
        return status(HttpStatus.ACCEPTED);
    }

    static HeadersBuilder<?> noContent() {
        return status(HttpStatus.NO_CONTENT);
    }

    static BodyBuilder seeOther(URI location) {
        BodyBuilder builder = status(HttpStatus.SEE_OTHER);
        return builder.location(location);
    }

    static BodyBuilder temporaryRedirect(URI location) {
        BodyBuilder builder = status(HttpStatus.TEMPORARY_REDIRECT);
        return builder.location(location);
    }

    static BodyBuilder permanentRedirect(URI location) {
        BodyBuilder builder = status(HttpStatus.PERMANENT_REDIRECT);
        return builder.location(location);
    }

    static BodyBuilder badRequest() {
        return status(HttpStatus.BAD_REQUEST);
    }

    static HeadersBuilder<?> notFound() {
        return status(HttpStatus.NOT_FOUND);
    }

    static BodyBuilder unprocessableEntity() {
        return status(HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
