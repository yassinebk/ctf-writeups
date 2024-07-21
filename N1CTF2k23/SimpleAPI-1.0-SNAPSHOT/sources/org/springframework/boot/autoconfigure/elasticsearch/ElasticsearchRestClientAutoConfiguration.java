package org.springframework.boot.autoconfigure.elasticsearch;

import org.elasticsearch.client.RestClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientConfigurations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
@EnableConfigurationProperties({ElasticsearchRestClientProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RestClient.class})
@Import({ElasticsearchRestClientConfigurations.RestClientBuilderConfiguration.class, ElasticsearchRestClientConfigurations.RestHighLevelClientConfiguration.class, ElasticsearchRestClientConfigurations.RestClientFallbackConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/elasticsearch/ElasticsearchRestClientAutoConfiguration.class */
public class ElasticsearchRestClientAutoConfiguration {
}
