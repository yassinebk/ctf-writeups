package org.springframework.boot.web.codec;

import org.springframework.http.codec.CodecConfigurer;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/codec/CodecCustomizer.class */
public interface CodecCustomizer {
    void customize(CodecConfigurer configurer);
}
