package org.springframework.boot.autoconfigure.jms.artemis;

import org.apache.activemq.artemis.core.config.Configuration;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jms/artemis/ArtemisConfigurationCustomizer.class */
public interface ArtemisConfigurationCustomizer {
    void customize(Configuration configuration);
}
