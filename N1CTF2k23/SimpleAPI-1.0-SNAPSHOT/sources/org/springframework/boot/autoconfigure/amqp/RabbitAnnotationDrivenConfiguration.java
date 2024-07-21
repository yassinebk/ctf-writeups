package org.springframework.boot.autoconfigure.amqp;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.DirectRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({EnableRabbit.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitAnnotationDrivenConfiguration.class */
class RabbitAnnotationDrivenConfiguration {
    private final ObjectProvider<MessageConverter> messageConverter;
    private final ObjectProvider<MessageRecoverer> messageRecoverer;
    private final ObjectProvider<RabbitRetryTemplateCustomizer> retryTemplateCustomizers;
    private final RabbitProperties properties;

    RabbitAnnotationDrivenConfiguration(ObjectProvider<MessageConverter> messageConverter, ObjectProvider<MessageRecoverer> messageRecoverer, ObjectProvider<RabbitRetryTemplateCustomizer> retryTemplateCustomizers, RabbitProperties properties) {
        this.messageConverter = messageConverter;
        this.messageRecoverer = messageRecoverer;
        this.retryTemplateCustomizers = retryTemplateCustomizers;
        this.properties = properties;
    }

    @ConditionalOnMissingBean
    @Bean
    SimpleRabbitListenerContainerFactoryConfigurer simpleRabbitListenerContainerFactoryConfigurer() {
        SimpleRabbitListenerContainerFactoryConfigurer configurer = new SimpleRabbitListenerContainerFactoryConfigurer();
        configurer.setMessageConverter(this.messageConverter.getIfUnique());
        configurer.setMessageRecoverer(this.messageRecoverer.getIfUnique());
        configurer.setRetryTemplateCustomizers((List) this.retryTemplateCustomizers.orderedStream().collect(Collectors.toList()));
        configurer.setRabbitProperties(this.properties);
        return configurer;
    }

    @ConditionalOnMissingBean(name = {"rabbitListenerContainerFactory"})
    @ConditionalOnProperty(prefix = "spring.rabbitmq.listener", name = {"type"}, havingValue = "simple", matchIfMissing = true)
    @Bean(name = {"rabbitListenerContainerFactory"})
    SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @ConditionalOnMissingBean
    @Bean
    DirectRabbitListenerContainerFactoryConfigurer directRabbitListenerContainerFactoryConfigurer() {
        DirectRabbitListenerContainerFactoryConfigurer configurer = new DirectRabbitListenerContainerFactoryConfigurer();
        configurer.setMessageConverter(this.messageConverter.getIfUnique());
        configurer.setMessageRecoverer(this.messageRecoverer.getIfUnique());
        configurer.setRetryTemplateCustomizers((List) this.retryTemplateCustomizers.orderedStream().collect(Collectors.toList()));
        configurer.setRabbitProperties(this.properties);
        return configurer;
    }

    @ConditionalOnMissingBean(name = {"rabbitListenerContainerFactory"})
    @ConditionalOnProperty(prefix = "spring.rabbitmq.listener", name = {"type"}, havingValue = "direct")
    @Bean(name = {"rabbitListenerContainerFactory"})
    DirectRabbitListenerContainerFactory directRabbitListenerContainerFactory(DirectRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        DirectRabbitListenerContainerFactory factory = new DirectRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @ConditionalOnMissingBean(name = {"org.springframework.amqp.rabbit.config.internalRabbitListenerAnnotationProcessor"})
    @EnableRabbit
    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitAnnotationDrivenConfiguration$EnableRabbitConfiguration.class */
    static class EnableRabbitConfiguration {
        EnableRabbitConfiguration() {
        }
    }
}
