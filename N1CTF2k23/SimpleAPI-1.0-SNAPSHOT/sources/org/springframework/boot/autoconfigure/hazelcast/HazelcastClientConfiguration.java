package org.springframework.boot.autoconfigure.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import java.io.IOException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
@ConditionalOnMissingBean({HazelcastInstance.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({HazelcastClient.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastClientConfiguration.class */
class HazelcastClientConfiguration {
    static final String CONFIG_SYSTEM_PROPERTY = "hazelcast.client.config";

    HazelcastClientConfiguration() {
    }

    @ConditionalOnMissingBean({ClientConfig.class})
    @Configuration(proxyBeanMethods = false)
    @Conditional({ConfigAvailableCondition.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastClientConfiguration$HazelcastClientConfigFileConfiguration.class */
    static class HazelcastClientConfigFileConfiguration {
        HazelcastClientConfigFileConfiguration() {
        }

        @Bean
        HazelcastInstance hazelcastInstance(HazelcastProperties properties) throws IOException {
            Resource config = properties.resolveConfigLocation();
            if (config != null) {
                return new HazelcastClientFactory(config).getHazelcastInstance();
            }
            return HazelcastClient.newHazelcastClient();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnSingleCandidate(ClientConfig.class)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastClientConfiguration$HazelcastClientConfigConfiguration.class */
    static class HazelcastClientConfigConfiguration {
        HazelcastClientConfigConfiguration() {
        }

        @Bean
        HazelcastInstance hazelcastInstance(ClientConfig config) {
            return new HazelcastClientFactory(config).getHazelcastInstance();
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastClientConfiguration$ConfigAvailableCondition.class */
    static class ConfigAvailableCondition extends HazelcastConfigResourceCondition {
        ConfigAvailableCondition() {
            super(HazelcastClientConfiguration.CONFIG_SYSTEM_PROPERTY, "file:./hazelcast-client.xml", "classpath:/hazelcast-client.xml", "file:./hazelcast-client.yaml", "classpath:/hazelcast-client.yaml");
        }
    }
}
