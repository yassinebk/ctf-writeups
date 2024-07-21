package org.springframework.boot.autoconfigure.mongo.embedded;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.DownloadConfigBuilder;
import de.flapdoodle.embed.mongo.config.ExtractedArtifactStoreBuilder;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.config.RuntimeConfigBuilder;
import de.flapdoodle.embed.mongo.config.Storage;
import de.flapdoodle.embed.mongo.distribution.Feature;
import de.flapdoodle.embed.mongo.distribution.IFeatureAwareVersion;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.distribution.Versions;
import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.config.io.ProcessOutput;
import de.flapdoodle.embed.process.config.store.IDownloadConfig;
import de.flapdoodle.embed.process.distribution.GenericVersion;
import de.flapdoodle.embed.process.io.Processors;
import de.flapdoodle.embed.process.io.Slf4jLevel;
import de.flapdoodle.embed.process.io.progress.Slf4jProgressListener;
import de.flapdoodle.embed.process.runtime.Network;
import de.flapdoodle.embed.process.store.ArtifactStoreBuilder;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.catalina.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.mongo.MongoClientDependsOnBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.data.mongo.ReactiveStreamsMongoClientDependsOnBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.ReactiveMongoClientFactoryBean;
@EnableConfigurationProperties({MongoProperties.class, EmbeddedMongoProperties.class})
@AutoConfigureBefore({MongoAutoConfiguration.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({MongoClientSettings.class, MongodStarter.class})
@Import({EmbeddedMongoClientDependsOnBeanFactoryPostProcessor.class, EmbeddedReactiveStreamsMongoClientDependsOnBeanFactoryPostProcessor.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/embedded/EmbeddedMongoAutoConfiguration.class */
public class EmbeddedMongoAutoConfiguration {
    private static final byte[] IP4_LOOPBACK_ADDRESS = {Byte.MAX_VALUE, 0, 0, 1};
    private static final byte[] IP6_LOOPBACK_ADDRESS = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
    private final MongoProperties properties;

    public EmbeddedMongoAutoConfiguration(MongoProperties properties, EmbeddedMongoProperties embeddedProperties) {
        this.properties = properties;
    }

    @ConditionalOnMissingBean
    @Bean(initMethod = Lifecycle.START_EVENT, destroyMethod = Lifecycle.STOP_EVENT)
    public MongodExecutable embeddedMongoServer(IMongodConfig mongodConfig, IRuntimeConfig runtimeConfig, ApplicationContext context) throws IOException {
        Integer configuredPort = this.properties.getPort();
        if (configuredPort == null || configuredPort.intValue() == 0) {
            setEmbeddedPort(context, mongodConfig.net().getPort());
        }
        MongodStarter mongodStarter = getMongodStarter(runtimeConfig);
        return mongodStarter.prepare(mongodConfig);
    }

    private MongodStarter getMongodStarter(IRuntimeConfig runtimeConfig) {
        if (runtimeConfig == null) {
            return MongodStarter.getDefaultInstance();
        }
        return MongodStarter.getInstance(runtimeConfig);
    }

    @ConditionalOnMissingBean
    @Bean
    public IMongodConfig embeddedMongoConfiguration(EmbeddedMongoProperties embeddedProperties) throws IOException {
        MongodConfigBuilder builder = new MongodConfigBuilder().version(determineVersion(embeddedProperties));
        EmbeddedMongoProperties.Storage storage = embeddedProperties.getStorage();
        if (storage != null) {
            String databaseDir = storage.getDatabaseDir();
            String replSetName = storage.getReplSetName();
            int oplogSize = storage.getOplogSize() != null ? (int) storage.getOplogSize().toMegabytes() : 0;
            builder.replication(new Storage(databaseDir, replSetName, oplogSize));
        }
        Integer configuredPort = this.properties.getPort();
        if (configuredPort != null && configuredPort.intValue() > 0) {
            builder.net(new Net(getHost().getHostAddress(), configuredPort.intValue(), Network.localhostIsIPv6()));
        } else {
            builder.net(new Net(getHost().getHostAddress(), Network.getFreeServerPort(getHost()), Network.localhostIsIPv6()));
        }
        return builder.build();
    }

    private IFeatureAwareVersion determineVersion(EmbeddedMongoProperties embeddedProperties) {
        Version[] values;
        if (embeddedProperties.getFeatures() == null) {
            for (Version version : Version.values()) {
                if (version.asInDownloadPath().equals(embeddedProperties.getVersion())) {
                    return version;
                }
            }
            return Versions.withFeatures(new GenericVersion(embeddedProperties.getVersion()), new Feature[0]);
        }
        return Versions.withFeatures(new GenericVersion(embeddedProperties.getVersion()), (Feature[]) embeddedProperties.getFeatures().toArray(new Feature[0]));
    }

    private InetAddress getHost() throws UnknownHostException {
        if (this.properties.getHost() == null) {
            return InetAddress.getByAddress(Network.localhostIsIPv6() ? IP6_LOOPBACK_ADDRESS : IP4_LOOPBACK_ADDRESS);
        }
        return InetAddress.getByName(this.properties.getHost());
    }

    private void setEmbeddedPort(ApplicationContext context, int port) {
        setPortProperty(context, port);
    }

    private void setPortProperty(ApplicationContext currentContext, int port) {
        if (currentContext instanceof ConfigurableApplicationContext) {
            MutablePropertySources sources = ((ConfigurableApplicationContext) currentContext).getEnvironment().getPropertySources();
            getMongoPorts(sources).put("local.mongo.port", Integer.valueOf(port));
        }
        if (currentContext.getParent() != null) {
            setPortProperty(currentContext.getParent(), port);
        }
    }

    private Map<String, Object> getMongoPorts(MutablePropertySources sources) {
        PropertySource<?> propertySource = sources.get("mongo.ports");
        if (propertySource == null) {
            propertySource = new MapPropertySource("mongo.ports", new HashMap());
            sources.addFirst(propertySource);
        }
        return (Map) propertySource.getSource();
    }

    @ConditionalOnMissingBean({IRuntimeConfig.class})
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({Logger.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/embedded/EmbeddedMongoAutoConfiguration$RuntimeConfigConfiguration.class */
    static class RuntimeConfigConfiguration {
        RuntimeConfigConfiguration() {
        }

        @Bean
        IRuntimeConfig embeddedMongoRuntimeConfig(ObjectProvider<DownloadConfigBuilderCustomizer> downloadConfigBuilderCustomizers) {
            Logger logger = LoggerFactory.getLogger(getClass().getPackage().getName() + ".EmbeddedMongo");
            ProcessOutput processOutput = new ProcessOutput(Processors.logTo(logger, Slf4jLevel.INFO), Processors.logTo(logger, Slf4jLevel.ERROR), Processors.named("[console>]", Processors.logTo(logger, Slf4jLevel.DEBUG)));
            return new RuntimeConfigBuilder().defaultsWithLogger(Command.MongoD, logger).processOutput(processOutput).artifactStore(getArtifactStore(logger, downloadConfigBuilderCustomizers.orderedStream())).daemonProcess(false).build();
        }

        private ArtifactStoreBuilder getArtifactStore(Logger logger, Stream<DownloadConfigBuilderCustomizer> downloadConfigBuilderCustomizers) {
            DownloadConfigBuilder downloadConfigBuilder = new DownloadConfigBuilder().defaultsForCommand(Command.MongoD);
            downloadConfigBuilder.progressListener(new Slf4jProgressListener(logger));
            downloadConfigBuilderCustomizers.forEach(customizer -> {
                customizer.customize(downloadConfigBuilder);
            });
            IDownloadConfig downloadConfig = downloadConfigBuilder.build();
            return new ExtractedArtifactStoreBuilder().defaults(Command.MongoD).download(downloadConfig);
        }
    }

    @ConditionalOnClass({MongoClient.class, MongoClientFactoryBean.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/embedded/EmbeddedMongoAutoConfiguration$EmbeddedMongoClientDependsOnBeanFactoryPostProcessor.class */
    static class EmbeddedMongoClientDependsOnBeanFactoryPostProcessor extends MongoClientDependsOnBeanFactoryPostProcessor {
        EmbeddedMongoClientDependsOnBeanFactoryPostProcessor() {
            super(MongodExecutable.class);
        }
    }

    @ConditionalOnClass({com.mongodb.reactivestreams.client.MongoClient.class, ReactiveMongoClientFactoryBean.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/mongo/embedded/EmbeddedMongoAutoConfiguration$EmbeddedReactiveStreamsMongoClientDependsOnBeanFactoryPostProcessor.class */
    static class EmbeddedReactiveStreamsMongoClientDependsOnBeanFactoryPostProcessor extends ReactiveStreamsMongoClientDependsOnBeanFactoryPostProcessor {
        EmbeddedReactiveStreamsMongoClientDependsOnBeanFactoryPostProcessor() {
            super(MongodExecutable.class);
        }
    }
}
