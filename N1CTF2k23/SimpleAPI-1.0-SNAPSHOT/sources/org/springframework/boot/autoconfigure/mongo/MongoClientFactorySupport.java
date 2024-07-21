package org.springframework.boot.autoconfigure.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.MongoDriverInformation;
import com.mongodb.ServerAddress;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/MongoClientFactorySupport.class */
public abstract class MongoClientFactorySupport<T> {
    private final MongoProperties properties;
    private final Environment environment;
    private final List<MongoClientSettingsBuilderCustomizer> builderCustomizers;
    private final BiFunction<MongoClientSettings, MongoDriverInformation, T> clientCreator;

    /* JADX INFO: Access modifiers changed from: protected */
    public MongoClientFactorySupport(MongoProperties properties, Environment environment, List<MongoClientSettingsBuilderCustomizer> builderCustomizers, BiFunction<MongoClientSettings, MongoDriverInformation, T> clientCreator) {
        this.properties = properties;
        this.environment = environment;
        this.builderCustomizers = builderCustomizers != null ? builderCustomizers : Collections.emptyList();
        this.clientCreator = clientCreator;
    }

    public T createMongoClient(MongoClientSettings settings) {
        MongoClientSettings targetSettings = computeClientSettings(settings);
        return this.clientCreator.apply(targetSettings, driverInformation());
    }

    private MongoClientSettings computeClientSettings(MongoClientSettings settings) {
        MongoClientSettings.Builder settingsBuilder = settings != null ? MongoClientSettings.builder(settings) : MongoClientSettings.builder();
        validateConfiguration();
        applyUuidRepresentation(settingsBuilder);
        applyHostAndPort(settingsBuilder);
        applyCredentials(settingsBuilder);
        applyReplicaSet(settingsBuilder);
        customize(settingsBuilder);
        return settingsBuilder.build();
    }

    private void validateConfiguration() {
        if (hasCustomAddress() || hasCustomCredentials() || hasReplicaSet()) {
            Assert.state(this.properties.getUri() == null, "Invalid mongo configuration, either uri or host/port/credentials/replicaSet must be specified");
        }
    }

    private void applyUuidRepresentation(MongoClientSettings.Builder settingsBuilder) {
        settingsBuilder.uuidRepresentation(this.properties.getUuidRepresentation());
    }

    private void applyHostAndPort(MongoClientSettings.Builder settings) {
        if (isEmbedded()) {
            settings.applyConnectionString(new ConnectionString("mongodb://localhost:" + getEmbeddedPort()));
        } else if (hasCustomAddress()) {
            String host = (String) getOrDefault(this.properties.getHost(), "localhost");
            int port = ((Integer) getOrDefault(this.properties.getPort(), Integer.valueOf((int) MongoProperties.DEFAULT_PORT))).intValue();
            ServerAddress serverAddress = new ServerAddress(host, port);
            settings.applyToClusterSettings(cluster -> {
                cluster.hosts(Collections.singletonList(serverAddress));
            });
        } else {
            settings.applyConnectionString(new ConnectionString(this.properties.determineUri()));
        }
    }

    private void applyCredentials(MongoClientSettings.Builder builder) {
        if (hasCustomCredentials()) {
            String database = this.properties.getAuthenticationDatabase() != null ? this.properties.getAuthenticationDatabase() : this.properties.getMongoClientDatabase();
            builder.credential(MongoCredential.createCredential(this.properties.getUsername(), database, this.properties.getPassword()));
        }
    }

    private void applyReplicaSet(MongoClientSettings.Builder builder) {
        if (hasReplicaSet()) {
            builder.applyToClusterSettings(cluster -> {
                cluster.requiredReplicaSetName(this.properties.getReplicaSetName());
            });
        }
    }

    private void customize(MongoClientSettings.Builder builder) {
        for (MongoClientSettingsBuilderCustomizer customizer : this.builderCustomizers) {
            customizer.customize(builder);
        }
    }

    private <V> V getOrDefault(V value, V defaultValue) {
        return value != null ? value : defaultValue;
    }

    private Integer getEmbeddedPort() {
        String localPort;
        if (this.environment != null && (localPort = this.environment.getProperty("local.mongo.port")) != null) {
            return Integer.valueOf(localPort);
        }
        return null;
    }

    private boolean isEmbedded() {
        return getEmbeddedPort() != null;
    }

    private boolean hasCustomCredentials() {
        return (this.properties.getUsername() == null || this.properties.getPassword() == null) ? false : true;
    }

    private boolean hasReplicaSet() {
        return this.properties.getReplicaSetName() != null;
    }

    private boolean hasCustomAddress() {
        return (this.properties.getHost() == null && this.properties.getPort() == null) ? false : true;
    }

    private MongoDriverInformation driverInformation() {
        return MongoDriverInformation.builder(MongoDriverInformation.builder().build()).driverName("spring-boot").build();
    }
}
