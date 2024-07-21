package org.springframework.boot.autoconfigure.data.r2dbc;

import java.lang.annotation.Annotation;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.r2dbc.repository.config.R2dbcRepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/r2dbc/R2dbcRepositoriesAutoConfigureRegistrar.class */
class R2dbcRepositoriesAutoConfigureRegistrar extends AbstractRepositoryConfigurationSourceSupport {
    R2dbcRepositoriesAutoConfigureRegistrar() {
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected Class<? extends Annotation> getAnnotation() {
        return EnableR2dbcRepositories.class;
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected Class<?> getConfiguration() {
        return EnableR2dbcRepositoriesConfiguration.class;
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new R2dbcRepositoryConfigurationExtension();
    }

    @EnableR2dbcRepositories
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/r2dbc/R2dbcRepositoriesAutoConfigureRegistrar$EnableR2dbcRepositoriesConfiguration.class */
    private static class EnableR2dbcRepositoriesConfiguration {
        private EnableR2dbcRepositoriesConfiguration() {
        }
    }
}
