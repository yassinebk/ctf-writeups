package org.springframework.boot.autoconfigure.data.r2dbc;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactoryBean;
@AutoConfigureAfter({R2dbcDataAutoConfiguration.class})
@ConditionalOnMissingBean({R2dbcRepositoryFactoryBean.class})
@ConditionalOnProperty(prefix = "spring.data.r2dbc.repositories", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Import({R2dbcRepositoriesAutoConfigureRegistrar.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ConnectionFactory.class, R2dbcRepository.class})
@ConditionalOnBean({DatabaseClient.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/r2dbc/R2dbcRepositoriesAutoConfiguration.class */
public class R2dbcRepositoriesAutoConfiguration {
}
