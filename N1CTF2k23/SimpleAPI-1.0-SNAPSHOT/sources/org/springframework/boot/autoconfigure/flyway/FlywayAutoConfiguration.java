package org.springframework.boot.autoconfigure.flyway;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.migration.JavaMigration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.jpa.EntityManagerFactoryDependsOnPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.JdbcOperationsDependsOnPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.NamedParameterJdbcOperationsDependsOnPostProcessor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Flyway.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class, JdbcTemplateAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@Conditional({FlywayDataSourceCondition.class})
@ConditionalOnProperty(prefix = "spring.flyway", name = {"enabled"}, matchIfMissing = true)
@Import({FlywayEntityManagerFactoryDependsOnPostProcessor.class, FlywayJdbcOperationsDependsOnPostProcessor.class, FlywayNamedParameterJdbcOperationsDependencyConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration.class */
public class FlywayAutoConfiguration {
    @ConfigurationPropertiesBinding
    @Bean
    public StringOrNumberToMigrationVersionConverter stringOrNumberMigrationVersionConverter() {
        return new StringOrNumberToMigrationVersionConverter();
    }

    @Bean
    public FlywaySchemaManagementProvider flywayDefaultDdlModeProvider(ObjectProvider<Flyway> flyways) {
        return new FlywaySchemaManagementProvider(flyways);
    }

    @EnableConfigurationProperties({DataSourceProperties.class, FlywayProperties.class})
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean({Flyway.class})
    @Import({FlywayMigrationInitializerEntityManagerFactoryDependsOnPostProcessor.class, FlywayMigrationInitializerJdbcOperationsDependsOnPostProcessor.class, FlywayMigrationInitializerNamedParameterJdbcOperationsDependsOnPostProcessor.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$FlywayConfiguration.class */
    public static class FlywayConfiguration {
        @Bean
        public Flyway flyway(FlywayProperties properties, DataSourceProperties dataSourceProperties, ResourceLoader resourceLoader, ObjectProvider<DataSource> dataSource, @FlywayDataSource ObjectProvider<DataSource> flywayDataSource, ObjectProvider<FlywayConfigurationCustomizer> fluentConfigurationCustomizers, ObjectProvider<JavaMigration> javaMigrations, ObjectProvider<Callback> callbacks) {
            FluentConfiguration configuration = new FluentConfiguration(resourceLoader.getClassLoader());
            DataSource dataSourceToMigrate = configureDataSource(configuration, properties, dataSourceProperties, flywayDataSource.getIfAvailable(), dataSource.getIfUnique());
            checkLocationExists(dataSourceToMigrate, properties, resourceLoader);
            configureProperties(configuration, properties);
            List<Callback> orderedCallbacks = (List) callbacks.orderedStream().collect(Collectors.toList());
            configureCallbacks(configuration, orderedCallbacks);
            fluentConfigurationCustomizers.orderedStream().forEach(customizer -> {
                customizer.customize(configuration);
            });
            configureFlywayCallbacks(configuration, orderedCallbacks);
            List<JavaMigration> migrations = (List) javaMigrations.stream().collect(Collectors.toList());
            configureJavaMigrations(configuration, migrations);
            return configuration.load();
        }

        private DataSource configureDataSource(FluentConfiguration configuration, FlywayProperties properties, DataSourceProperties dataSourceProperties, DataSource flywayDataSource, DataSource dataSource) {
            if (properties.isCreateDataSource()) {
                properties.getClass();
                Supplier<String> supplier = this::getUrl;
                dataSourceProperties.getClass();
                String url = getProperty(supplier, this::determineUrl);
                properties.getClass();
                Supplier<String> supplier2 = this::getUser;
                dataSourceProperties.getClass();
                String user = getProperty(supplier2, this::determineUsername);
                properties.getClass();
                Supplier<String> supplier3 = this::getPassword;
                dataSourceProperties.getClass();
                String password = getProperty(supplier3, this::determinePassword);
                configuration.dataSource(url, user, password);
                if (!CollectionUtils.isEmpty(properties.getInitSqls())) {
                    String initSql = StringUtils.collectionToDelimitedString(properties.getInitSqls(), "\n");
                    configuration.initSql(initSql);
                }
            } else if (flywayDataSource != null) {
                configuration.dataSource(flywayDataSource);
            } else {
                configuration.dataSource(dataSource);
            }
            return configuration.getDataSource();
        }

        private void checkLocationExists(DataSource dataSource, FlywayProperties properties, ResourceLoader resourceLoader) {
            if (properties.isCheckLocation()) {
                List<String> locations = new LocationResolver(dataSource).resolveLocations(properties.getLocations());
                if (!hasAtLeastOneLocation(resourceLoader, locations)) {
                    throw new FlywayMigrationScriptMissingException(locations);
                }
            }
        }

        private void configureProperties(FluentConfiguration configuration, FlywayProperties properties) {
            PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
            String[] locations = (String[]) new LocationResolver(configuration.getDataSource()).resolveLocations(properties.getLocations()).toArray(new String[0]);
            PropertyMapper.Source from = map.from((PropertyMapper) locations);
            configuration.getClass();
            from.to(this::locations);
            PropertyMapper.Source from2 = map.from((PropertyMapper) properties.getEncoding());
            configuration.getClass();
            from2.to(this::encoding);
            PropertyMapper.Source from3 = map.from((PropertyMapper) Integer.valueOf(properties.getConnectRetries()));
            configuration.getClass();
            from3.to((v1) -> {
                r1.connectRetries(v1);
            });
            map.from((PropertyMapper) properties.getDefaultSchema()).to(schema -> {
                configuration.defaultSchema(schema);
            });
            PropertyMapper.Source as = map.from((PropertyMapper) properties.getSchemas()).as((v0) -> {
                return StringUtils.toStringArray(v0);
            });
            configuration.getClass();
            as.to(this::schemas);
            PropertyMapper.Source from4 = map.from((PropertyMapper) properties.getTable());
            configuration.getClass();
            from4.to(this::table);
            map.from((PropertyMapper) properties.getTablespace()).whenNonNull().to(tablespace -> {
                configuration.tablespace(tablespace);
            });
            PropertyMapper.Source from5 = map.from((PropertyMapper) properties.getBaselineDescription());
            configuration.getClass();
            from5.to(this::baselineDescription);
            PropertyMapper.Source from6 = map.from((PropertyMapper) properties.getBaselineVersion());
            configuration.getClass();
            from6.to(this::baselineVersion);
            PropertyMapper.Source from7 = map.from((PropertyMapper) properties.getInstalledBy());
            configuration.getClass();
            from7.to(this::installedBy);
            PropertyMapper.Source from8 = map.from((PropertyMapper) properties.getPlaceholders());
            configuration.getClass();
            from8.to(this::placeholders);
            PropertyMapper.Source from9 = map.from((PropertyMapper) properties.getPlaceholderPrefix());
            configuration.getClass();
            from9.to(this::placeholderPrefix);
            PropertyMapper.Source from10 = map.from((PropertyMapper) properties.getPlaceholderSuffix());
            configuration.getClass();
            from10.to(this::placeholderSuffix);
            PropertyMapper.Source from11 = map.from((PropertyMapper) Boolean.valueOf(properties.isPlaceholderReplacement()));
            configuration.getClass();
            from11.to((v1) -> {
                r1.placeholderReplacement(v1);
            });
            PropertyMapper.Source from12 = map.from((PropertyMapper) properties.getSqlMigrationPrefix());
            configuration.getClass();
            from12.to(this::sqlMigrationPrefix);
            PropertyMapper.Source as2 = map.from((PropertyMapper) properties.getSqlMigrationSuffixes()).as((v0) -> {
                return StringUtils.toStringArray(v0);
            });
            configuration.getClass();
            as2.to(this::sqlMigrationSuffixes);
            PropertyMapper.Source from13 = map.from((PropertyMapper) properties.getSqlMigrationSeparator());
            configuration.getClass();
            from13.to(this::sqlMigrationSeparator);
            PropertyMapper.Source from14 = map.from((PropertyMapper) properties.getRepeatableSqlMigrationPrefix());
            configuration.getClass();
            from14.to(this::repeatableSqlMigrationPrefix);
            PropertyMapper.Source from15 = map.from((PropertyMapper) properties.getTarget());
            configuration.getClass();
            from15.to(this::target);
            PropertyMapper.Source from16 = map.from((PropertyMapper) Boolean.valueOf(properties.isBaselineOnMigrate()));
            configuration.getClass();
            from16.to((v1) -> {
                r1.baselineOnMigrate(v1);
            });
            PropertyMapper.Source from17 = map.from((PropertyMapper) Boolean.valueOf(properties.isCleanDisabled()));
            configuration.getClass();
            from17.to((v1) -> {
                r1.cleanDisabled(v1);
            });
            PropertyMapper.Source from18 = map.from((PropertyMapper) Boolean.valueOf(properties.isCleanOnValidationError()));
            configuration.getClass();
            from18.to((v1) -> {
                r1.cleanOnValidationError(v1);
            });
            PropertyMapper.Source from19 = map.from((PropertyMapper) Boolean.valueOf(properties.isGroup()));
            configuration.getClass();
            from19.to((v1) -> {
                r1.group(v1);
            });
            PropertyMapper.Source from20 = map.from((PropertyMapper) Boolean.valueOf(properties.isIgnoreMissingMigrations()));
            configuration.getClass();
            from20.to((v1) -> {
                r1.ignoreMissingMigrations(v1);
            });
            PropertyMapper.Source from21 = map.from((PropertyMapper) Boolean.valueOf(properties.isIgnoreIgnoredMigrations()));
            configuration.getClass();
            from21.to((v1) -> {
                r1.ignoreIgnoredMigrations(v1);
            });
            PropertyMapper.Source from22 = map.from((PropertyMapper) Boolean.valueOf(properties.isIgnorePendingMigrations()));
            configuration.getClass();
            from22.to((v1) -> {
                r1.ignorePendingMigrations(v1);
            });
            PropertyMapper.Source from23 = map.from((PropertyMapper) Boolean.valueOf(properties.isIgnoreFutureMigrations()));
            configuration.getClass();
            from23.to((v1) -> {
                r1.ignoreFutureMigrations(v1);
            });
            PropertyMapper.Source from24 = map.from((PropertyMapper) Boolean.valueOf(properties.isMixed()));
            configuration.getClass();
            from24.to((v1) -> {
                r1.mixed(v1);
            });
            PropertyMapper.Source from25 = map.from((PropertyMapper) Boolean.valueOf(properties.isOutOfOrder()));
            configuration.getClass();
            from25.to((v1) -> {
                r1.outOfOrder(v1);
            });
            PropertyMapper.Source from26 = map.from((PropertyMapper) Boolean.valueOf(properties.isSkipDefaultCallbacks()));
            configuration.getClass();
            from26.to((v1) -> {
                r1.skipDefaultCallbacks(v1);
            });
            PropertyMapper.Source from27 = map.from((PropertyMapper) Boolean.valueOf(properties.isSkipDefaultResolvers()));
            configuration.getClass();
            from27.to((v1) -> {
                r1.skipDefaultResolvers(v1);
            });
            configureValidateMigrationNaming(configuration, properties.isValidateMigrationNaming());
            PropertyMapper.Source from28 = map.from((PropertyMapper) Boolean.valueOf(properties.isValidateOnMigrate()));
            configuration.getClass();
            from28.to((v1) -> {
                r1.validateOnMigrate(v1);
            });
            PropertyMapper.Source whenNonNull = map.from((PropertyMapper) properties.getBatch()).whenNonNull();
            configuration.getClass();
            whenNonNull.to((v1) -> {
                r1.batch(v1);
            });
            PropertyMapper.Source whenNonNull2 = map.from((PropertyMapper) properties.getDryRunOutput()).whenNonNull();
            configuration.getClass();
            whenNonNull2.to(this::dryRunOutput);
            PropertyMapper.Source whenNonNull3 = map.from((PropertyMapper) properties.getErrorOverrides()).whenNonNull();
            configuration.getClass();
            whenNonNull3.to(this::errorOverrides);
            PropertyMapper.Source whenNonNull4 = map.from((PropertyMapper) properties.getLicenseKey()).whenNonNull();
            configuration.getClass();
            whenNonNull4.to(this::licenseKey);
            PropertyMapper.Source whenNonNull5 = map.from((PropertyMapper) properties.getOracleSqlplus()).whenNonNull();
            configuration.getClass();
            whenNonNull5.to((v1) -> {
                r1.oracleSqlplus(v1);
            });
            map.from((PropertyMapper) properties.getOracleSqlplusWarn()).whenNonNull().to(oracleSqlplusWarn -> {
                configuration.oracleSqlplusWarn(oracleSqlplusWarn.booleanValue());
            });
            PropertyMapper.Source whenNonNull6 = map.from((PropertyMapper) properties.getStream()).whenNonNull();
            configuration.getClass();
            whenNonNull6.to((v1) -> {
                r1.stream(v1);
            });
            PropertyMapper.Source whenNonNull7 = map.from((PropertyMapper) properties.getUndoSqlMigrationPrefix()).whenNonNull();
            configuration.getClass();
            whenNonNull7.to(this::undoSqlMigrationPrefix);
        }

        private void configureValidateMigrationNaming(FluentConfiguration configuration, boolean validateMigrationNaming) {
            try {
                configuration.validateMigrationNaming(validateMigrationNaming);
            } catch (NoSuchMethodError e) {
            }
        }

        private void configureCallbacks(FluentConfiguration configuration, List<Callback> callbacks) {
            if (!callbacks.isEmpty()) {
                configuration.callbacks((Callback[]) callbacks.toArray(new Callback[0]));
            }
        }

        private void configureFlywayCallbacks(FluentConfiguration flyway, List<Callback> callbacks) {
            if (!callbacks.isEmpty()) {
                flyway.callbacks((Callback[]) callbacks.toArray(new Callback[0]));
            }
        }

        private void configureJavaMigrations(FluentConfiguration flyway, List<JavaMigration> migrations) {
            if (!migrations.isEmpty()) {
                try {
                    flyway.javaMigrations((JavaMigration[]) migrations.toArray(new JavaMigration[0]));
                } catch (NoSuchMethodError e) {
                }
            }
        }

        private String getProperty(Supplier<String> property, Supplier<String> defaultValue) {
            String value = property.get();
            return value != null ? value : defaultValue.get();
        }

        private boolean hasAtLeastOneLocation(ResourceLoader resourceLoader, Collection<String> locations) {
            for (String location : locations) {
                if (resourceLoader.getResource(normalizePrefix(location)).exists()) {
                    return true;
                }
            }
            return false;
        }

        private String normalizePrefix(String location) {
            return location.replace("filesystem:", ResourceUtils.FILE_URL_PREFIX);
        }

        @ConditionalOnMissingBean
        @Bean
        public FlywayMigrationInitializer flywayInitializer(Flyway flyway, ObjectProvider<FlywayMigrationStrategy> migrationStrategy) {
            return new FlywayMigrationInitializer(flyway, migrationStrategy.getIfAvailable());
        }
    }

    @ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class})
    @ConditionalOnBean({AbstractEntityManagerFactoryBean.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$FlywayMigrationInitializerEntityManagerFactoryDependsOnPostProcessor.class */
    static class FlywayMigrationInitializerEntityManagerFactoryDependsOnPostProcessor extends EntityManagerFactoryDependsOnPostProcessor {
        FlywayMigrationInitializerEntityManagerFactoryDependsOnPostProcessor() {
            super(FlywayMigrationInitializer.class);
        }
    }

    @ConditionalOnClass({JdbcOperations.class})
    @ConditionalOnBean({JdbcOperations.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$FlywayMigrationInitializerJdbcOperationsDependsOnPostProcessor.class */
    static class FlywayMigrationInitializerJdbcOperationsDependsOnPostProcessor extends JdbcOperationsDependsOnPostProcessor {
        FlywayMigrationInitializerJdbcOperationsDependsOnPostProcessor() {
            super(FlywayMigrationInitializer.class);
        }
    }

    @ConditionalOnClass({NamedParameterJdbcOperations.class})
    @ConditionalOnBean({NamedParameterJdbcOperations.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$FlywayMigrationInitializerNamedParameterJdbcOperationsDependsOnPostProcessor.class */
    static class FlywayMigrationInitializerNamedParameterJdbcOperationsDependsOnPostProcessor extends NamedParameterJdbcOperationsDependsOnPostProcessor {
        FlywayMigrationInitializerNamedParameterJdbcOperationsDependsOnPostProcessor() {
            super(FlywayMigrationInitializer.class);
        }
    }

    @ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class})
    @ConditionalOnBean({AbstractEntityManagerFactoryBean.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$FlywayEntityManagerFactoryDependsOnPostProcessor.class */
    static class FlywayEntityManagerFactoryDependsOnPostProcessor extends EntityManagerFactoryDependsOnPostProcessor {
        FlywayEntityManagerFactoryDependsOnPostProcessor() {
            super(Flyway.class);
        }
    }

    @ConditionalOnClass({JdbcOperations.class})
    @ConditionalOnBean({JdbcOperations.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$FlywayJdbcOperationsDependsOnPostProcessor.class */
    static class FlywayJdbcOperationsDependsOnPostProcessor extends JdbcOperationsDependsOnPostProcessor {
        FlywayJdbcOperationsDependsOnPostProcessor() {
            super(Flyway.class);
        }
    }

    @ConditionalOnClass({NamedParameterJdbcOperations.class})
    @ConditionalOnBean({NamedParameterJdbcOperations.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$FlywayNamedParameterJdbcOperationsDependencyConfiguration.class */
    protected static class FlywayNamedParameterJdbcOperationsDependencyConfiguration extends NamedParameterJdbcOperationsDependsOnPostProcessor {
        public FlywayNamedParameterJdbcOperationsDependencyConfiguration() {
            super(Flyway.class);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$LocationResolver.class */
    public static class LocationResolver {
        private static final String VENDOR_PLACEHOLDER = "{vendor}";
        private final DataSource dataSource;

        LocationResolver(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        List<String> resolveLocations(List<String> locations) {
            if (usesVendorLocation(locations)) {
                DatabaseDriver databaseDriver = getDatabaseDriver();
                return replaceVendorLocations(locations, databaseDriver);
            }
            return locations;
        }

        private List<String> replaceVendorLocations(List<String> locations, DatabaseDriver databaseDriver) {
            if (databaseDriver == DatabaseDriver.UNKNOWN) {
                return locations;
            }
            String vendor = databaseDriver.getId();
            return (List) locations.stream().map(location -> {
                return location.replace(VENDOR_PLACEHOLDER, vendor);
            }).collect(Collectors.toList());
        }

        private DatabaseDriver getDatabaseDriver() {
            try {
                String url = (String) JdbcUtils.extractDatabaseMetaData(this.dataSource, "getURL");
                return DatabaseDriver.fromJdbcUrl(url);
            } catch (MetaDataAccessException ex) {
                throw new IllegalStateException((Throwable) ex);
            }
        }

        private boolean usesVendorLocation(Collection<String> locations) {
            for (String location : locations) {
                if (location.contains(VENDOR_PLACEHOLDER)) {
                    return true;
                }
            }
            return false;
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$StringOrNumberToMigrationVersionConverter.class */
    private static class StringOrNumberToMigrationVersionConverter implements GenericConverter {
        private static final Set<GenericConverter.ConvertiblePair> CONVERTIBLE_TYPES;

        private StringOrNumberToMigrationVersionConverter() {
        }

        static {
            Set<GenericConverter.ConvertiblePair> types = new HashSet<>(2);
            types.add(new GenericConverter.ConvertiblePair(String.class, MigrationVersion.class));
            types.add(new GenericConverter.ConvertiblePair(Number.class, MigrationVersion.class));
            CONVERTIBLE_TYPES = Collections.unmodifiableSet(types);
        }

        @Override // org.springframework.core.convert.converter.GenericConverter
        public Set<GenericConverter.ConvertiblePair> getConvertibleTypes() {
            return CONVERTIBLE_TYPES;
        }

        @Override // org.springframework.core.convert.converter.GenericConverter
        public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
            String value = ObjectUtils.nullSafeToString(source);
            return MigrationVersion.fromVersion(value);
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$FlywayDataSourceCondition.class */
    static final class FlywayDataSourceCondition extends AnyNestedCondition {
        FlywayDataSourceCondition() {
            super(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnBean({DataSource.class})
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$FlywayDataSourceCondition$DataSourceBeanCondition.class */
        private static final class DataSourceBeanCondition {
            private DataSourceBeanCondition() {
            }
        }

        @ConditionalOnProperty(prefix = "spring.flyway", name = {"url"}, matchIfMissing = false)
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/flyway/FlywayAutoConfiguration$FlywayDataSourceCondition$FlywayUrlCondition.class */
        private static final class FlywayUrlCondition {
            private FlywayUrlCondition() {
            }
        }
    }
}
