package org.springframework.boot.autoconfigure.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.jdbc.pool.DataSourceProxy;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.MBeanExporter;
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "spring.jmx", name = {"enabled"}, havingValue = "true", matchIfMissing = true)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceJmxConfiguration.class */
class DataSourceJmxConfiguration {
    private static final Log logger = LogFactory.getLog(DataSourceJmxConfiguration.class);

    DataSourceJmxConfiguration() {
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({HikariDataSource.class})
    @ConditionalOnSingleCandidate(DataSource.class)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceJmxConfiguration$Hikari.class */
    static class Hikari {
        private final DataSource dataSource;
        private final ObjectProvider<MBeanExporter> mBeanExporter;

        Hikari(DataSource dataSource, ObjectProvider<MBeanExporter> mBeanExporter) {
            this.dataSource = dataSource;
            this.mBeanExporter = mBeanExporter;
        }

        @PostConstruct
        void validateMBeans() {
            HikariDataSource hikariDataSource = (HikariDataSource) DataSourceUnwrapper.unwrap(this.dataSource, HikariDataSource.class);
            if (hikariDataSource != null && hikariDataSource.isRegisterMbeans()) {
                this.mBeanExporter.ifUnique(exporter -> {
                    exporter.addExcludedBean("dataSource");
                });
            }
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({DataSourceProxy.class})
    @ConditionalOnSingleCandidate(DataSource.class)
    @ConditionalOnProperty(prefix = "spring.datasource.tomcat", name = {"jmx-enabled"})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceJmxConfiguration$TomcatDataSourceJmxConfiguration.class */
    static class TomcatDataSourceJmxConfiguration {
        TomcatDataSourceJmxConfiguration() {
        }

        @ConditionalOnMissingBean(name = {"dataSourceMBean"})
        @Bean
        Object dataSourceMBean(DataSource dataSource) {
            DataSourceProxy dataSourceProxy = (DataSourceProxy) DataSourceUnwrapper.unwrap(dataSource, DataSourceProxy.class);
            if (dataSourceProxy != null) {
                try {
                    return dataSourceProxy.createPool().getJmxPool();
                } catch (SQLException e) {
                    DataSourceJmxConfiguration.logger.warn("Cannot expose DataSource to JMX (could not connect)");
                    return null;
                }
            }
            return null;
        }
    }
}
