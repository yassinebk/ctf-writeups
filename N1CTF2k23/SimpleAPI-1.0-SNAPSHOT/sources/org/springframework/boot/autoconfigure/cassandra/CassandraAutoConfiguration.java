package org.springframework.boot.autoconfigure.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.config.DriverOption;
import com.datastax.oss.driver.internal.core.config.typesafe.DefaultProgrammaticDriverConfigLoaderBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.net.ssl.SSLContext;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
@EnableConfigurationProperties({CassandraProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({CqlSession.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/cassandra/CassandraAutoConfiguration.class */
public class CassandraAutoConfiguration {
    @ConditionalOnMissingBean
    @Bean
    @Lazy
    public CqlSession cassandraSession(CqlSessionBuilder cqlSessionBuilder) {
        return (CqlSession) cqlSessionBuilder.build();
    }

    @ConditionalOnMissingBean
    @Scope("prototype")
    @Bean
    public CqlSessionBuilder cassandraSessionBuilder(CassandraProperties properties, DriverConfigLoader driverConfigLoader, ObjectProvider<CqlSessionBuilderCustomizer> builderCustomizers) {
        CqlSessionBuilder builder = (CqlSessionBuilder) CqlSession.builder().withConfigLoader(driverConfigLoader);
        configureSsl(properties, builder);
        builder.withKeyspace(properties.getKeyspaceName());
        builderCustomizers.orderedStream().forEach(customizer -> {
            customizer.customize(builder);
        });
        return builder;
    }

    private void configureSsl(CassandraProperties properties, CqlSessionBuilder builder) {
        if (properties.isSsl()) {
            try {
                builder.withSslContext(SSLContext.getDefault());
            } catch (NoSuchAlgorithmException ex) {
                throw new IllegalStateException("Could not setup SSL default context for Cassandra", ex);
            }
        }
    }

    @ConditionalOnMissingBean
    @Bean
    public DriverConfigLoader cassandraDriverConfigLoader(CassandraProperties properties, ObjectProvider<DriverConfigLoaderBuilderCustomizer> builderCustomizers) {
        DefaultProgrammaticDriverConfigLoaderBuilder defaultProgrammaticDriverConfigLoaderBuilder = new DefaultProgrammaticDriverConfigLoaderBuilder(() -> {
            return cassandraConfiguration(properties);
        }, "datastax-java-driver");
        builderCustomizers.orderedStream().forEach(customizer -> {
            customizer.customize(defaultProgrammaticDriverConfigLoaderBuilder);
        });
        return defaultProgrammaticDriverConfigLoaderBuilder.build();
    }

    private Config cassandraConfiguration(CassandraProperties properties) {
        CassandraDriverOptions options = new CassandraDriverOptions();
        PropertyMapper map = PropertyMapper.get();
        map.from((PropertyMapper) properties.getSessionName()).whenHasText().to(sessionName -> {
            options.add((DriverOption) DefaultDriverOption.SESSION_NAME, sessionName);
        });
        properties.getClass();
        map.from(this::getUsername).whenNonNull().to(username -> {
            options.add((DriverOption) DefaultDriverOption.AUTH_PROVIDER_USER_NAME, username).add((DriverOption) DefaultDriverOption.AUTH_PROVIDER_PASSWORD, properties.getPassword());
        });
        properties.getClass();
        map.from(this::getCompression).whenNonNull().to(compression -> {
            options.add((DriverOption) DefaultDriverOption.PROTOCOL_COMPRESSION, (Enum<?>) compression);
        });
        mapConnectionOptions(properties, options);
        mapPoolingOptions(properties, options);
        mapRequestOptions(properties, options);
        map.from((PropertyMapper) mapContactPoints(properties)).to(contactPoints -> {
            options.add((DriverOption) DefaultDriverOption.CONTACT_POINTS, (List<String>) contactPoints);
        });
        map.from((PropertyMapper) properties.getLocalDatacenter()).to(localDatacenter -> {
            options.add((DriverOption) DefaultDriverOption.LOAD_BALANCING_LOCAL_DATACENTER, localDatacenter);
        });
        ConfigFactory.invalidateCaches();
        return ConfigFactory.defaultOverrides().withFallback(options.build()).withFallback(ConfigFactory.defaultReference()).resolve();
    }

    private void mapConnectionOptions(CassandraProperties properties, CassandraDriverOptions options) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        CassandraProperties.Connection connectionProperties = properties.getConnection();
        connectionProperties.getClass();
        map.from(this::getConnectTimeout).asInt((v0) -> {
            return v0.toMillis();
        }).to(connectTimeout -> {
            options.add((DriverOption) DefaultDriverOption.CONNECTION_CONNECT_TIMEOUT, connectTimeout.intValue());
        });
        connectionProperties.getClass();
        map.from(this::getInitQueryTimeout).asInt((v0) -> {
            return v0.toMillis();
        }).to(initQueryTimeout -> {
            options.add((DriverOption) DefaultDriverOption.CONNECTION_INIT_QUERY_TIMEOUT, initQueryTimeout.intValue());
        });
    }

    private void mapPoolingOptions(CassandraProperties properties, CassandraDriverOptions options) {
        PropertyMapper map = PropertyMapper.get();
        CassandraProperties.Pool poolProperties = properties.getPool();
        poolProperties.getClass();
        map.from(this::getIdleTimeout).whenNonNull().asInt((v0) -> {
            return v0.getSeconds();
        }).to(idleTimeout -> {
            options.add((DriverOption) DefaultDriverOption.HEARTBEAT_TIMEOUT, idleTimeout.intValue());
        });
        poolProperties.getClass();
        map.from(this::getHeartbeatInterval).whenNonNull().asInt((v0) -> {
            return v0.getSeconds();
        }).to(heartBeatInterval -> {
            options.add((DriverOption) DefaultDriverOption.HEARTBEAT_INTERVAL, heartBeatInterval.intValue());
        });
    }

    private void mapRequestOptions(CassandraProperties properties, CassandraDriverOptions options) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        CassandraProperties.Request requestProperties = properties.getRequest();
        requestProperties.getClass();
        map.from(this::getTimeout).asInt((v0) -> {
            return v0.toMillis();
        }).to(timeout -> {
            options.add((DriverOption) DefaultDriverOption.REQUEST_TIMEOUT, timeout.intValue());
        });
        requestProperties.getClass();
        map.from(this::getConsistency).to(consistency -> {
            options.add((DriverOption) DefaultDriverOption.REQUEST_CONSISTENCY, (Enum<?>) consistency);
        });
        requestProperties.getClass();
        map.from(this::getSerialConsistency).to(serialConsistency -> {
            options.add((DriverOption) DefaultDriverOption.REQUEST_SERIAL_CONSISTENCY, (Enum<?>) serialConsistency);
        });
        requestProperties.getClass();
        map.from(this::getPageSize).to(pageSize -> {
            options.add((DriverOption) DefaultDriverOption.REQUEST_PAGE_SIZE, pageSize.intValue());
        });
        CassandraProperties.Throttler throttlerProperties = requestProperties.getThrottler();
        throttlerProperties.getClass();
        map.from(this::getType).as((v0) -> {
            return v0.type();
        }).to(type -> {
            options.add((DriverOption) DefaultDriverOption.REQUEST_THROTTLER_CLASS, type);
        });
        throttlerProperties.getClass();
        map.from(this::getMaxQueueSize).to(maxQueueSize -> {
            options.add((DriverOption) DefaultDriverOption.REQUEST_THROTTLER_MAX_QUEUE_SIZE, maxQueueSize.intValue());
        });
        throttlerProperties.getClass();
        map.from(this::getMaxConcurrentRequests).to(maxConcurrentRequests -> {
            options.add((DriverOption) DefaultDriverOption.REQUEST_THROTTLER_MAX_CONCURRENT_REQUESTS, maxConcurrentRequests.intValue());
        });
        throttlerProperties.getClass();
        map.from(this::getMaxRequestsPerSecond).to(maxRequestsPerSecond -> {
            options.add((DriverOption) DefaultDriverOption.REQUEST_THROTTLER_MAX_REQUESTS_PER_SECOND, maxRequestsPerSecond.intValue());
        });
        throttlerProperties.getClass();
        map.from(this::getDrainInterval).asInt((v0) -> {
            return v0.toMillis();
        }).to(drainInterval -> {
            options.add((DriverOption) DefaultDriverOption.REQUEST_THROTTLER_DRAIN_INTERVAL, drainInterval.intValue());
        });
    }

    private List<String> mapContactPoints(CassandraProperties properties) {
        return (List) properties.getContactPoints().stream().map(candidate -> {
            return formatContactPoint(candidate, properties.getPort());
        }).collect(Collectors.toList());
    }

    private String formatContactPoint(String candidate, int port) {
        int i = candidate.lastIndexOf(58);
        if (i == -1 || !isPort(() -> {
            return candidate.substring(i + 1);
        })) {
            return String.format("%s:%s", candidate, Integer.valueOf(port));
        }
        return candidate;
    }

    private boolean isPort(Supplier<String> value) {
        try {
            int i = Integer.parseInt(value.get());
            return i > 0 && i < 65535;
        } catch (Exception e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/cassandra/CassandraAutoConfiguration$CassandraDriverOptions.class */
    public static class CassandraDriverOptions {
        private final Map<String, String> options;

        private CassandraDriverOptions() {
            this.options = new LinkedHashMap();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public CassandraDriverOptions add(DriverOption option, String value) {
            String key = createKeyFor(option);
            this.options.put(key, value);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public CassandraDriverOptions add(DriverOption option, int value) {
            return add(option, String.valueOf(value));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public CassandraDriverOptions add(DriverOption option, Enum<?> value) {
            return add(option, value.name());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public CassandraDriverOptions add(DriverOption option, List<String> values) {
            for (int i = 0; i < values.size(); i++) {
                this.options.put(String.format("%s.%s", createKeyFor(option), Integer.valueOf(i)), values.get(i));
            }
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Config build() {
            return ConfigFactory.parseMap(this.options, "Environment");
        }

        private static String createKeyFor(DriverOption option) {
            return String.format("%s.%s", "datastax-java-driver", option.getPath());
        }
    }
}
