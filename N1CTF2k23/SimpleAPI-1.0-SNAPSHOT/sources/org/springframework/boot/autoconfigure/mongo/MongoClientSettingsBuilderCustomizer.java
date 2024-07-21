package org.springframework.boot.autoconfigure.mongo;

import com.mongodb.MongoClientSettings;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/MongoClientSettingsBuilderCustomizer.class */
public interface MongoClientSettingsBuilderCustomizer {
    void customize(MongoClientSettings.Builder clientSettingsBuilder);
}
