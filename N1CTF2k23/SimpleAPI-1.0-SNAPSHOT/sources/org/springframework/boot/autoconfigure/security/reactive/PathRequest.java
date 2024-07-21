package org.springframework.boot.autoconfigure.security.reactive;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/reactive/PathRequest.class */
public final class PathRequest {
    private PathRequest() {
    }

    public static StaticResourceRequest toStaticResources() {
        return StaticResourceRequest.INSTANCE;
    }
}
