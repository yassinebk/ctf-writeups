package org.springframework.boot.autoconfigure.jdbc.metadata;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.boot.jdbc.metadata.CommonsDbcp2DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.boot.jdbc.metadata.HikariDataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.TomcatDataSourcePoolMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration(proxyBeanMethods = false)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/DataSourcePoolMetadataProvidersConfiguration.class */
public class DataSourcePoolMetadataProvidersConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({DataSource.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/DataSourcePoolMetadataProvidersConfiguration$TomcatDataSourcePoolMetadataProviderConfiguration.class */
    static class TomcatDataSourcePoolMetadataProviderConfiguration {
        TomcatDataSourcePoolMetadataProviderConfiguration() {
        }

        @Bean
        DataSourcePoolMetadataProvider tomcatPoolDataSourceMetadataProvider() {
            return dataSource -> {
                DataSource tomcatDataSource = (DataSource) DataSourceUnwrapper.unwrap(dataSource, DataSource.class);
                if (tomcatDataSource != null) {
                    return new TomcatDataSourcePoolMetadata(tomcatDataSource);
                }
                return null;
            };
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({HikariDataSource.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/DataSourcePoolMetadataProvidersConfiguration$HikariPoolDataSourceMetadataProviderConfiguration.class */
    static class HikariPoolDataSourceMetadataProviderConfiguration {
        HikariPoolDataSourceMetadataProviderConfiguration() {
        }

        @Bean
        DataSourcePoolMetadataProvider hikariPoolDataSourceMetadataProvider() {
            return dataSource -> {
                HikariDataSource hikariDataSource = (HikariDataSource) DataSourceUnwrapper.unwrap(dataSource, HikariDataSource.class);
                if (hikariDataSource != null) {
                    return new HikariDataSourcePoolMetadata(hikariDataSource);
                }
                return null;
            };
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({BasicDataSource.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/metadata/DataSourcePoolMetadataProvidersConfiguration$CommonsDbcp2PoolDataSourceMetadataProviderConfiguration.class */
    static class CommonsDbcp2PoolDataSourceMetadataProviderConfiguration {
        CommonsDbcp2PoolDataSourceMetadataProviderConfiguration() {
        }

        @Bean
        DataSourcePoolMetadataProvider commonsDbcp2PoolDataSourceMetadataProvider() {
            return dataSource -> {
                BasicDataSource dbcpDataSource = (BasicDataSource) DataSourceUnwrapper.unwrap(dataSource, BasicDataSource.class);
                if (dbcpDataSource != null) {
                    return new CommonsDbcp2DataSourcePoolMetadata(dbcpDataSource);
                }
                return null;
            };
        }
    }
}
