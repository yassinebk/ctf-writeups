package org.springframework.boot.autoconfigure.data.couchbase;

import com.couchbase.client.java.Bucket;
import javax.validation.Validator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.couchbase.core.mapping.event.ValidatingCouchbaseEventListener;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
@EnableConfigurationProperties({CouchbaseDataProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Bucket.class, CouchbaseRepository.class})
@AutoConfigureAfter({CouchbaseAutoConfiguration.class, ValidationAutoConfiguration.class})
@Import({CouchbaseDataConfiguration.class, CouchbaseClientFactoryConfiguration.class, CouchbaseClientFactoryDependentConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/couchbase/CouchbaseDataAutoConfiguration.class */
public class CouchbaseDataAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({Validator.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/couchbase/CouchbaseDataAutoConfiguration$ValidationConfiguration.class */
    public static class ValidationConfiguration {
        @Bean
        @ConditionalOnSingleCandidate(Validator.class)
        public ValidatingCouchbaseEventListener validationEventListener(Validator validator) {
            return new ValidatingCouchbaseEventListener(validator);
        }
    }
}
