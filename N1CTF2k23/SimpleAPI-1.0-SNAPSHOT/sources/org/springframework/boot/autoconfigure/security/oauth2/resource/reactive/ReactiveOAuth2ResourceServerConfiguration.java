package org.springframework.boot.autoconfigure.security.oauth2.resource.reactive;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerJwkConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerOpaqueTokenConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/reactive/ReactiveOAuth2ResourceServerConfiguration.class */
class ReactiveOAuth2ResourceServerConfiguration {
    ReactiveOAuth2ResourceServerConfiguration() {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({BearerTokenAuthenticationToken.class, ReactiveJwtDecoder.class})
    @Import({ReactiveOAuth2ResourceServerJwkConfiguration.JwtConfiguration.class, ReactiveOAuth2ResourceServerJwkConfiguration.WebSecurityConfiguration.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/reactive/ReactiveOAuth2ResourceServerConfiguration$JwtConfiguration.class */
    static class JwtConfiguration {
        JwtConfiguration() {
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({BearerTokenAuthenticationToken.class, ReactiveOpaqueTokenIntrospector.class})
    @Import({ReactiveOAuth2ResourceServerOpaqueTokenConfiguration.OpaqueTokenIntrospectionClientConfiguration.class, ReactiveOAuth2ResourceServerOpaqueTokenConfiguration.WebSecurityConfiguration.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/reactive/ReactiveOAuth2ResourceServerConfiguration$OpaqueTokenConfiguration.class */
    static class OpaqueTokenConfiguration {
        OpaqueTokenConfiguration() {
        }
    }
}
