package org.springframework.boot.autoconfigure.r2dbc;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactory;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/r2dbc/ConnectionFactoryConfigurations.class */
abstract class ConnectionFactoryConfigurations {
    ConnectionFactoryConfigurations() {
    }

    protected static ConnectionFactory createConnectionFactory(R2dbcProperties properties, ClassLoader classLoader, List<ConnectionFactoryOptionsBuilderCustomizer> optionsCustomizers) {
        return ConnectionFactoryBuilder.of(properties, () -> {
            return EmbeddedDatabaseConnection.get(classLoader);
        }).configure(options -> {
            Iterator it = optionsCustomizers.iterator();
            while (it.hasNext()) {
                ConnectionFactoryOptionsBuilderCustomizer optionsCustomizer = (ConnectionFactoryOptionsBuilderCustomizer) it.next();
                optionsCustomizer.customize(options);
            }
        }).build();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({ConnectionPool.class})
    @ConditionalOnMissingBean({ConnectionFactory.class})
    @Conditional({PooledConnectionFactoryCondition.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/r2dbc/ConnectionFactoryConfigurations$Pool.class */
    static class Pool {
        Pool() {
        }

        @Bean(destroyMethod = "dispose")
        ConnectionPool connectionFactory(R2dbcProperties properties, ResourceLoader resourceLoader, ObjectProvider<ConnectionFactoryOptionsBuilderCustomizer> customizers) {
            ConnectionFactory connectionFactory = ConnectionFactoryConfigurations.createConnectionFactory(properties, resourceLoader.getClassLoader(), (List) customizers.orderedStream().collect(Collectors.toList()));
            R2dbcProperties.Pool pool = properties.getPool();
            ConnectionPoolConfiguration.Builder builder = ConnectionPoolConfiguration.builder(connectionFactory).maxSize(pool.getMaxSize()).initialSize(pool.getInitialSize()).maxIdleTime(pool.getMaxIdleTime());
            if (StringUtils.hasText(pool.getValidationQuery())) {
                builder.validationQuery(pool.getValidationQuery());
            }
            return new ConnectionPool(builder.build());
        }
    }

    @ConditionalOnMissingBean({ConnectionFactory.class})
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(prefix = "spring.r2dbc.pool", value = {"enabled"}, havingValue = "false", matchIfMissing = true)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/r2dbc/ConnectionFactoryConfigurations$Generic.class */
    static class Generic {
        Generic() {
        }

        @Bean
        ConnectionFactory connectionFactory(R2dbcProperties properties, ResourceLoader resourceLoader, ObjectProvider<ConnectionFactoryOptionsBuilderCustomizer> customizers) {
            return ConnectionFactoryConfigurations.createConnectionFactory(properties, resourceLoader.getClassLoader(), (List) customizers.orderedStream().collect(Collectors.toList()));
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/r2dbc/ConnectionFactoryConfigurations$PooledConnectionFactoryCondition.class */
    static class PooledConnectionFactoryCondition extends SpringBootCondition {
        PooledConnectionFactoryCondition() {
        }

        @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            boolean poolEnabled = ((Boolean) context.getEnvironment().getProperty("spring.r2dbc.pool.enabled", Boolean.class, true)).booleanValue();
            if (poolEnabled) {
                String url = context.getEnvironment().getProperty("spring.r2dbc.url");
                boolean pooledUrl = StringUtils.hasText(url) && url.contains(":pool:");
                if (pooledUrl) {
                    return ConditionOutcome.noMatch("R2DBC Connection URL contains pooling-related options");
                }
                return ConditionOutcome.match("Pooling is enabled and R2DBC Connection URL does not contain pooling-related options");
            }
            return ConditionOutcome.noMatch("Pooling is disabled");
        }
    }
}
