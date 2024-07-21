package org.springframework.boot.autoconfigure.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import java.io.IOException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
@ConditionalOnMissingBean({HazelcastInstance.class})
@Configuration(proxyBeanMethods = false)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastServerConfiguration.class */
class HazelcastServerConfiguration {
    static final String CONFIG_SYSTEM_PROPERTY = "hazelcast.config";

    HazelcastServerConfiguration() {
    }

    @ConditionalOnMissingBean({Config.class})
    @Configuration(proxyBeanMethods = false)
    @Conditional({ConfigAvailableCondition.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastServerConfiguration$HazelcastServerConfigFileConfiguration.class */
    static class HazelcastServerConfigFileConfiguration {
        HazelcastServerConfigFileConfiguration() {
        }

        @Bean
        HazelcastInstance hazelcastInstance(HazelcastProperties properties) throws IOException {
            Resource config = properties.resolveConfigLocation();
            if (config != null) {
                return new HazelcastInstanceFactory(config).getHazelcastInstance();
            }
            return Hazelcast.newHazelcastInstance();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnSingleCandidate(Config.class)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastServerConfiguration$HazelcastServerConfigConfiguration.class */
    static class HazelcastServerConfigConfiguration {
        HazelcastServerConfigConfiguration() {
        }

        @Bean
        HazelcastInstance hazelcastInstance(Config config) {
            return new HazelcastInstanceFactory(config).getHazelcastInstance();
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastServerConfiguration$ConfigAvailableCondition.class */
    static class ConfigAvailableCondition extends HazelcastConfigResourceCondition {
        ConfigAvailableCondition() {
            super(HazelcastServerConfiguration.CONFIG_SYSTEM_PROPERTY, "file:./hazelcast.xml", "classpath:/hazelcast.xml", "file:./hazelcast.yaml", "classpath:/hazelcast.yaml");
        }
    }
}
