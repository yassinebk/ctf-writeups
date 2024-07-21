package org.springframework.boot.autoconfigure.security.rsocket;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.rsocket.core.SecuritySocketAcceptorInterceptor;
@EnableRSocketSecurity
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({SecuritySocketAcceptorInterceptor.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/rsocket/RSocketSecurityAutoConfiguration.class */
public class RSocketSecurityAutoConfiguration {
    @Bean
    RSocketServerCustomizer springSecurityRSocketSecurity(SecuritySocketAcceptorInterceptor interceptor) {
        return server -> {
            server.interceptors(registry -> {
                registry.forSocketAcceptor(interceptor);
            });
        };
    }
}
