package org.springframework.boot.autoconfigure.data.redis;

import java.lang.annotation.Annotation;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.repository.configuration.RedisRepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/redis/RedisRepositoriesRegistrar.class */
class RedisRepositoriesRegistrar extends AbstractRepositoryConfigurationSourceSupport {
    RedisRepositoriesRegistrar() {
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected Class<? extends Annotation> getAnnotation() {
        return EnableRedisRepositories.class;
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected Class<?> getConfiguration() {
        return EnableRedisRepositoriesConfiguration.class;
    }

    @Override // org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new RedisRepositoryConfigurationExtension();
    }

    @EnableRedisRepositories
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/redis/RedisRepositoriesRegistrar$EnableRedisRepositoriesConfiguration.class */
    private static class EnableRedisRepositoriesConfiguration {
        private EnableRedisRepositoriesConfiguration() {
        }
    }
}
