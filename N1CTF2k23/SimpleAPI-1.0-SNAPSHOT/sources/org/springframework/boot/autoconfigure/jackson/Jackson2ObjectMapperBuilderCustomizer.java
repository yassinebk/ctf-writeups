package org.springframework.boot.autoconfigure.jackson;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jackson/Jackson2ObjectMapperBuilderCustomizer.class */
public interface Jackson2ObjectMapperBuilderCustomizer {
    void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder);
}
