package org.springframework.boot.autoconfigure.context;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix = "spring.lifecycle")
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/context/LifecycleProperties.class */
public class LifecycleProperties {
    private Duration timeoutPerShutdownPhase = Duration.ofSeconds(30);

    public Duration getTimeoutPerShutdownPhase() {
        return this.timeoutPerShutdownPhase;
    }

    public void setTimeoutPerShutdownPhase(Duration timeoutPerShutdownPhase) {
        this.timeoutPerShutdownPhase = timeoutPerShutdownPhase;
    }
}
