package org.springframework.boot.autoconfigure.data.elasticsearch;

import java.lang.annotation.Annotation;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.elasticsearch.repository.config.ElasticsearchRepositoryConfigExtension;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/elasticsearch/ElasticsearchRepositoriesRegistrar.class */
class ElasticsearchRepositoriesRegistrar extends AbstractRepositoryConfigurationSourceSupport {
    ElasticsearchRepositoriesRegistrar() {
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected Class<? extends Annotation> getAnnotation() {
        return EnableElasticsearchRepositories.class;
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected Class<?> getConfiguration() {
        return EnableElasticsearchRepositoriesConfiguration.class;
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new ElasticsearchRepositoryConfigExtension();
    }

    @EnableElasticsearchRepositories
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/elasticsearch/ElasticsearchRepositoriesRegistrar$EnableElasticsearchRepositoriesConfiguration.class */
    private static class EnableElasticsearchRepositoriesConfiguration {
        private EnableElasticsearchRepositoriesConfiguration() {
        }
    }
}
