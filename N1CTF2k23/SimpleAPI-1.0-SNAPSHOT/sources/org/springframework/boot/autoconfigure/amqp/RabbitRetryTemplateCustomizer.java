package org.springframework.boot.autoconfigure.amqp;

import org.springframework.retry.support.RetryTemplate;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitRetryTemplateCustomizer.class */
public interface RabbitRetryTemplateCustomizer {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitRetryTemplateCustomizer$Target.class */
    public enum Target {
        SENDER,
        LISTENER
    }

    void customize(Target target, RetryTemplate retryTemplate);
}
