package org.springframework.boot.autoconfigure.liquibase;

import java.util.function.Supplier;
import javax.sql.DataSource;
import liquibase.change.DatabaseChange;
import liquibase.integration.spring.SpringLiquibase;
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
import org.springframework.boot.autoconfigure.jdbc.NamedParameterJdbcOperationsDependsOnPostProcessor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({SpringLiquibase.class, DatabaseChange.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ConditionalOnProperty(prefix = "spring.liquibase", name = {"enabled"}, matchIfMissing = true)
@Conditional({LiquibaseDataSourceCondition.class})
@Import({LiquibaseEntityManagerFactoryDependsOnPostProcessor.class, LiquibaseJdbcOperationsDependsOnPostProcessor.class, LiquibaseNamedParameterJdbcOperationsDependsOnPostProcessor.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/liquibase/LiquibaseAutoConfiguration.class */
public class LiquibaseAutoConfiguration {
    @Bean
    public LiquibaseSchemaManagementProvider liquibaseDefaultDdlModeProvider(ObjectProvider<SpringLiquibase> liquibases) {
        return new LiquibaseSchemaManagementProvider(liquibases);
    }

    @ConditionalOnMissingBean({SpringLiquibase.class})
    @EnableConfigurationProperties({DataSourceProperties.class, LiquibaseProperties.class})
    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/liquibase/LiquibaseAutoConfiguration$LiquibaseConfiguration.class */
    public static class LiquibaseConfiguration {
        private final LiquibaseProperties properties;

        public LiquibaseConfiguration(LiquibaseProperties properties) {
            this.properties = properties;
        }

        @Bean
        public SpringLiquibase liquibase(DataSourceProperties dataSourceProperties, ObjectProvider<DataSource> dataSource, @LiquibaseDataSource ObjectProvider<DataSource> liquibaseDataSource) {
            SpringLiquibase liquibase = createSpringLiquibase(liquibaseDataSource.getIfAvailable(), dataSource.getIfUnique(), dataSourceProperties);
            liquibase.setChangeLog(this.properties.getChangeLog());
            liquibase.setClearCheckSums(this.properties.isClearChecksums());
            liquibase.setContexts(this.properties.getContexts());
            liquibase.setDefaultSchema(this.properties.getDefaultSchema());
            liquibase.setLiquibaseSchema(this.properties.getLiquibaseSchema());
            liquibase.setLiquibaseTablespace(this.properties.getLiquibaseTablespace());
            liquibase.setDatabaseChangeLogTable(this.properties.getDatabaseChangeLogTable());
            liquibase.setDatabaseChangeLogLockTable(this.properties.getDatabaseChangeLogLockTable());
            liquibase.setDropFirst(this.properties.isDropFirst());
            liquibase.setShouldRun(this.properties.isEnabled());
            liquibase.setLabels(this.properties.getLabels());
            liquibase.setChangeLogParameters(this.properties.getParameters());
            liquibase.setRollbackFile(this.properties.getRollbackFile());
            liquibase.setTestRollbackOnUpdate(this.properties.isTestRollbackOnUpdate());
            liquibase.setTag(this.properties.getTag());
            return liquibase;
        }

        private SpringLiquibase createSpringLiquibase(DataSource liquibaseDatasource, DataSource dataSource, DataSourceProperties dataSourceProperties) {
            DataSource liquibaseDataSource = getDataSource(liquibaseDatasource, dataSource);
            if (liquibaseDataSource != null) {
                SpringLiquibase liquibase = new SpringLiquibase();
                liquibase.setDataSource(liquibaseDataSource);
                return liquibase;
            }
            SpringLiquibase liquibase2 = new DataSourceClosingSpringLiquibase();
            liquibase2.setDataSource(createNewDataSource(dataSourceProperties));
            return liquibase2;
        }

        private DataSource getDataSource(DataSource liquibaseDataSource, DataSource dataSource) {
            if (liquibaseDataSource != null) {
                return liquibaseDataSource;
            }
            if (this.properties.getUrl() == null && this.properties.getUser() == null) {
                return dataSource;
            }
            return null;
        }

        private DataSource createNewDataSource(DataSourceProperties dataSourceProperties) {
            LiquibaseProperties liquibaseProperties = this.properties;
            liquibaseProperties.getClass();
            Supplier<String> supplier = this::getUrl;
            dataSourceProperties.getClass();
            String url = getProperty(supplier, this::determineUrl);
            LiquibaseProperties liquibaseProperties2 = this.properties;
            liquibaseProperties2.getClass();
            Supplier<String> supplier2 = this::getUser;
            dataSourceProperties.getClass();
            String user = getProperty(supplier2, this::determineUsername);
            LiquibaseProperties liquibaseProperties3 = this.properties;
            liquibaseProperties3.getClass();
            Supplier<String> supplier3 = this::getPassword;
            dataSourceProperties.getClass();
            String password = getProperty(supplier3, this::determinePassword);
            return DataSourceBuilder.create().type(determineDataSourceType()).url(url).username(user).password(password).build();
        }

        private Class<? extends DataSource> determineDataSourceType() {
            Class<? extends DataSource> type = DataSourceBuilder.findType(null);
            return type != null ? type : SimpleDriverDataSource.class;
        }

        private String getProperty(Supplier<String> property, Supplier<String> defaultValue) {
            String value = property.get();
            return value != null ? value : defaultValue.get();
        }
    }

    @ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class})
    @ConditionalOnBean({AbstractEntityManagerFactoryBean.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/liquibase/LiquibaseAutoConfiguration$LiquibaseEntityManagerFactoryDependsOnPostProcessor.class */
    static class LiquibaseEntityManagerFactoryDependsOnPostProcessor extends EntityManagerFactoryDependsOnPostProcessor {
        LiquibaseEntityManagerFactoryDependsOnPostProcessor() {
            super(SpringLiquibase.class);
        }
    }

    @ConditionalOnClass({JdbcOperations.class})
    @ConditionalOnBean({JdbcOperations.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/liquibase/LiquibaseAutoConfiguration$LiquibaseJdbcOperationsDependsOnPostProcessor.class */
    static class LiquibaseJdbcOperationsDependsOnPostProcessor extends JdbcOperationsDependsOnPostProcessor {
        LiquibaseJdbcOperationsDependsOnPostProcessor() {
            super(SpringLiquibase.class);
        }
    }

    @ConditionalOnClass({NamedParameterJdbcOperations.class})
    @ConditionalOnBean({NamedParameterJdbcOperations.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/liquibase/LiquibaseAutoConfiguration$LiquibaseNamedParameterJdbcOperationsDependsOnPostProcessor.class */
    static class LiquibaseNamedParameterJdbcOperationsDependsOnPostProcessor extends NamedParameterJdbcOperationsDependsOnPostProcessor {
        LiquibaseNamedParameterJdbcOperationsDependsOnPostProcessor() {
            super(SpringLiquibase.class);
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/liquibase/LiquibaseAutoConfiguration$LiquibaseDataSourceCondition.class */
    static final class LiquibaseDataSourceCondition extends AnyNestedCondition {
        LiquibaseDataSourceCondition() {
            super(ConfigurationCondition.ConfigurationPhase.REGISTER_BEAN);
        }

        @ConditionalOnBean({DataSource.class})
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/liquibase/LiquibaseAutoConfiguration$LiquibaseDataSourceCondition$DataSourceBeanCondition.class */
        private static final class DataSourceBeanCondition {
            private DataSourceBeanCondition() {
            }
        }

        @ConditionalOnProperty(prefix = "spring.liquibase", name = {"url"}, matchIfMissing = false)
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/liquibase/LiquibaseAutoConfiguration$LiquibaseDataSourceCondition$LiquibaseUrlCondition.class */
        private static final class LiquibaseUrlCondition {
            private LiquibaseUrlCondition() {
            }
        }
    }
}
