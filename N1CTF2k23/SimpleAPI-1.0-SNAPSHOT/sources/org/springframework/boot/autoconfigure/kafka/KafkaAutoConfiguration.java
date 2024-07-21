package org.springframework.boot.autoconfigure.kafka;

import java.io.IOException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.security.jaas.KafkaJaasLoginModuleInitializer;
import org.springframework.kafka.support.LoggingProducerListener;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.transaction.KafkaTransactionManager;
@EnableConfigurationProperties({KafkaProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({KafkaTemplate.class})
@Import({KafkaAnnotationDrivenConfiguration.class, KafkaStreamsAnnotationDrivenConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/kafka/KafkaAutoConfiguration.class */
public class KafkaAutoConfiguration {
    private final KafkaProperties properties;

    public KafkaAutoConfiguration(KafkaProperties properties) {
        this.properties = properties;
    }

    @ConditionalOnMissingBean({KafkaTemplate.class})
    @Bean
    public KafkaTemplate<?, ?> kafkaTemplate(ProducerFactory<Object, Object> kafkaProducerFactory, ProducerListener<Object, Object> kafkaProducerListener, ObjectProvider<RecordMessageConverter> messageConverter) {
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
        kafkaTemplate.getClass();
        messageConverter.ifUnique(this::setMessageConverter);
        kafkaTemplate.setProducerListener(kafkaProducerListener);
        kafkaTemplate.setDefaultTopic(this.properties.getTemplate().getDefaultTopic());
        return kafkaTemplate;
    }

    @ConditionalOnMissingBean({ProducerListener.class})
    @Bean
    public ProducerListener<Object, Object> kafkaProducerListener() {
        return new LoggingProducerListener();
    }

    @ConditionalOnMissingBean({ConsumerFactory.class})
    @Bean
    public ConsumerFactory<?, ?> kafkaConsumerFactory(ObjectProvider<DefaultKafkaConsumerFactoryCustomizer> customizers) {
        DefaultKafkaConsumerFactory<Object, Object> factory = new DefaultKafkaConsumerFactory<>(this.properties.buildConsumerProperties());
        customizers.orderedStream().forEach(customizer -> {
            customizer.customize(factory);
        });
        return factory;
    }

    @ConditionalOnMissingBean({ProducerFactory.class})
    @Bean
    public ProducerFactory<?, ?> kafkaProducerFactory(ObjectProvider<DefaultKafkaProducerFactoryCustomizer> customizers) {
        DefaultKafkaProducerFactory<?, ?> factory = new DefaultKafkaProducerFactory<>(this.properties.buildProducerProperties());
        String transactionIdPrefix = this.properties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        customizers.orderedStream().forEach(customizer -> {
            customizer.customize(factory);
        });
        return factory;
    }

    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = {"spring.kafka.producer.transaction-id-prefix"})
    @Bean
    public KafkaTransactionManager<?, ?> kafkaTransactionManager(ProducerFactory<?, ?> producerFactory) {
        return new KafkaTransactionManager<>(producerFactory);
    }

    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = {"spring.kafka.jaas.enabled"})
    @Bean
    public KafkaJaasLoginModuleInitializer kafkaJaasInitializer() throws IOException {
        KafkaJaasLoginModuleInitializer jaas = new KafkaJaasLoginModuleInitializer();
        KafkaProperties.Jaas jaasProperties = this.properties.getJaas();
        if (jaasProperties.getControlFlag() != null) {
            jaas.setControlFlag(jaasProperties.getControlFlag());
        }
        if (jaasProperties.getLoginModule() != null) {
            jaas.setLoginModule(jaasProperties.getLoginModule());
        }
        jaas.setOptions(jaasProperties.getOptions());
        return jaas;
    }

    @ConditionalOnMissingBean
    @Bean
    public KafkaAdmin kafkaAdmin() {
        KafkaAdmin kafkaAdmin = new KafkaAdmin(this.properties.buildAdminProperties());
        kafkaAdmin.setFatalIfBrokerNotAvailable(this.properties.getAdmin().isFailFast());
        return kafkaAdmin;
    }
}
