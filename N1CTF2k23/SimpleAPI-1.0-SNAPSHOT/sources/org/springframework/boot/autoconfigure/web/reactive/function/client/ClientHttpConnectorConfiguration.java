package org.springframework.boot.autoconfigure.web.reactive.function.client;

import org.eclipse.jetty.reactive.client.ReactiveRequest;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.http.client.reactive.JettyResourceFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import reactor.netty.http.client.HttpClient;
@Configuration(proxyBeanMethods = false)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/reactive/function/client/ClientHttpConnectorConfiguration.class */
class ClientHttpConnectorConfiguration {
    ClientHttpConnectorConfiguration() {
    }

    @ConditionalOnMissingBean({ClientHttpConnector.class})
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({HttpClient.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/reactive/function/client/ClientHttpConnectorConfiguration$ReactorNetty.class */
    public static class ReactorNetty {
        @ConditionalOnMissingBean
        @Bean
        public ReactorResourceFactory reactorClientResourceFactory() {
            return new ReactorResourceFactory();
        }

        @Bean
        @Lazy
        public ReactorClientHttpConnector reactorClientHttpConnector(ReactorResourceFactory reactorResourceFactory, ObjectProvider<ReactorNettyHttpClientMapper> mapperProvider) {
            ReactorNettyHttpClientMapper mapper = mapperProvider.orderedStream().reduce(before, after -> {
                return client -> {
                    return after.configure(before.configure(client));
                };
            }).orElse(client -> {
                return client;
            });
            mapper.getClass();
            return new ReactorClientHttpConnector(reactorResourceFactory, this::configure);
        }
    }

    @ConditionalOnMissingBean({ClientHttpConnector.class})
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({ReactiveRequest.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/reactive/function/client/ClientHttpConnectorConfiguration$JettyClient.class */
    public static class JettyClient {
        @ConditionalOnMissingBean
        @Bean
        public JettyResourceFactory jettyClientResourceFactory() {
            return new JettyResourceFactory();
        }

        @Bean
        @Lazy
        public JettyClientHttpConnector jettyClientHttpConnector(JettyResourceFactory jettyResourceFactory) {
            org.eclipse.jetty.client.HttpClient httpClient = new org.eclipse.jetty.client.HttpClient(new SslContextFactory.Client());
            return new JettyClientHttpConnector(httpClient, jettyResourceFactory);
        }
    }
}
