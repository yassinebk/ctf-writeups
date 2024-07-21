package org.springframework.boot.autoconfigure.data.elasticsearch;

import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;
import org.springframework.web.reactive.function.client.WebClient;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/elasticsearch/ElasticsearchDataConfiguration.class */
abstract class ElasticsearchDataConfiguration {
    ElasticsearchDataConfiguration() {
    }

    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/elasticsearch/ElasticsearchDataConfiguration$BaseConfiguration.class */
    static class BaseConfiguration {
        BaseConfiguration() {
        }

        @ConditionalOnMissingBean
        @Bean
        ElasticsearchConverter elasticsearchConverter(SimpleElasticsearchMappingContext mappingContext) {
            return new MappingElasticsearchConverter(mappingContext);
        }

        @ConditionalOnMissingBean
        @Bean
        SimpleElasticsearchMappingContext mappingContext() {
            return new SimpleElasticsearchMappingContext();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({RestHighLevelClient.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/elasticsearch/ElasticsearchDataConfiguration$RestClientConfiguration.class */
    static class RestClientConfiguration {
        RestClientConfiguration() {
        }

        @ConditionalOnMissingBean(value = {ElasticsearchOperations.class}, name = {"elasticsearchTemplate"})
        @ConditionalOnBean({RestHighLevelClient.class})
        @Bean
        ElasticsearchRestTemplate elasticsearchTemplate(RestHighLevelClient client, ElasticsearchConverter converter) {
            return new ElasticsearchRestTemplate(client, converter);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({WebClient.class, ReactiveElasticsearchOperations.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/elasticsearch/ElasticsearchDataConfiguration$ReactiveRestClientConfiguration.class */
    static class ReactiveRestClientConfiguration {
        ReactiveRestClientConfiguration() {
        }

        @ConditionalOnMissingBean(value = {ReactiveElasticsearchOperations.class}, name = {"reactiveElasticsearchTemplate"})
        @ConditionalOnBean({ReactiveElasticsearchClient.class})
        @Bean
        ReactiveElasticsearchTemplate reactiveElasticsearchTemplate(ReactiveElasticsearchClient client, ElasticsearchConverter converter) {
            ReactiveElasticsearchTemplate template = new ReactiveElasticsearchTemplate(client, converter);
            template.setIndicesOptions(IndicesOptions.strictExpandOpenAndForbidClosed());
            template.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            return template;
        }
    }
}
