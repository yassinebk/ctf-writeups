package org.springframework.boot.autoconfigure.security.oauth2.resource.servlet;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.Oauth2ResourceServerConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
@AutoConfigureBefore({SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
@EnableConfigurationProperties({OAuth2ResourceServerProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import({Oauth2ResourceServerConfiguration.JwtConfiguration.class, Oauth2ResourceServerConfiguration.OpaqueTokenConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/servlet/OAuth2ResourceServerAutoConfiguration.class */
public class OAuth2ResourceServerAutoConfiguration {
}
