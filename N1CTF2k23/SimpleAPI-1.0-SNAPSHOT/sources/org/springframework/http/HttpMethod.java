package org.springframework.http;

import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/HttpMethod.class */
public enum HttpMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;
    
    private static final Map<String, HttpMethod> mappings = new HashMap(16);

    static {
        HttpMethod[] values;
        for (HttpMethod httpMethod : values()) {
            mappings.put(httpMethod.name(), httpMethod);
        }
    }

    @Nullable
    public static HttpMethod resolve(@Nullable String method) {
        if (method != null) {
            return mappings.get(method);
        }
        return null;
    }

    public boolean matches(String method) {
        return this == resolve(method);
    }
}
