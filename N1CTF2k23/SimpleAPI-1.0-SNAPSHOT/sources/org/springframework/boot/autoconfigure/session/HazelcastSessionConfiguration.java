package org.springframework.boot.autoconfigure.session;

import com.hazelcast.core.HazelcastInstance;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.SessionRepository;
import org.springframework.session.hazelcast.HazelcastIndexedSessionRepository;
import org.springframework.session.hazelcast.config.annotation.web.http.HazelcastHttpSessionConfiguration;
@EnableConfigurationProperties({HazelcastSessionProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({HazelcastIndexedSessionRepository.class})
@ConditionalOnMissingBean({SessionRepository.class})
@ConditionalOnBean({HazelcastInstance.class})
@Conditional({ServletSessionCondition.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/session/HazelcastSessionConfiguration.class */
class HazelcastSessionConfiguration {
    HazelcastSessionConfiguration() {
    }

    @Configuration
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/session/HazelcastSessionConfiguration$SpringBootHazelcastHttpSessionConfiguration.class */
    public static class SpringBootHazelcastHttpSessionConfiguration extends HazelcastHttpSessionConfiguration {
        @Autowired
        public void customize(SessionProperties sessionProperties, HazelcastSessionProperties hazelcastSessionProperties) {
            Duration timeout = sessionProperties.getTimeout();
            if (timeout != null) {
                setMaxInactiveIntervalInSeconds((int) timeout.getSeconds());
            }
            setSessionMapName(hazelcastSessionProperties.getMapName());
            setFlushMode(hazelcastSessionProperties.getFlushMode());
            setSaveMode(hazelcastSessionProperties.getSaveMode());
        }
    }
}
