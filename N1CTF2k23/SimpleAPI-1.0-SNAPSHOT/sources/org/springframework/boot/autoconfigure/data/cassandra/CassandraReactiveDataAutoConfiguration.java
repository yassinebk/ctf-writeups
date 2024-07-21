package org.springframework.boot.autoconfigure.data.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.ReactiveSession;
import org.springframework.data.cassandra.ReactiveSessionFactory;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;
import org.springframework.data.cassandra.core.cql.session.DefaultBridgedReactiveSession;
import org.springframework.data.cassandra.core.cql.session.DefaultReactiveSessionFactory;
import reactor.core.publisher.Flux;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({CqlSession.class, ReactiveCassandraTemplate.class, Flux.class})
@AutoConfigureAfter({CassandraDataAutoConfiguration.class})
@ConditionalOnBean({CqlSession.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/cassandra/CassandraReactiveDataAutoConfiguration.class */
public class CassandraReactiveDataAutoConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public ReactiveSession reactiveCassandraSession(CqlSession session) {
        return new DefaultBridgedReactiveSession(session);
    }

    @Bean
    public ReactiveSessionFactory reactiveCassandraSessionFactory(ReactiveSession reactiveCassandraSession) {
        return new DefaultReactiveSessionFactory(reactiveCassandraSession);
    }

    @ConditionalOnMissingBean({ReactiveCassandraOperations.class})
    @Bean
    public ReactiveCassandraTemplate reactiveCassandraTemplate(ReactiveSession reactiveCassandraSession, CassandraConverter converter) {
        return new ReactiveCassandraTemplate(reactiveCassandraSession, converter);
    }
}
