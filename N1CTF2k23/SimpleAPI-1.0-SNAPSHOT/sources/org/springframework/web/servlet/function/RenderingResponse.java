package org.springframework.web.servlet.function;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import javax.servlet.http.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RenderingResponse.class */
public interface RenderingResponse extends ServerResponse {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RenderingResponse$Builder.class */
    public interface Builder {
        Builder modelAttribute(Object obj);

        Builder modelAttribute(String str, @Nullable Object obj);

        Builder modelAttributes(Object... objArr);

        Builder modelAttributes(Collection<?> collection);

        Builder modelAttributes(Map<String, ?> map);

        Builder header(String str, String... strArr);

        Builder headers(Consumer<HttpHeaders> consumer);

        Builder status(HttpStatus httpStatus);

        Builder status(int i);

        Builder cookie(Cookie cookie);

        Builder cookies(Consumer<MultiValueMap<String, Cookie>> consumer);

        RenderingResponse build();
    }

    String name();

    Map<String, Object> model();

    static Builder from(RenderingResponse other) {
        return new DefaultRenderingResponseBuilder(other);
    }

    static Builder create(String name) {
        return new DefaultRenderingResponseBuilder(name);
    }
}
