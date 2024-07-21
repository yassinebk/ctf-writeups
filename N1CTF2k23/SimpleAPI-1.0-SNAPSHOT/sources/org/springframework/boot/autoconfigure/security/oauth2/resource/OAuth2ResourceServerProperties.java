package org.springframework.boot.autoconfigure.security.oauth2.resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
@ConfigurationProperties(prefix = "spring.security.oauth2.resourceserver")
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/OAuth2ResourceServerProperties.class */
public class OAuth2ResourceServerProperties {
    private final Jwt jwt = new Jwt();
    private final Opaquetoken opaqueToken = new Opaquetoken();

    public Jwt getJwt() {
        return this.jwt;
    }

    public Opaquetoken getOpaquetoken() {
        return this.opaqueToken;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/OAuth2ResourceServerProperties$Jwt.class */
    public static class Jwt {
        private String jwkSetUri;
        private String jwsAlgorithm = "RS256";
        private String issuerUri;
        private Resource publicKeyLocation;

        public String getJwkSetUri() {
            return this.jwkSetUri;
        }

        public void setJwkSetUri(String jwkSetUri) {
            this.jwkSetUri = jwkSetUri;
        }

        public String getJwsAlgorithm() {
            return this.jwsAlgorithm;
        }

        public void setJwsAlgorithm(String jwsAlgorithm) {
            this.jwsAlgorithm = jwsAlgorithm;
        }

        public String getIssuerUri() {
            return this.issuerUri;
        }

        public void setIssuerUri(String issuerUri) {
            this.issuerUri = issuerUri;
        }

        public Resource getPublicKeyLocation() {
            return this.publicKeyLocation;
        }

        public void setPublicKeyLocation(Resource publicKeyLocation) {
            this.publicKeyLocation = publicKeyLocation;
        }

        public String readPublicKey() throws IOException {
            Assert.notNull(this.publicKeyLocation, "PublicKeyLocation must not be null");
            if (!this.publicKeyLocation.exists()) {
                throw new InvalidConfigurationPropertyValueException("spring.security.oauth2.resourceserver.public-key-location", this.publicKeyLocation, "Public key location does not exist");
            }
            InputStream inputStream = this.publicKeyLocation.getInputStream();
            Throwable th = null;
            try {
                String copyToString = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
                if (inputStream != null) {
                    if (0 != 0) {
                        try {
                            inputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        inputStream.close();
                    }
                }
                return copyToString;
            } finally {
            }
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/oauth2/resource/OAuth2ResourceServerProperties$Opaquetoken.class */
    public static class Opaquetoken {
        private String clientId;
        private String clientSecret;
        private String introspectionUri;

        public String getClientId() {
            return this.clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return this.clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getIntrospectionUri() {
            return this.introspectionUri;
        }

        public void setIntrospectionUri(String introspectionUri) {
            this.introspectionUri = introspectionUri;
        }
    }
}
