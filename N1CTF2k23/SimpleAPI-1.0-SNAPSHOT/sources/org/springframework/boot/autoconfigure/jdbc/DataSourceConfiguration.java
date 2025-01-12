package org.springframework.boot.autoconfigure.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceConfiguration.class */
abstract class DataSourceConfiguration {
    DataSourceConfiguration() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected static <T> T createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({org.apache.tomcat.jdbc.pool.DataSource.class})
    @ConditionalOnMissingBean({DataSource.class})
    @ConditionalOnProperty(name = {"spring.datasource.type"}, havingValue = "org.apache.tomcat.jdbc.pool.DataSource", matchIfMissing = true)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceConfiguration$Tomcat.class */
    static class Tomcat {
        Tomcat() {
        }

        @ConfigurationProperties(prefix = "spring.datasource.tomcat")
        @Bean
        org.apache.tomcat.jdbc.pool.DataSource dataSource(DataSourceProperties properties) {
            org.apache.tomcat.jdbc.pool.DataSource dataSource = (org.apache.tomcat.jdbc.pool.DataSource) DataSourceConfiguration.createDataSource(properties, org.apache.tomcat.jdbc.pool.DataSource.class);
            DatabaseDriver databaseDriver = DatabaseDriver.fromJdbcUrl(properties.determineUrl());
            String validationQuery = databaseDriver.getValidationQuery();
            if (validationQuery != null) {
                dataSource.setTestOnBorrow(true);
                dataSource.setValidationQuery(validationQuery);
            }
            return dataSource;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({HikariDataSource.class})
    @ConditionalOnMissingBean({DataSource.class})
    @ConditionalOnProperty(name = {"spring.datasource.type"}, havingValue = "com.zaxxer.hikari.HikariDataSource", matchIfMissing = true)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceConfiguration$Hikari.class */
    static class Hikari {
        Hikari() {
        }

        @ConfigurationProperties(prefix = "spring.datasource.hikari")
        @Bean
        HikariDataSource dataSource(DataSourceProperties properties) {
            HikariDataSource dataSource = (HikariDataSource) DataSourceConfiguration.createDataSource(properties, HikariDataSource.class);
            if (StringUtils.hasText(properties.getName())) {
                dataSource.setPoolName(properties.getName());
            }
            return dataSource;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({BasicDataSource.class})
    @ConditionalOnMissingBean({DataSource.class})
    @ConditionalOnProperty(name = {"spring.datasource.type"}, havingValue = "org.apache.commons.dbcp2.BasicDataSource", matchIfMissing = true)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceConfiguration$Dbcp2.class */
    static class Dbcp2 {
        Dbcp2() {
        }

        @ConfigurationProperties(prefix = "spring.datasource.dbcp2")
        @Bean
        BasicDataSource dataSource(DataSourceProperties properties) {
            return (BasicDataSource) DataSourceConfiguration.createDataSource(properties, BasicDataSource.class);
        }
    }

    @ConditionalOnMissingBean({DataSource.class})
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = {"spring.datasource.type"})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceConfiguration$Generic.class */
    static class Generic {
        Generic() {
        }

        /* JADX WARN: Type inference failed for: r0v2, types: [javax.sql.DataSource] */
        @Bean
        DataSource dataSource(DataSourceProperties properties) {
            return properties.initializeDataSourceBuilder().build();
        }
    }
}
