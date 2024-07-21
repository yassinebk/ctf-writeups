package org.springframework.boot.autoconfigure.data.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.ConditionalOnRepositoryType;
import org.springframework.boot.autoconfigure.data.RepositoryType;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.support.CassandraRepositoryFactoryBean;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({CqlSession.class, CassandraRepository.class})
@ConditionalOnMissingBean({CassandraRepositoryFactoryBean.class})
@ConditionalOnRepositoryType(store = "cassandra", type = RepositoryType.IMPERATIVE)
@Import({CassandraRepositoriesRegistrar.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/cassandra/CassandraRepositoriesAutoConfiguration.class */
public class CassandraRepositoriesAutoConfiguration {
}
