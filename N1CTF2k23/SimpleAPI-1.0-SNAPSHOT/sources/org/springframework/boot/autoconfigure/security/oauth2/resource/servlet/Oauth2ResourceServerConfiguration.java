package org.springframework.boot.autoconfigure.security.oauth2.resource.servlet;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerJwtConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerOpaqueTokenConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/servlet/Oauth2ResourceServerConfiguration.class */
class Oauth2ResourceServerConfiguration {
    Oauth2ResourceServerConfiguration() {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({JwtAuthenticationToken.class, JwtDecoder.class})
    @Import({OAuth2ResourceServerJwtConfiguration.JwtDecoderConfiguration.class, OAuth2ResourceServerJwtConfiguration.OAuth2WebSecurityConfigurerAdapter.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/servlet/Oauth2ResourceServerConfiguration$JwtConfiguration.class */
    static class JwtConfiguration {
        JwtConfiguration() {
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({BearerTokenAuthenticationToken.class, OpaqueTokenIntrospector.class})
    @Import({OAuth2ResourceServerOpaqueTokenConfiguration.OpaqueTokenIntrospectionClientConfiguration.class, OAuth2ResourceServerOpaqueTokenConfiguration.OAuth2WebSecurityConfigurerAdapter.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/servlet/Oauth2ResourceServerConfiguration$OpaqueTokenConfiguration.class */
    static class OpaqueTokenConfiguration {
        OpaqueTokenConfiguration() {
        }
    }
}
