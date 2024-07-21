package org.springframework.boot.autoconfigure.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.jpa.EntityManagerFactoryDependsOnPostProcessor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({HazelcastInstance.class, LocalContainerEntityManagerFactoryBean.class})
@AutoConfigureAfter({HazelcastAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Import({HazelcastInstanceEntityManagerFactoryDependsOnPostProcessor.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastJpaDependencyAutoConfiguration.class */
public class HazelcastJpaDependencyAutoConfiguration {

    @Conditional({OnHazelcastAndJpaCondition.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastJpaDependencyAutoConfiguration$HazelcastInstanceEntityManagerFactoryDependsOnPostProcessor.class */
    static class HazelcastInstanceEntityManagerFactoryDependsOnPostProcessor extends EntityManagerFactoryDependsOnPostProcessor {
        HazelcastInstanceEntityManagerFactoryDependsOnPostProcessor() {
            super("hazelcastInstance");
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastJpaDependencyAutoConfiguration$OnHazelcastAndJpaCondition.class */
    static class OnHazelcastAndJpaCondition extends AllNestedConditions {
        OnHazelcastAndJpaCondition() {
            super(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnBean(name = {"hazelcastInstance"})
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastJpaDependencyAutoConfiguration$OnHazelcastAndJpaCondition$HasHazelcastInstance.class */
        static class HasHazelcastInstance {
            HasHazelcastInstance() {
            }
        }

        @ConditionalOnBean({AbstractEntityManagerFactoryBean.class})
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/hazelcast/HazelcastJpaDependencyAutoConfiguration$OnHazelcastAndJpaCondition$HasJpa.class */
        static class HasJpa {
            HasJpa() {
            }
        }
    }
}
