package org.springframework.boot.autoconfigure.rsocket;

import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.server.TcpServerTransport;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import reactor.netty.http.server.HttpServer;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RSocketRequester.class, RSocketFactory.class, HttpServer.class, TcpServerTransport.class})
@AutoConfigureAfter({RSocketStrategiesAutoConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/rsocket/RSocketRequesterAutoConfiguration.class */
public class RSocketRequesterAutoConfiguration {
    @ConditionalOnMissingBean
    @Scope("prototype")
    @Bean
    public RSocketRequester.Builder rSocketRequesterBuilder(RSocketStrategies strategies) {
        return RSocketRequester.builder().rsocketStrategies(strategies);
    }
}
