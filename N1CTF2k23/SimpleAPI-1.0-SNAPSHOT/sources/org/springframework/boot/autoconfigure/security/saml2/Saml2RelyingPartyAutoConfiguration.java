package org.springframework.boot.autoconfigure.security.saml2;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
@AutoConfigureBefore({SecurityAutoConfiguration.class})
@EnableConfigurationProperties({Saml2RelyingPartyProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RelyingPartyRegistrationRepository.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Import({Saml2RelyingPartyRegistrationConfiguration.class, Saml2LoginConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/saml2/Saml2RelyingPartyAutoConfiguration.class */
public class Saml2RelyingPartyAutoConfiguration {
}
