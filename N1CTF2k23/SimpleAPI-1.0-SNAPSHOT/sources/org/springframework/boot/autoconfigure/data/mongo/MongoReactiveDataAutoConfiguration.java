package org.springframework.boot.autoconfigure.data.mongo;

import com.mongodb.ClientSessionOptions;
import com.mongodb.reactivestreams.client.ClientSession;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoDatabase;
import java.util.Optional;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.convert.NoOpDbRefResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsOperations;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
@EnableConfigurationProperties({MongoProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({MongoClient.class, ReactiveMongoTemplate.class})
@AutoConfigureAfter({MongoReactiveAutoConfiguration.class})
@ConditionalOnBean({MongoClient.class})
@Import({MongoDataConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/mongo/MongoReactiveDataAutoConfiguration.class */
public class MongoReactiveDataAutoConfiguration {
    @ConditionalOnMissingBean({ReactiveMongoDatabaseFactory.class})
    @Bean
    public SimpleReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory(MongoProperties properties, MongoClient mongo) {
        String database = properties.getMongoClientDatabase();
        return new SimpleReactiveMongoDatabaseFactory(mongo, database);
    }

    @ConditionalOnMissingBean({ReactiveMongoOperations.class})
    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory, MongoConverter converter) {
        return new ReactiveMongoTemplate(reactiveMongoDatabaseFactory, converter);
    }

    @ConditionalOnMissingBean({MongoConverter.class})
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoMappingContext context, MongoCustomConversions conversions) {
        MappingMongoConverter mappingConverter = new MappingMongoConverter(NoOpDbRefResolver.INSTANCE, context);
        mappingConverter.setCustomConversions(conversions);
        return mappingConverter;
    }

    @ConditionalOnMissingBean({DataBufferFactory.class})
    @Bean
    public DefaultDataBufferFactory dataBufferFactory() {
        return new DefaultDataBufferFactory();
    }

    @ConditionalOnMissingBean({ReactiveGridFsOperations.class})
    @Bean
    public ReactiveGridFsTemplate reactiveGridFsTemplate(ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory, MappingMongoConverter mappingMongoConverter, DataBufferFactory dataBufferFactory, MongoProperties properties) {
        return new ReactiveGridFsTemplate(dataBufferFactory, new GridFsReactiveMongoDatabaseFactory(reactiveMongoDatabaseFactory, properties), mappingMongoConverter, (String) null);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/mongo/MongoReactiveDataAutoConfiguration$GridFsReactiveMongoDatabaseFactory.class */
    static class GridFsReactiveMongoDatabaseFactory implements ReactiveMongoDatabaseFactory {
        private final ReactiveMongoDatabaseFactory delegate;
        private final MongoProperties properties;

        GridFsReactiveMongoDatabaseFactory(ReactiveMongoDatabaseFactory delegate, MongoProperties properties) {
            this.delegate = delegate;
            this.properties = properties;
        }

        public boolean hasCodecFor(Class<?> type) {
            return this.delegate.hasCodecFor(type);
        }

        public Mono<MongoDatabase> getMongoDatabase() throws DataAccessException {
            String gridFsDatabase = this.properties.getGridFsDatabase();
            if (StringUtils.hasText(gridFsDatabase)) {
                return this.delegate.getMongoDatabase(gridFsDatabase);
            }
            return this.delegate.getMongoDatabase();
        }

        public Mono<MongoDatabase> getMongoDatabase(String dbName) throws DataAccessException {
            return this.delegate.getMongoDatabase(dbName);
        }

        public <T> Optional<Codec<T>> getCodecFor(Class<T> type) {
            return this.delegate.getCodecFor(type);
        }

        public PersistenceExceptionTranslator getExceptionTranslator() {
            return this.delegate.getExceptionTranslator();
        }

        public CodecRegistry getCodecRegistry() {
            return this.delegate.getCodecRegistry();
        }

        public Mono<ClientSession> getSession(ClientSessionOptions options) {
            return this.delegate.getSession(options);
        }

        public ReactiveMongoDatabaseFactory withSession(ClientSession session) {
            return this.delegate.withSession(session);
        }

        public boolean isTransactionActive() {
            return this.delegate.isTransactionActive();
        }
    }
}
