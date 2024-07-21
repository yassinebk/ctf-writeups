package org.springframework.boot.autoconfigure.security.saml2;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
@Configuration(proxyBeanMethods = false)
@ConditionalOnBean({RelyingPartyRegistrationRepository.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/saml2/Saml2LoginConfiguration.class */
class Saml2LoginConfiguration {
    Saml2LoginConfiguration() {
    }

    @ConditionalOnMissingBean({WebSecurityConfigurerAdapter.class})
    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/saml2/Saml2LoginConfiguration$Saml2LoginConfigurerAdapter.class */
    static class Saml2LoginConfigurerAdapter extends WebSecurityConfigurerAdapter {
        Saml2LoginConfigurerAdapter() {
        }

        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests(requests -> {
                ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) requests.anyRequest()).authenticated();
            }).saml2Login();
        }
    }
}
