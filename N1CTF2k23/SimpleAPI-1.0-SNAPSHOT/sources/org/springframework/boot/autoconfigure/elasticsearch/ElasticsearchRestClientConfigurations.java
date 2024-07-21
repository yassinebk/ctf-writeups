package org.springframework.boot.autoconfigure.elasticsearch;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/elasticsearch/ElasticsearchRestClientConfigurations.class */
class ElasticsearchRestClientConfigurations {
    ElasticsearchRestClientConfigurations() {
    }

    @ConditionalOnMissingBean({RestClientBuilder.class})
    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/elasticsearch/ElasticsearchRestClientConfigurations$RestClientBuilderConfiguration.class */
    static class RestClientBuilderConfiguration {
        RestClientBuilderConfiguration() {
        }

        @Bean
        RestClientBuilderCustomizer defaultRestClientBuilderCustomizer(ElasticsearchRestClientProperties properties) {
            return new DefaultRestClientBuilderCustomizer(properties);
        }

        @Bean
        RestClientBuilder elasticsearchRestClientBuilder(ElasticsearchRestClientProperties properties, ObjectProvider<RestClientBuilderCustomizer> builderCustomizers) {
            HttpHost[] hosts = (HttpHost[]) properties.getUris().stream().map(HttpHost::create).toArray(x$0 -> {
                return new HttpHost[x$0];
            });
            RestClientBuilder builder = RestClient.builder(hosts);
            builder.setHttpClientConfigCallback(httpClientBuilder -> {
                builderCustomizers.orderedStream().forEach(customizer -> {
                    customizer.customize(httpClientBuilder);
                });
                return httpClientBuilder;
            });
            builder.setRequestConfigCallback(requestConfigBuilder -> {
                builderCustomizers.orderedStream().forEach(customizer -> {
                    customizer.customize(requestConfigBuilder);
                });
                return requestConfigBuilder;
            });
            builderCustomizers.orderedStream().forEach(customizer -> {
                customizer.customize(builder);
            });
            return builder;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({RestHighLevelClient.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/elasticsearch/ElasticsearchRestClientConfigurations$RestHighLevelClientConfiguration.class */
    static class RestHighLevelClientConfiguration {
        RestHighLevelClientConfiguration() {
        }

        @ConditionalOnMissingBean
        @Bean
        RestHighLevelClient elasticsearchRestHighLevelClient(RestClientBuilder restClientBuilder) {
            return new RestHighLevelClient(restClientBuilder);
        }

        @ConditionalOnMissingBean
        @Bean
        RestClient elasticsearchRestClient(RestClientBuilder builder, ObjectProvider<RestHighLevelClient> restHighLevelClient) {
            RestHighLevelClient client = restHighLevelClient.getIfUnique();
            if (client != null) {
                return client.getLowLevelClient();
            }
            return builder.build();
        }
    }

    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/elasticsearch/ElasticsearchRestClientConfigurations$RestClientFallbackConfiguration.class */
    static class RestClientFallbackConfiguration {
        RestClientFallbackConfiguration() {
        }

        @ConditionalOnMissingBean
        @Bean
        RestClient elasticsearchRestClient(RestClientBuilder builder) {
            return builder.build();
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/elasticsearch/ElasticsearchRestClientConfigurations$DefaultRestClientBuilderCustomizer.class */
    static class DefaultRestClientBuilderCustomizer implements RestClientBuilderCustomizer {
        private static final PropertyMapper map = PropertyMapper.get();
        private final ElasticsearchRestClientProperties properties;

        DefaultRestClientBuilderCustomizer(ElasticsearchRestClientProperties properties) {
            this.properties = properties;
        }

        @Override // org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer
        public void customize(RestClientBuilder builder) {
        }

        @Override // org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer
        public void customize(HttpAsyncClientBuilder builder) {
            PropertyMapper propertyMapper = map;
            ElasticsearchRestClientProperties elasticsearchRestClientProperties = this.properties;
            elasticsearchRestClientProperties.getClass();
            propertyMapper.from(this::getUsername).whenHasText().to(username -> {
                BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
                basicCredentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(this.properties.getUsername(), this.properties.getPassword()));
                builder.setDefaultCredentialsProvider(basicCredentialsProvider);
            });
        }

        @Override // org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer
        public void customize(RequestConfig.Builder builder) {
            PropertyMapper propertyMapper = map;
            ElasticsearchRestClientProperties elasticsearchRestClientProperties = this.properties;
            elasticsearchRestClientProperties.getClass();
            PropertyMapper.Source<Integer> asInt = propertyMapper.from(this::getConnectionTimeout).whenNonNull().asInt((v0) -> {
                return v0.toMillis();
            });
            builder.getClass();
            asInt.to((v1) -> {
                r1.setConnectTimeout(v1);
            });
            PropertyMapper propertyMapper2 = map;
            ElasticsearchRestClientProperties elasticsearchRestClientProperties2 = this.properties;
            elasticsearchRestClientProperties2.getClass();
            PropertyMapper.Source<Integer> asInt2 = propertyMapper2.from(this::getReadTimeout).whenNonNull().asInt((v0) -> {
                return v0.toMillis();
            });
            builder.getClass();
            asInt2.to((v1) -> {
                r1.setSocketTimeout(v1);
            });
        }
    }
}
