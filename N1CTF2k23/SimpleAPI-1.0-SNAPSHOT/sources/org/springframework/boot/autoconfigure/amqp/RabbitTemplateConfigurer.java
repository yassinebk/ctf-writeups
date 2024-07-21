package org.springframework.boot.autoconfigure.amqp;

import java.util.List;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.RabbitRetryTemplateCustomizer;
import org.springframework.boot.context.properties.PropertyMapper;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/amqp/RabbitTemplateConfigurer.class */
public class RabbitTemplateConfigurer {
    private MessageConverter messageConverter;
    private List<RabbitRetryTemplateCustomizer> retryTemplateCustomizers;
    private RabbitProperties rabbitProperties;

    /* JADX INFO: Access modifiers changed from: protected */
    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setRetryTemplateCustomizers(List<RabbitRetryTemplateCustomizer> retryTemplateCustomizers) {
        this.retryTemplateCustomizers = retryTemplateCustomizers;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setRabbitProperties(RabbitProperties rabbitProperties) {
        this.rabbitProperties = rabbitProperties;
    }

    protected final RabbitProperties getRabbitProperties() {
        return this.rabbitProperties;
    }

    public void configure(RabbitTemplate template, ConnectionFactory connectionFactory) {
        PropertyMapper map = PropertyMapper.get();
        template.setConnectionFactory(connectionFactory);
        if (this.messageConverter != null) {
            template.setMessageConverter(this.messageConverter);
        }
        template.setMandatory(determineMandatoryFlag());
        RabbitProperties.Template templateProperties = this.rabbitProperties.getTemplate();
        if (templateProperties.getRetry().isEnabled()) {
            template.setRetryTemplate(new RetryTemplateFactory(this.retryTemplateCustomizers).createRetryTemplate(templateProperties.getRetry(), RabbitRetryTemplateCustomizer.Target.SENDER));
        }
        templateProperties.getClass();
        PropertyMapper.Source as = map.from(this::getReceiveTimeout).whenNonNull().as((v0) -> {
            return v0.toMillis();
        });
        template.getClass();
        as.to((v1) -> {
            r1.setReceiveTimeout(v1);
        });
        templateProperties.getClass();
        PropertyMapper.Source as2 = map.from(this::getReplyTimeout).whenNonNull().as((v0) -> {
            return v0.toMillis();
        });
        template.getClass();
        as2.to((v1) -> {
            r1.setReplyTimeout(v1);
        });
        templateProperties.getClass();
        PropertyMapper.Source from = map.from(this::getExchange);
        template.getClass();
        from.to(this::setExchange);
        templateProperties.getClass();
        PropertyMapper.Source from2 = map.from(this::getRoutingKey);
        template.getClass();
        from2.to(this::setRoutingKey);
        templateProperties.getClass();
        PropertyMapper.Source whenNonNull = map.from(this::getDefaultReceiveQueue).whenNonNull();
        template.getClass();
        whenNonNull.to(this::setDefaultReceiveQueue);
    }

    private boolean determineMandatoryFlag() {
        Boolean mandatory = this.rabbitProperties.getTemplate().getMandatory();
        return mandatory != null ? mandatory.booleanValue() : this.rabbitProperties.isPublisherReturns();
    }
}
