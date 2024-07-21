package org.springframework.boot.autoconfigure.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.List;
import org.springframework.core.env.Environment;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/MongoClientFactory.class */
public class MongoClientFactory extends MongoClientFactorySupport<MongoClient> {
    @Deprecated
    public MongoClientFactory(MongoProperties properties, Environment environment) {
        this(properties, environment, null);
    }

    public MongoClientFactory(MongoProperties properties, Environment environment, List<MongoClientSettingsBuilderCustomizer> builderCustomizers) {
        super(properties, environment, builderCustomizers, MongoClients::create);
    }
}
