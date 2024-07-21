package org.springframework.boot.autoconfigure.security.saml2;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.core.io.Resource;
import org.springframework.security.saml2.provider.service.registration.Saml2MessageBinding;
@ConfigurationProperties("spring.security.saml2.relyingparty")
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/saml2/Saml2RelyingPartyProperties.class */
public class Saml2RelyingPartyProperties {
    private Map<String, Registration> registration = new LinkedHashMap();

    public Map<String, Registration> getRegistration() {
        return this.registration;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/saml2/Saml2RelyingPartyProperties$Registration.class */
    public static class Registration {
        private final Signing signing = new Signing();
        private Identityprovider identityprovider = new Identityprovider();

        public Signing getSigning() {
            return this.signing;
        }

        public Identityprovider getIdentityprovider() {
            return this.identityprovider;
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/saml2/Saml2RelyingPartyProperties$Registration$Signing.class */
        public static class Signing {
            private List<Credential> credentials = new ArrayList();

            public List<Credential> getCredentials() {
                return this.credentials;
            }

            /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/saml2/Saml2RelyingPartyProperties$Registration$Signing$Credential.class */
            public static class Credential {
                private Resource privateKeyLocation;
                private Resource certificateLocation;

                public Resource getPrivateKeyLocation() {
                    return this.privateKeyLocation;
                }

                public void setPrivateKeyLocation(Resource privateKey) {
                    this.privateKeyLocation = privateKey;
                }

                public Resource getCertificateLocation() {
                    return this.certificateLocation;
                }

                public void setCertificateLocation(Resource certificate) {
                    this.certificateLocation = certificate;
                }
            }
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/saml2/Saml2RelyingPartyProperties$Identityprovider.class */
    public static class Identityprovider {
        private String entityId;
        private Singlesignon singlesignon = new Singlesignon();
        private Verification verification = new Verification();

        public String getEntityId() {
            return this.entityId;
        }

        public void setEntityId(String entityId) {
            this.entityId = entityId;
        }

        @DeprecatedConfigurationProperty(reason = "moved to 'singlesignon.url'")
        @Deprecated
        public String getSsoUrl() {
            return this.singlesignon.getUrl();
        }

        @Deprecated
        public void setSsoUrl(String ssoUrl) {
            this.singlesignon.setUrl(ssoUrl);
        }

        public Singlesignon getSinglesignon() {
            return this.singlesignon;
        }

        public Verification getVerification() {
            return this.verification;
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/saml2/Saml2RelyingPartyProperties$Identityprovider$Singlesignon.class */
        public static class Singlesignon {
            private String url;
            private Saml2MessageBinding binding = Saml2MessageBinding.REDIRECT;
            private boolean signRequest = true;

            public String getUrl() {
                return this.url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public Saml2MessageBinding getBinding() {
                return this.binding;
            }

            public void setBinding(Saml2MessageBinding binding) {
                this.binding = binding;
            }

            public boolean isSignRequest() {
                return this.signRequest;
            }

            public void setSignRequest(boolean signRequest) {
                this.signRequest = signRequest;
            }
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/saml2/Saml2RelyingPartyProperties$Identityprovider$Verification.class */
        public static class Verification {
            private List<Credential> credentials = new ArrayList();

            public List<Credential> getCredentials() {
                return this.credentials;
            }

            /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/saml2/Saml2RelyingPartyProperties$Identityprovider$Verification$Credential.class */
            public static class Credential {
                private Resource certificate;

                public Resource getCertificateLocation() {
                    return this.certificate;
                }

                public void setCertificateLocation(Resource certificate) {
                    this.certificate = certificate;
                }
            }
        }
    }
}
