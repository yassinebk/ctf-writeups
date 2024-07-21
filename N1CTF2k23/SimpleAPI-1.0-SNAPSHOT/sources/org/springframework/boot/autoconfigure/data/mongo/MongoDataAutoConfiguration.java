package org.springframework.boot.autoconfigure.data.mongo;

import com.mongodb.client.MongoClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
@EnableConfigurationProperties({MongoProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({MongoClient.class, MongoTemplate.class})
@AutoConfigureAfter({MongoAutoConfiguration.class})
@Import({MongoDataConfiguration.class, MongoDatabaseFactoryConfiguration.class, MongoDatabaseFactoryDependentConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/mongo/MongoDataAutoConfiguration.class */
public class MongoDataAutoConfiguration {
}
