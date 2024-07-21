package org.springframework.boot.autoconfigure.data.r2dbc;

import io.r2dbc.spi.ConnectionFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.r2dbc.convert.MappingR2dbcConverter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext;
import org.springframework.data.r2dbc.support.R2dbcExceptionSubclassTranslator;
import org.springframework.data.r2dbc.support.R2dbcExceptionTranslator;
import org.springframework.data.relational.core.mapping.NamingStrategy;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({DatabaseClient.class})
@ConditionalOnSingleCandidate(ConnectionFactory.class)
@AutoConfigureAfter({R2dbcAutoConfiguration.class})
@ConditionalOnMissingBean({DatabaseClient.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/r2dbc/R2dbcDataAutoConfiguration.class */
public class R2dbcDataAutoConfiguration {
    private final ConnectionFactory connectionFactory;

    public R2dbcDataAutoConfiguration(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @ConditionalOnMissingBean
    @Bean
    public DatabaseClient r2dbcDatabaseClient(ReactiveDataAccessStrategy dataAccessStrategy, R2dbcExceptionTranslator exceptionTranslator) {
        return DatabaseClient.builder().connectionFactory(this.connectionFactory).dataAccessStrategy(dataAccessStrategy).exceptionTranslator(exceptionTranslator).build();
    }

    @ConditionalOnMissingBean
    @Bean
    public R2dbcMappingContext r2dbcMappingContext(ObjectProvider<NamingStrategy> namingStrategy, R2dbcCustomConversions r2dbcCustomConversions) {
        R2dbcMappingContext relationalMappingContext = new R2dbcMappingContext(namingStrategy.getIfAvailable(() -> {
            return NamingStrategy.INSTANCE;
        }));
        relationalMappingContext.setSimpleTypeHolder(r2dbcCustomConversions.getSimpleTypeHolder());
        return relationalMappingContext;
    }

    @ConditionalOnMissingBean
    @Bean
    public ReactiveDataAccessStrategy reactiveDataAccessStrategy(R2dbcMappingContext mappingContext, R2dbcCustomConversions r2dbcCustomConversions) {
        MappingR2dbcConverter converter = new MappingR2dbcConverter(mappingContext, r2dbcCustomConversions);
        return new DefaultReactiveDataAccessStrategy(DialectResolver.getDialect(this.connectionFactory), converter);
    }

    @ConditionalOnMissingBean
    @Bean
    public R2dbcCustomConversions r2dbcCustomConversions() {
        R2dbcDialect dialect = DialectResolver.getDialect(this.connectionFactory);
        List<Object> converters = new ArrayList<>(dialect.getConverters());
        converters.addAll(R2dbcCustomConversions.STORE_CONVERTERS);
        return new R2dbcCustomConversions(CustomConversions.StoreConversions.of(dialect.getSimpleTypeHolder(), converters), Collections.emptyList());
    }

    @ConditionalOnMissingBean
    @Bean
    public R2dbcExceptionTranslator r2dbcExceptionTranslator() {
        return new R2dbcExceptionSubclassTranslator();
    }
}
