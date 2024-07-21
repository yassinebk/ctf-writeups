package org.springframework.boot.autoconfigure.rsocket;

import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RSocketRequester.class, RSocketFactory.class, TcpServerTransport.class})
@AutoConfigureAfter({RSocketStrategiesAutoConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/rsocket/RSocketMessagingAutoConfiguration.class */
public class RSocketMessagingAutoConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public RSocketMessageHandler messageHandler(RSocketStrategies rSocketStrategies, ObjectProvider<RSocketMessageHandlerCustomizer> customizers) {
        RSocketMessageHandler messageHandler = new RSocketMessageHandler();
        messageHandler.setRSocketStrategies(rSocketStrategies);
        customizers.orderedStream().forEach(customizer -> {
            customizer.customize(messageHandler);
        });
        return messageHandler;
    }
}
