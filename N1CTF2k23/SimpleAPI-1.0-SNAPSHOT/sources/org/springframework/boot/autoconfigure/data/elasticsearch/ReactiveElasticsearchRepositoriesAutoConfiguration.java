package org.springframework.boot.autoconfigure.data.elasticsearch;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.support.ReactiveElasticsearchRepositoryFactoryBean;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ReactiveElasticsearchClient.class, ReactiveElasticsearchRepository.class})
@ConditionalOnMissingBean({ReactiveElasticsearchRepositoryFactoryBean.class})
@ConditionalOnProperty(prefix = "spring.data.elasticsearch.repositories", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Import({ReactiveElasticsearchRepositoriesRegistrar.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/elasticsearch/ReactiveElasticsearchRepositoriesAutoConfiguration.class */
public class ReactiveElasticsearchRepositoriesAutoConfiguration {
}
