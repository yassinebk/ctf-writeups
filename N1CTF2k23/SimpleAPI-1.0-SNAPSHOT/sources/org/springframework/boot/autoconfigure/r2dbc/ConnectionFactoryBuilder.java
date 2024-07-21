package org.springframework.boot.autoconfigure.r2dbc;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/r2dbc/ConnectionFactoryBuilder.class */
public final class ConnectionFactoryBuilder {
    private final ConnectionFactoryOptions.Builder optionsBuilder;

    private ConnectionFactoryBuilder(ConnectionFactoryOptions.Builder optionsBuilder) {
        this.optionsBuilder = optionsBuilder;
    }

    public static ConnectionFactoryBuilder of(R2dbcProperties properties, Supplier<EmbeddedDatabaseConnection> embeddedDatabaseConnection) {
        return new ConnectionFactoryBuilder(new ConnectionFactoryOptionsInitializer().initializeOptions(properties, embeddedDatabaseConnection));
    }

    public ConnectionFactoryBuilder configure(Consumer<ConnectionFactoryOptions.Builder> options) {
        options.accept(this.optionsBuilder);
        return this;
    }

    public ConnectionFactoryBuilder username(String username) {
        return configure(options -> {
            options.option(ConnectionFactoryOptions.USER, username);
        });
    }

    public ConnectionFactoryBuilder password(CharSequence password) {
        return configure(options -> {
            options.option(ConnectionFactoryOptions.PASSWORD, password);
        });
    }

    public ConnectionFactoryBuilder hostname(String host) {
        return configure(options -> {
            options.option(ConnectionFactoryOptions.HOST, host);
        });
    }

    public ConnectionFactoryBuilder port(int port) {
        return configure(options -> {
            options.option(ConnectionFactoryOptions.PORT, Integer.valueOf(port));
        });
    }

    public ConnectionFactoryBuilder database(String database) {
        return configure(options -> {
            options.option(ConnectionFactoryOptions.DATABASE, database);
        });
    }

    public ConnectionFactory build() {
        return ConnectionFactories.get(buildOptions());
    }

    public ConnectionFactoryOptions buildOptions() {
        return this.optionsBuilder.build();
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/r2dbc/ConnectionFactoryBuilder$ConnectionFactoryOptionsInitializer.class */
    static class ConnectionFactoryOptionsInitializer {
        ConnectionFactoryOptionsInitializer() {
        }

        ConnectionFactoryOptions.Builder initializeOptions(R2dbcProperties properties, Supplier<EmbeddedDatabaseConnection> embeddedDatabaseConnection) {
            if (StringUtils.hasText(properties.getUrl())) {
                return initializeRegularOptions(properties);
            }
            EmbeddedDatabaseConnection embeddedConnection = embeddedDatabaseConnection.get();
            if (embeddedConnection != EmbeddedDatabaseConnection.NONE) {
                return initializeEmbeddedOptions(properties, embeddedConnection);
            }
            throw connectionFactoryBeanCreationException("Failed to determine a suitable R2DBC Connection URL", properties, embeddedConnection);
        }

        private ConnectionFactoryOptions.Builder initializeRegularOptions(R2dbcProperties properties) {
            ConnectionFactoryOptions urlOptions = ConnectionFactoryOptions.parse(properties.getUrl());
            ConnectionFactoryOptions.Builder optionsBuilder = urlOptions.mutate();
            Option option = ConnectionFactoryOptions.USER;
            properties.getClass();
            configureIf(optionsBuilder, urlOptions, option, this::getUsername, StringUtils::hasText);
            Option option2 = ConnectionFactoryOptions.PASSWORD;
            properties.getClass();
            configureIf(optionsBuilder, urlOptions, option2, this::getPassword, StringUtils::hasText);
            configureIf(optionsBuilder, urlOptions, ConnectionFactoryOptions.DATABASE, () -> {
                return determineDatabaseName(properties);
            }, StringUtils::hasText);
            if (properties.getProperties() != null) {
                properties.getProperties().forEach(key, value -> {
                    optionsBuilder.option(Option.valueOf(key), value);
                });
            }
            return optionsBuilder;
        }

        private ConnectionFactoryOptions.Builder initializeEmbeddedOptions(R2dbcProperties properties, EmbeddedDatabaseConnection embeddedDatabaseConnection) {
            String url = embeddedDatabaseConnection.getUrl(determineEmbeddedDatabaseName(properties));
            if (url == null) {
                throw connectionFactoryBeanCreationException("Failed to determine a suitable R2DBC Connection URL", properties, embeddedDatabaseConnection);
            }
            ConnectionFactoryOptions.Builder builder = ConnectionFactoryOptions.parse(url).mutate();
            String username = determineEmbeddedUsername(properties);
            if (StringUtils.hasText(username)) {
                builder.option(ConnectionFactoryOptions.USER, username);
            }
            if (StringUtils.hasText(properties.getPassword())) {
                builder.option(ConnectionFactoryOptions.PASSWORD, properties.getPassword());
            }
            return builder;
        }

        private String determineDatabaseName(R2dbcProperties properties) {
            if (properties.isGenerateUniqueName()) {
                return properties.determineUniqueName();
            }
            if (StringUtils.hasLength(properties.getName())) {
                return properties.getName();
            }
            return null;
        }

        private String determineEmbeddedDatabaseName(R2dbcProperties properties) {
            String databaseName = determineDatabaseName(properties);
            return databaseName != null ? databaseName : "testdb";
        }

        private String determineEmbeddedUsername(R2dbcProperties properties) {
            String username = ifHasText(properties.getUsername());
            return username != null ? username : "sa";
        }

        private <T extends CharSequence> void configureIf(ConnectionFactoryOptions.Builder optionsBuilder, ConnectionFactoryOptions originalOptions, Option<T> option, Supplier<T> valueSupplier, Predicate<T> setIf) {
            if (originalOptions.hasOption(option)) {
                return;
            }
            T value = valueSupplier.get();
            if (setIf.test(value)) {
                optionsBuilder.option(option, value);
            }
        }

        private ConnectionFactoryBeanCreationException connectionFactoryBeanCreationException(String message, R2dbcProperties properties, EmbeddedDatabaseConnection embeddedDatabaseConnection) {
            return new ConnectionFactoryBeanCreationException(message, properties, embeddedDatabaseConnection);
        }

        private String ifHasText(String candidate) {
            if (StringUtils.hasText(candidate)) {
                return candidate;
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/r2dbc/ConnectionFactoryBuilder$ConnectionFactoryBeanCreationException.class */
    public static class ConnectionFactoryBeanCreationException extends BeanCreationException {
        private final R2dbcProperties properties;
        private final EmbeddedDatabaseConnection embeddedDatabaseConnection;

        ConnectionFactoryBeanCreationException(String message, R2dbcProperties properties, EmbeddedDatabaseConnection embeddedDatabaseConnection) {
            super(message);
            this.properties = properties;
            this.embeddedDatabaseConnection = embeddedDatabaseConnection;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public EmbeddedDatabaseConnection getEmbeddedDatabaseConnection() {
            return this.embeddedDatabaseConnection;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public R2dbcProperties getProperties() {
            return this.properties;
        }
    }
}
