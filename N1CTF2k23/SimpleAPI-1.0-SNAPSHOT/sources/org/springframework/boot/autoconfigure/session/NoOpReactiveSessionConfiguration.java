package org.springframework.boot.autoconfigure.session;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ReactiveSessionRepository;
@ConditionalOnMissingBean({ReactiveSessionRepository.class})
@Configuration(proxyBeanMethods = false)
@Conditional({ReactiveSessionCondition.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/session/NoOpReactiveSessionConfiguration.class */
class NoOpReactiveSessionConfiguration {
    NoOpReactiveSessionConfiguration() {
    }
}
