package org.springframework.boot.autoconfigure.data.elasticsearch;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
@EnableConfigurationProperties({ReactiveElasticsearchRestClientProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ReactiveRestClients.class, WebClient.class, HttpClient.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/elasticsearch/ReactiveElasticsearchRestClientAutoConfiguration.class */
public class ReactiveElasticsearchRestClientAutoConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public ClientConfiguration clientConfiguration(ReactiveElasticsearchRestClientProperties properties) {
        ClientConfiguration.MaybeSecureClientConfigurationBuilder builder = ClientConfiguration.builder().connectedTo((String[]) properties.getEndpoints().toArray(new String[0]));
        if (properties.isUseSsl()) {
            builder.usingSsl();
        }
        configureTimeouts(builder, properties);
        configureExchangeStrategies(builder, properties);
        return builder.build();
    }

    private void configureTimeouts(ClientConfiguration.TerminalClientConfigurationBuilder builder, ReactiveElasticsearchRestClientProperties properties) {
        PropertyMapper map = PropertyMapper.get();
        PropertyMapper.Source whenNonNull = map.from((PropertyMapper) properties.getConnectionTimeout()).whenNonNull();
        builder.getClass();
        whenNonNull.to(this::withConnectTimeout);
        PropertyMapper.Source whenNonNull2 = map.from((PropertyMapper) properties.getSocketTimeout()).whenNonNull();
        builder.getClass();
        whenNonNull2.to(this::withSocketTimeout);
        map.from((PropertyMapper) properties.getUsername()).whenHasText().to(username -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setBasicAuth(username, properties.getPassword());
            builder.withDefaultHeaders(headers);
        });
    }

    private void configureExchangeStrategies(ClientConfiguration.TerminalClientConfigurationBuilder builder, ReactiveElasticsearchRestClientProperties properties) {
        PropertyMapper map = PropertyMapper.get();
        builder.withWebClientConfigurer(webClient -> {
            ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder().codecs(configurer -> {
                map.from((PropertyMapper) properties.getMaxInMemorySize()).whenNonNull().asInt((v0) -> {
                    return v0.toBytes();
                }).to(maxInMemorySize -> {
                    configurer.defaultCodecs().maxInMemorySize(maxInMemorySize.intValue());
                });
            }).build();
            return webClient.mutate().exchangeStrategies(exchangeStrategies).build();
        });
    }

    @ConditionalOnMissingBean
    @Bean
    public ReactiveElasticsearchClient reactiveElasticsearchClient(ClientConfiguration clientConfiguration) {
        return ReactiveRestClients.create(clientConfiguration);
    }
}
