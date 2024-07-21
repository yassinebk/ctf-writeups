package org.springframework.boot.autoconfigure.data.elasticsearch;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ElasticsearchTemplate.class})
@AutoConfigureAfter({ElasticsearchRestClientAutoConfiguration.class, ReactiveElasticsearchRestClientAutoConfiguration.class})
@Import({ElasticsearchDataConfiguration.BaseConfiguration.class, ElasticsearchDataConfiguration.RestClientConfiguration.class, ElasticsearchDataConfiguration.ReactiveRestClientConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/elasticsearch/ElasticsearchDataAutoConfiguration.class */
public class ElasticsearchDataAutoConfiguration {
}
