package org.springframework.boot.autoconfigure.kafka;

import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/kafka/DefaultKafkaConsumerFactoryCustomizer.class */
public interface DefaultKafkaConsumerFactoryCustomizer {
    void customize(DefaultKafkaConsumerFactory<?, ?> consumerFactory);
}
