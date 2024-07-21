package org.springframework.boot.autoconfigure.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
@EnableConfigurationProperties({MongoProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({MongoClient.class})
@ConditionalOnMissingBean(type = {"org.springframework.data.mongodb.MongoDatabaseFactory"})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/MongoAutoConfiguration.class */
public class MongoAutoConfiguration {
    @ConditionalOnMissingBean({MongoClient.class})
    @Bean
    public MongoClient mongo(MongoProperties properties, Environment environment, ObjectProvider<MongoClientSettingsBuilderCustomizer> builderCustomizers, ObjectProvider<MongoClientSettings> settings) {
        return new MongoClientFactory(properties, environment, (List) builderCustomizers.orderedStream().collect(Collectors.toList())).createMongoClient(settings.getIfAvailable());
    }
}
