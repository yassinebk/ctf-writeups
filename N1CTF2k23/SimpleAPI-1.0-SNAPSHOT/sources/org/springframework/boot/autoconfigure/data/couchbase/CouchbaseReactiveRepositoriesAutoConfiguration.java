package org.springframework.boot.autoconfigure.data.couchbase;

import com.couchbase.client.java.Cluster;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.ConditionalOnRepositoryType;
import org.springframework.boot.autoconfigure.data.RepositoryType;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;
import org.springframework.data.couchbase.repository.config.ReactiveRepositoryOperationsMapping;
import org.springframework.data.couchbase.repository.support.ReactiveCouchbaseRepositoryFactoryBean;
import reactor.core.publisher.Flux;
@AutoConfigureAfter({CouchbaseReactiveDataAutoConfiguration.class})
@ConditionalOnMissingBean({ReactiveCouchbaseRepositoryFactoryBean.class})
@Import({CouchbaseReactiveRepositoriesRegistrar.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Cluster.class, ReactiveCouchbaseRepository.class, Flux.class})
@ConditionalOnRepositoryType(store = "couchbase", type = RepositoryType.REACTIVE)
@ConditionalOnBean({ReactiveRepositoryOperationsMapping.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/couchbase/CouchbaseReactiveRepositoriesAutoConfiguration.class */
public class CouchbaseReactiveRepositoriesAutoConfiguration {
}
