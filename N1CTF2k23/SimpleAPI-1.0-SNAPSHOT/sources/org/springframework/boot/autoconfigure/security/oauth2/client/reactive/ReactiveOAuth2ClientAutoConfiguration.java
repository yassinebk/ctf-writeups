package org.springframework.boot.autoconfigure.security.oauth2.client.reactive;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.NoneNestedConditions;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientConfigurations;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import reactor.core.publisher.Flux;
@AutoConfigureBefore({ReactiveSecurityAutoConfiguration.class})
@EnableConfigurationProperties({OAuth2ClientProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Flux.class, EnableWebFluxSecurity.class, ClientRegistration.class})
@Conditional({NonServletApplicationCondition.class})
@Import({ReactiveOAuth2ClientConfigurations.ReactiveClientRegistrationRepositoryConfiguration.class, ReactiveOAuth2ClientConfigurations.ReactiveOAuth2ClientConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/reactive/ReactiveOAuth2ClientAutoConfiguration.class */
public class ReactiveOAuth2ClientAutoConfiguration {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/reactive/ReactiveOAuth2ClientAutoConfiguration$NonServletApplicationCondition.class */
    static class NonServletApplicationCondition extends NoneNestedConditions {
        NonServletApplicationCondition() {
            super(ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION);
        }

        @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/reactive/ReactiveOAuth2ClientAutoConfiguration$NonServletApplicationCondition$ServletApplicationCondition.class */
        static class ServletApplicationCondition {
            ServletApplicationCondition() {
            }
        }
    }
}
