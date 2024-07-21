package org.springframework.boot.autoconfigure.gson;

import com.google.gson.GsonBuilder;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/gson/GsonBuilderCustomizer.class */
public interface GsonBuilderCustomizer {
    void customize(GsonBuilder gsonBuilder);
}