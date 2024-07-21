package org.springframework.boot.cloud;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/cloud/CloudPlatform.class */
public enum CloudPlatform {
    NONE { // from class: org.springframework.boot.cloud.CloudPlatform.1
        @Override // org.springframework.boot.cloud.CloudPlatform
        public boolean isDetected(Environment environment) {
            return false;
        }
    },
    CLOUD_FOUNDRY { // from class: org.springframework.boot.cloud.CloudPlatform.2
        @Override // org.springframework.boot.cloud.CloudPlatform
        public boolean isDetected(Environment environment) {
            return environment.containsProperty("VCAP_APPLICATION") || environment.containsProperty("VCAP_SERVICES");
        }
    },
    HEROKU { // from class: org.springframework.boot.cloud.CloudPlatform.3
        @Override // org.springframework.boot.cloud.CloudPlatform
        public boolean isDetected(Environment environment) {
            return environment.containsProperty("DYNO");
        }
    },
    SAP { // from class: org.springframework.boot.cloud.CloudPlatform.4
        @Override // org.springframework.boot.cloud.CloudPlatform
        public boolean isDetected(Environment environment) {
            return environment.containsProperty("HC_LANDSCAPE");
        }
    },
    KUBERNETES { // from class: org.springframework.boot.cloud.CloudPlatform.5
        private static final String KUBERNETES_SERVICE_HOST = "KUBERNETES_SERVICE_HOST";
        private static final String KUBERNETES_SERVICE_PORT = "KUBERNETES_SERVICE_PORT";
        private static final String SERVICE_HOST_SUFFIX = "_SERVICE_HOST";
        private static final String SERVICE_PORT_SUFFIX = "_SERVICE_PORT";

        @Override // org.springframework.boot.cloud.CloudPlatform
        public boolean isDetected(Environment environment) {
            if (environment instanceof ConfigurableEnvironment) {
                return isAutoDetected((ConfigurableEnvironment) environment);
            }
            return false;
        }

        private boolean isAutoDetected(ConfigurableEnvironment environment) {
            PropertySource<?> environmentPropertySource = environment.getPropertySources().get("systemEnvironment");
            if (environmentPropertySource != null) {
                if (environmentPropertySource.containsProperty(KUBERNETES_SERVICE_HOST) && environmentPropertySource.containsProperty(KUBERNETES_SERVICE_PORT)) {
                    return true;
                }
                if (environmentPropertySource instanceof EnumerablePropertySource) {
                    return isAutoDetected((EnumerablePropertySource) environmentPropertySource);
                }
                return false;
            }
            return false;
        }

        private boolean isAutoDetected(EnumerablePropertySource<?> environmentPropertySource) {
            String[] propertyNames;
            for (String propertyName : environmentPropertySource.getPropertyNames()) {
                if (propertyName.endsWith(SERVICE_HOST_SUFFIX)) {
                    String serviceName = propertyName.substring(0, propertyName.length() - SERVICE_HOST_SUFFIX.length());
                    if (environmentPropertySource.getProperty(serviceName + SERVICE_PORT_SUFFIX) != null) {
                        return true;
                    }
                }
            }
            return false;
        }
    };

    public abstract boolean isDetected(Environment environment);

    public boolean isActive(Environment environment) {
        return isEnforced(environment) || isDetected(environment);
    }

    public boolean isEnforced(Environment environment) {
        String platform = environment.getProperty("spring.main.cloud-platform");
        return name().equalsIgnoreCase(platform);
    }

    public boolean isUsingForwardHeaders() {
        return true;
    }

    public static CloudPlatform getActive(Environment environment) {
        CloudPlatform[] values;
        if (environment != null) {
            for (CloudPlatform cloudPlatform : values()) {
                if (cloudPlatform.isActive(environment)) {
                    return cloudPlatform;
                }
            }
            return null;
        }
        return null;
    }
}
