package org.springframework.boot.autoconfigure.data.couchbase;

import com.couchbase.client.java.Cluster;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;
import reactor.core.publisher.Flux;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Cluster.class, ReactiveCouchbaseRepository.class, Flux.class})
@AutoConfigureAfter({CouchbaseDataAutoConfiguration.class})
@Import({CouchbaseReactiveDataConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/couchbase/CouchbaseReactiveDataAutoConfiguration.class */
public class CouchbaseReactiveDataAutoConfiguration {
}
