package org.springframework.boot.autoconfigure.mongo;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import java.util.List;
import org.springframework.core.env.Environment;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/ReactiveMongoClientFactory.class */
public class ReactiveMongoClientFactory extends MongoClientFactorySupport<MongoClient> {
    public ReactiveMongoClientFactory(MongoProperties properties, Environment environment, List<MongoClientSettingsBuilderCustomizer> builderCustomizers) {
        super(properties, environment, builderCustomizers, MongoClients::create);
    }
}
