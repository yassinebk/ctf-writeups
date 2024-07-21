package org.springframework.boot.autoconfigure.data.mongo;

import com.mongodb.client.MongoClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.ConditionalOnRepositoryType;
import org.springframework.boot.autoconfigure.data.RepositoryType;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.MongoRepositoryConfigurationExtension;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({MongoClient.class, MongoRepository.class})
@AutoConfigureAfter({MongoDataAutoConfiguration.class})
@ConditionalOnMissingBean({MongoRepositoryFactoryBean.class, MongoRepositoryConfigurationExtension.class})
@ConditionalOnRepositoryType(store = "mongodb", type = RepositoryType.IMPERATIVE)
@Import({MongoRepositoriesRegistrar.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/mongo/MongoRepositoriesAutoConfiguration.class */
public class MongoRepositoriesAutoConfiguration {
}
