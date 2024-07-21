package org.springframework.boot.autoconfigure.security.oauth2.resource.servlet;

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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
@Configuration(proxyBeanMethods = false)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/servlet/OAuth2ResourceServerJwtConfiguration.class */
class OAuth2ResourceServerJwtConfiguration {
    OAuth2ResourceServerJwtConfiguration() {
    }

    @ConditionalOnMissingBean({JwtDecoder.class})
    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/servlet/OAuth2ResourceServerJwtConfiguration$JwtDecoderConfiguration.class */
    static class JwtDecoderConfiguration {
        private final OAuth2ResourceServerProperties.Jwt properties;

        JwtDecoderConfiguration(OAuth2ResourceServerProperties properties) {
            this.properties = properties.getJwt();
        }

        @ConditionalOnProperty(name = {"spring.security.oauth2.resourceserver.jwt.jwk-set-uri"})
        @Bean
        JwtDecoder jwtDecoderByJwkKeySetUri() {
            NimbusJwtDecoder nimbusJwtDecoder = NimbusJwtDecoder.withJwkSetUri(this.properties.getJwkSetUri()).jwsAlgorithm(SignatureAlgorithm.from(this.properties.getJwsAlgorithm())).build();
            String issuerUri = this.properties.getIssuerUri();
            if (issuerUri != null) {
                nimbusJwtDecoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(issuerUri));
            }
            return nimbusJwtDecoder;
        }

        @Conditional({KeyValueCondition.class})
        @Bean
        JwtDecoder jwtDecoderByPublicKeyValue() throws Exception {
            RSAPublicKey publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(getKeySpec(this.properties.readPublicKey())));
            return NimbusJwtDecoder.withPublicKey(publicKey).signatureAlgorithm(SignatureAlgorithm.from(this.properties.getJwsAlgorithm())).build();
        }

        private byte[] getKeySpec(String keyValue) {
            return Base64.getMimeDecoder().decode(keyValue.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", ""));
        }

        @Conditional({IssuerUriCondition.class})
        @Bean
        JwtDecoder jwtDecoderByIssuerUri() {
            return JwtDecoders.fromIssuerLocation(this.properties.getIssuerUri());
        }
    }

    @ConditionalOnMissingBean({WebSecurityConfigurerAdapter.class})
    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/servlet/OAuth2ResourceServerJwtConfiguration$OAuth2WebSecurityConfigurerAdapter.class */
    static class OAuth2WebSecurityConfigurerAdapter {
        OAuth2WebSecurityConfigurerAdapter() {
        }

        @ConditionalOnBean({JwtDecoder.class})
        @Bean
        WebSecurityConfigurerAdapter jwtDecoderWebSecurityConfigurerAdapter() {
            return new WebSecurityConfigurerAdapter() { // from class: org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerJwtConfiguration.OAuth2WebSecurityConfigurerAdapter.1
                protected void configure(HttpSecurity http) throws Exception {
                    http.authorizeRequests(requests -> {
                        ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) requests.anyRequest()).authenticated();
                    });
                    http.oauth2ResourceServer((v0) -> {
                        v0.jwt();
                    });
                }
            };
        }
    }
}
