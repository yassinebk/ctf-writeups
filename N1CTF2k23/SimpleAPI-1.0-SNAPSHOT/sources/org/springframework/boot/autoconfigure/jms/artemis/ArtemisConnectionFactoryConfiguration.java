package org.springframework.boot.autoconfigure.jms.artemis;

import javax.jms.ConnectionFactory;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.catalina.Lifecycle;
import org.apache.commons.pool2.PooledObject;
import org.messaginghub.pooled.jms.JmsPoolConnectionFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.JmsPoolConnectionFactoryFactory;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
@ConditionalOnMissingBean({ConnectionFactory.class})
@Configuration(proxyBeanMethods = false)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jms/artemis/ArtemisConnectionFactoryConfiguration.class */
class ArtemisConnectionFactoryConfiguration {
    ArtemisConnectionFactoryConfiguration() {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({CachingConnectionFactory.class})
    @ConditionalOnProperty(prefix = "spring.artemis.pool", name = {"enabled"}, havingValue = "false", matchIfMissing = true)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jms/artemis/ArtemisConnectionFactoryConfiguration$SimpleConnectionFactoryConfiguration.class */
    static class SimpleConnectionFactoryConfiguration {
        private final ArtemisProperties properties;
        private final ListableBeanFactory beanFactory;

        SimpleConnectionFactoryConfiguration(ArtemisProperties properties, ListableBeanFactory beanFactory) {
            this.properties = properties;
            this.beanFactory = beanFactory;
        }

        @ConditionalOnProperty(prefix = "spring.jms.cache", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
        @Bean(name = {"jmsConnectionFactory"})
        CachingConnectionFactory cachingJmsConnectionFactory(JmsProperties jmsProperties) {
            JmsProperties.Cache cacheProperties = jmsProperties.getCache();
            CachingConnectionFactory connectionFactory = new CachingConnectionFactory(createConnectionFactory());
            connectionFactory.setCacheConsumers(cacheProperties.isConsumers());
            connectionFactory.setCacheProducers(cacheProperties.isProducers());
            connectionFactory.setSessionCacheSize(cacheProperties.getSessionCacheSize());
            return connectionFactory;
        }

        @ConditionalOnProperty(prefix = "spring.jms.cache", name = {"enabled"}, havingValue = "false")
        @Bean(name = {"jmsConnectionFactory"})
        ActiveMQConnectionFactory jmsConnectionFactory() {
            return createConnectionFactory();
        }

        private ActiveMQConnectionFactory createConnectionFactory() {
            return new ArtemisConnectionFactoryFactory(this.beanFactory, this.properties).createConnectionFactory(ActiveMQConnectionFactory.class);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({JmsPoolConnectionFactory.class, PooledObject.class})
    @ConditionalOnProperty(prefix = "spring.artemis.pool", name = {"enabled"}, havingValue = "true")
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jms/artemis/ArtemisConnectionFactoryConfiguration$PooledConnectionFactoryConfiguration.class */
    static class PooledConnectionFactoryConfiguration {
        PooledConnectionFactoryConfiguration() {
        }

        @Bean(destroyMethod = Lifecycle.STOP_EVENT)
        JmsPoolConnectionFactory jmsConnectionFactory(ListableBeanFactory beanFactory, ArtemisProperties properties) {
            return new JmsPoolConnectionFactoryFactory(properties.getPool()).createPooledConnectionFactory(new ArtemisConnectionFactoryFactory(beanFactory, properties).createConnectionFactory(ActiveMQConnectionFactory.class));
        }
    }
}
