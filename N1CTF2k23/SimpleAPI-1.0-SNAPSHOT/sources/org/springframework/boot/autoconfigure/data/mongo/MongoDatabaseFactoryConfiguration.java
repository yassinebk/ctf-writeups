package org.springframework.boot.autoconfigure.data.mongo;

import com.mongodb.client.MongoClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoDatabaseFactorySupport;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
@ConditionalOnMissingBean({MongoDatabaseFactory.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnSingleCandidate(MongoClient.class)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/mongo/MongoDatabaseFactoryConfiguration.class */
class MongoDatabaseFactoryConfiguration {
    MongoDatabaseFactoryConfiguration() {
    }

    @Bean
    MongoDatabaseFactorySupport<?> mongoDatabaseFactory(MongoClient mongoClient, MongoProperties properties) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, properties.getMongoClientDatabase());
    }
}
