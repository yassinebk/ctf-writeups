package org.springframework.boot.autoconfigure.web.reactive.function.client;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorConfiguration;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({WebClient.class})
@Import({ClientHttpConnectorConfiguration.ReactorNetty.class, ClientHttpConnectorConfiguration.JettyClient.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/reactive/function/client/ClientHttpConnectorAutoConfiguration.class */
public class ClientHttpConnectorAutoConfiguration {
    @ConditionalOnBean({ClientHttpConnector.class})
    @Bean
    @Lazy
    @Order(0)
    public WebClientCustomizer clientConnectorCustomizer(ClientHttpConnector clientHttpConnector) {
        return builder -> {
            builder.clientConnector(clientHttpConnector);
        };
    }
}
