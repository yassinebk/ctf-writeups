package org.springframework.boot.autoconfigure.data.elasticsearch;

import java.lang.annotation.Annotation;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;
import org.springframework.data.elasticsearch.repository.config.ReactiveElasticsearchRepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/elasticsearch/ReactiveElasticsearchRepositoriesRegistrar.class */
class ReactiveElasticsearchRepositoriesRegistrar extends AbstractRepositoryConfigurationSourceSupport {
    ReactiveElasticsearchRepositoriesRegistrar() {
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected Class<? extends Annotation> getAnnotation() {
        return EnableReactiveElasticsearchRepositories.class;
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected Class<?> getConfiguration() {
        return EnableElasticsearchRepositoriesConfiguration.class;
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new ReactiveElasticsearchRepositoryConfigurationExtension();
    }

    @EnableReactiveElasticsearchRepositories
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/elasticsearch/ReactiveElasticsearchRepositoriesRegistrar$EnableElasticsearchRepositoriesConfiguration.class */
    private static class EnableElasticsearchRepositoriesConfiguration {
        private EnableElasticsearchRepositoriesConfiguration() {
        }
    }
}
