package org.springframework.boot.autoconfigure.security.oauth2.resource.servlet;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
@Configuration(proxyBeanMethods = false)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/servlet/OAuth2ResourceServerOpaqueTokenConfiguration.class */
class OAuth2ResourceServerOpaqueTokenConfiguration {
    OAuth2ResourceServerOpaqueTokenConfiguration() {
    }

    @ConditionalOnMissingBean({OpaqueTokenIntrospector.class})
    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/servlet/OAuth2ResourceServerOpaqueTokenConfiguration$OpaqueTokenIntrospectionClientConfiguration.class */
    static class OpaqueTokenIntrospectionClientConfiguration {
        OpaqueTokenIntrospectionClientConfiguration() {
        }

        @ConditionalOnProperty(name = {"spring.security.oauth2.resourceserver.opaquetoken.introspection-uri"})
        @Bean
        NimbusOpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2ResourceServerProperties properties) {
            OAuth2ResourceServerProperties.Opaquetoken opaqueToken = properties.getOpaquetoken();
            return new NimbusOpaqueTokenIntrospector(opaqueToken.getIntrospectionUri(), opaqueToken.getClientId(), opaqueToken.getClientSecret());
        }
    }

    @ConditionalOnMissingBean({WebSecurityConfigurerAdapter.class})
    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/servlet/OAuth2ResourceServerOpaqueTokenConfiguration$OAuth2WebSecurityConfigurerAdapter.class */
    static class OAuth2WebSecurityConfigurerAdapter {
        OAuth2WebSecurityConfigurerAdapter() {
        }

        @ConditionalOnBean({OpaqueTokenIntrospector.class})
        @Bean
        WebSecurityConfigurerAdapter opaqueTokenWebSecurityConfigurerAdapter() {
            return new WebSecurityConfigurerAdapter() { // from class: org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerOpaqueTokenConfiguration.OAuth2WebSecurityConfigurerAdapter.1
                protected void configure(HttpSecurity http) throws Exception {
                    http.authorizeRequests(requests -> {
                        ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) requests.anyRequest()).authenticated();
                    });
                    http.oauth2ResourceServer((v0) -> {
                        v0.opaqueToken();
                    });
                }
            };
        }
    }
}
