package org.springframework.boot.autoconfigure.security.saml2;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.saml2.credentials.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.util.Assert;
@ConditionalOnMissingBean({RelyingPartyRegistrationRepository.class})
@Configuration(proxyBeanMethods = false)
@Conditional({RegistrationConfiguredCondition.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/saml2/Saml2RelyingPartyRegistrationConfiguration.class */
class Saml2RelyingPartyRegistrationConfiguration {
    Saml2RelyingPartyRegistrationConfiguration() {
    }

    @Bean
    RelyingPartyRegistrationRepository relyingPartyRegistrationRepository(Saml2RelyingPartyProperties properties) {
        List<RelyingPartyRegistration> registrations = (List) properties.getRegistration().entrySet().stream().map(this::asRegistration).collect(Collectors.toList());
        return new InMemoryRelyingPartyRegistrationRepository(registrations);
    }

    private RelyingPartyRegistration asRegistration(Map.Entry<String, Saml2RelyingPartyProperties.Registration> entry) {
        return asRegistration(entry.getKey(), entry.getValue());
    }

    private RelyingPartyRegistration asRegistration(String id, Saml2RelyingPartyProperties.Registration properties) {
        boolean signRequest = properties.getIdentityprovider().getSinglesignon().isSignRequest();
        validateSigningCredentials(properties, signRequest);
        RelyingPartyRegistration.Builder builder = RelyingPartyRegistration.withRegistrationId(id);
        builder.assertionConsumerServiceUrlTemplate("{baseUrl}/login/saml2/sso/{registrationId}");
        builder.providerDetails(details -> {
            details.webSsoUrl(properties.getIdentityprovider().getSinglesignon().getUrl());
        });
        builder.providerDetails(details2 -> {
            details2.entityId(properties.getIdentityprovider().getEntityId());
        });
        builder.providerDetails(details3 -> {
            details3.binding(properties.getIdentityprovider().getSinglesignon().getBinding());
        });
        builder.providerDetails(details4 -> {
            details4.signAuthNRequest(signRequest);
        });
        builder.credentials(credentials -> {
            credentials.addAll(asCredentials(properties));
        });
        return builder.build();
    }

    private void validateSigningCredentials(Saml2RelyingPartyProperties.Registration properties, boolean signRequest) {
        if (signRequest) {
            Assert.state(!properties.getSigning().getCredentials().isEmpty(), "Signing credentials must not be empty when authentication requests require signing.");
        }
    }

    private List<Saml2X509Credential> asCredentials(Saml2RelyingPartyProperties.Registration properties) {
        List<Saml2X509Credential> credentials = new ArrayList<>();
        Stream<R> map = properties.getSigning().getCredentials().stream().map(this::asSigningCredential);
        credentials.getClass();
        map.forEach((v1) -> {
            r1.add(v1);
        });
        Stream<R> map2 = properties.getIdentityprovider().getVerification().getCredentials().stream().map(this::asVerificationCredential);
        credentials.getClass();
        map2.forEach((v1) -> {
            r1.add(v1);
        });
        return credentials;
    }

    private Saml2X509Credential asSigningCredential(Saml2RelyingPartyProperties.Registration.Signing.Credential properties) {
        RSAPrivateKey privateKey = readPrivateKey(properties.getPrivateKeyLocation());
        X509Certificate certificate = readCertificate(properties.getCertificateLocation());
        return new Saml2X509Credential(privateKey, certificate, new Saml2X509Credential.Saml2X509CredentialType[]{Saml2X509Credential.Saml2X509CredentialType.SIGNING, Saml2X509Credential.Saml2X509CredentialType.DECRYPTION});
    }

    private Saml2X509Credential asVerificationCredential(Saml2RelyingPartyProperties.Identityprovider.Verification.Credential properties) {
        X509Certificate certificate = readCertificate(properties.getCertificateLocation());
        return new Saml2X509Credential(certificate, new Saml2X509Credential.Saml2X509CredentialType[]{Saml2X509Credential.Saml2X509CredentialType.ENCRYPTION, Saml2X509Credential.Saml2X509CredentialType.VERIFICATION});
    }

    private RSAPrivateKey readPrivateKey(Resource location) {
        Assert.state(location != null, "No private key location specified");
        Assert.state(location.exists(), "Private key location '" + location + "' does not exist");
        try {
            InputStream inputStream = location.getInputStream();
            RSAPrivateKey rSAPrivateKey = (RSAPrivateKey) RsaKeyConverters.pkcs8().convert(inputStream);
            if (inputStream != null) {
                if (0 != 0) {
                    inputStream.close();
                } else {
                    inputStream.close();
                }
            }
            return rSAPrivateKey;
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private X509Certificate readCertificate(Resource location) {
        Assert.state(location != null, "No certificate location specified");
        Assert.state(location.exists(), "Certificate  location '" + location + "' does not exist");
        try {
            InputStream inputStream = location.getInputStream();
            X509Certificate x509Certificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(inputStream);
            if (inputStream != null) {
                if (0 != 0) {
                    inputStream.close();
                } else {
                    inputStream.close();
                }
            }
            return x509Certificate;
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
