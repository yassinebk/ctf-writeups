package org.springframework.boot.autoconfigure.security.oauth2.resource.reactive;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.IssuerUriCondition;
import org.springframework.boot.autoconfigure.security.oauth2.resource.KeyValueCondition;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;
@Configuration(proxyBeanMethods = false)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/reactive/ReactiveOAuth2ResourceServerJwkConfiguration.class */
class ReactiveOAuth2ResourceServerJwkConfiguration {
    ReactiveOAuth2ResourceServerJwkConfiguration() {
    }

    @ConditionalOnMissingBean({ReactiveJwtDecoder.class})
    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/reactive/ReactiveOAuth2ResourceServerJwkConfiguration$JwtConfiguration.class */
    static class JwtConfiguration {
        private final OAuth2ResourceServerProperties.Jwt properties;

        JwtConfiguration(OAuth2ResourceServerProperties properties) {
            this.properties = properties.getJwt();
        }

        @ConditionalOnProperty(name = {"spring.security.oauth2.resourceserver.jwt.jwk-set-uri"})
        @Bean
        ReactiveJwtDecoder jwtDecoder() {
            NimbusReactiveJwtDecoder nimbusReactiveJwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(this.properties.getJwkSetUri()).jwsAlgorithm(SignatureAlgorithm.from(this.properties.getJwsAlgorithm())).build();
            String issuerUri = this.properties.getIssuerUri();
            if (issuerUri != null) {
                nimbusReactiveJwtDecoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuerUri));
            }
            return nimbusReactiveJwtDecoder;
        }

        @Conditional({KeyValueCondition.class})
        @Bean
        NimbusReactiveJwtDecoder jwtDecoderByPublicKeyValue() throws Exception {
            RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(getKeySpec(this.properties.readPublicKey())));
            return NimbusReactiveJwtDecoder.withPublicKey(publicKey).signatureAlgorithm(SignatureAlgorithm.from(this.properties.getJwsAlgorithm())).build();
        }

        private byte[] getKeySpec(String keyValue) {
            return Base64.getMimeDecoder().decode(keyValue.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", ""));
        }

        @Conditional({IssuerUriCondition.class})
        @Bean
        ReactiveJwtDecoder jwtDecoderByIssuerUri() {
            return ReactiveJwtDecoders.fromIssuerLocation(this.properties.getIssuerUri());
        }
    }

    @ConditionalOnMissingBean({SecurityWebFilterChain.class})
    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/reactive/ReactiveOAuth2ResourceServerJwkConfiguration$WebSecurityConfiguration.class */
    static class WebSecurityConfiguration {
        WebSecurityConfiguration() {
        }

        @ConditionalOnBean({ReactiveJwtDecoder.class})
        @Bean
        SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ReactiveJwtDecoder jwtDecoder) {
            http.authorizeExchange(exchanges -> {
                exchanges.anyExchange().authenticated();
            });
            http.oauth2ResourceServer(server -> {
                customDecoder(server, jwtDecoder);
            });
            return http.build();
        }

        private void customDecoder(ServerHttpSecurity.OAuth2ResourceServerSpec server, ReactiveJwtDecoder decoder) {
            server.jwt(jwt -> {
                jwt.jwtDecoder(decoder);
            });
        }
    }
}
