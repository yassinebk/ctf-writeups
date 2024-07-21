package org.springframework.boot.autoconfigure.security.oauth2.client.reactive;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.ClientsConfiguredCondition;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/reactive/ReactiveOAuth2ClientConfigurations.class */
class ReactiveOAuth2ClientConfigurations {
    ReactiveOAuth2ClientConfigurations() {
    }

    @ConditionalOnMissingBean({ReactiveClientRegistrationRepository.class})
    @Configuration(proxyBeanMethods = false)
    @Conditional({ClientsConfiguredCondition.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/reactive/ReactiveOAuth2ClientConfigurations$ReactiveClientRegistrationRepositoryConfiguration.class */
    static class ReactiveClientRegistrationRepositoryConfiguration {
        ReactiveClientRegistrationRepositoryConfiguration() {
        }

        @Bean
        InMemoryReactiveClientRegistrationRepository clientRegistrationRepository(OAuth2ClientProperties properties) {
            List<ClientRegistration> registrations = new ArrayList<>((Collection<? extends ClientRegistration>) OAuth2ClientPropertiesRegistrationAdapter.getClientRegistrations(properties).values());
            return new InMemoryReactiveClientRegistrationRepository(registrations);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnBean({ReactiveClientRegistrationRepository.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/reactive/ReactiveOAuth2ClientConfigurations$ReactiveOAuth2ClientConfiguration.class */
    static class ReactiveOAuth2ClientConfiguration {
        ReactiveOAuth2ClientConfiguration() {
        }

        @ConditionalOnMissingBean
        @Bean
        ReactiveOAuth2AuthorizedClientService authorizedClientService(ReactiveClientRegistrationRepository clientRegistrationRepository) {
            return new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrationRepository);
        }

        @ConditionalOnMissingBean
        @Bean
        ServerOAuth2AuthorizedClientRepository authorizedClientRepository(ReactiveOAuth2AuthorizedClientService authorizedClientService) {
            return new AuthenticatedPrincipalServerOAuth2AuthorizedClientRepository(authorizedClientService);
        }

        @Configuration(proxyBeanMethods = false)
        @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/client/reactive/ReactiveOAuth2ClientConfigurations$ReactiveOAuth2ClientConfiguration$SecurityWebFilterChainConfiguration.class */
        static class SecurityWebFilterChainConfiguration {
            SecurityWebFilterChainConfiguration() {
            }

            @ConditionalOnMissingBean
            @Bean
            SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
                http.authorizeExchange().anyExchange().authenticated();
                http.oauth2Login();
                http.oauth2Client();
                return http.build();
            }
        }
    }
}
