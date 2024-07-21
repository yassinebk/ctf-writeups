package org.springframework.boot.autoconfigure.data.cassandra;

import java.lang.annotation.Annotation;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.cassandra.repository.config.CassandraRepositoryConfigurationExtension;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/cassandra/CassandraRepositoriesRegistrar.class */
class CassandraRepositoriesRegistrar extends AbstractRepositoryConfigurationSourceSupport {
    CassandraRepositoriesRegistrar() {
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected Class<? extends Annotation> getAnnotation() {
        return EnableCassandraRepositories.class;
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected Class<?> getConfiguration() {
        return EnableCassandraRepositoriesConfiguration.class;
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new CassandraRepositoryConfigurationExtension();
    }

    @EnableCassandraRepositories
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/cassandra/CassandraRepositoriesRegistrar$EnableCassandraRepositoriesConfiguration.class */
    private static class EnableCassandraRepositoriesConfiguration {
        private EnableCassandraRepositoriesConfiguration() {
        }
    }
}
