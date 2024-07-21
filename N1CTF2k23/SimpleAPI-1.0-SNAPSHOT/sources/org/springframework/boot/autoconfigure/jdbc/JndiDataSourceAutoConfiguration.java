package org.springframework.boot.autoconfigure.jdbc;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.JmxUtils;
@AutoConfigureBefore({XADataSourceAutoConfiguration.class, DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({DataSourceProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({DataSource.class, EmbeddedDatabaseType.class})
@ConditionalOnProperty(prefix = "spring.datasource", name = {"jndi-name"})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/JndiDataSourceAutoConfiguration.class */
public class JndiDataSourceAutoConfiguration {
    @ConditionalOnMissingBean
    @Bean(destroyMethod = "")
    public DataSource dataSource(DataSourceProperties properties, ApplicationContext context) {
        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
        DataSource dataSource = dataSourceLookup.getDataSource(properties.getJndiName());
        excludeMBeanIfNecessary(dataSource, "dataSource", context);
        return dataSource;
    }

    private void excludeMBeanIfNecessary(Object candidate, String beanName, ApplicationContext context) {
        for (MBeanExporter mbeanExporter : context.getBeansOfType(MBeanExporter.class).values()) {
            if (JmxUtils.isMBean(candidate.getClass())) {
                mbeanExporter.addExcludedBean(beanName);
            }
        }
    }
}
