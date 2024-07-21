package org.springframework.boot.autoconfigure.mongo.embedded;

import de.flapdoodle.embed.mongo.config.DownloadConfigBuilder;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/embedded/DownloadConfigBuilderCustomizer.class */
public interface DownloadConfigBuilderCustomizer {
    void customize(DownloadConfigBuilder downloadConfigBuilder);
}
