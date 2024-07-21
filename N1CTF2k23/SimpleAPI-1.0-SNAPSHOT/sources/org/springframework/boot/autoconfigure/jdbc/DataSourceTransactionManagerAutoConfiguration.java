package org.springframework.boot.autoconfigure.jdbc;

import javax.sql.DataSource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
@EnableConfigurationProperties({DataSourceProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({JdbcTemplate.class, PlatformTransactionManager.class})
@AutoConfigureOrder(Integer.MAX_VALUE)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceTransactionManagerAutoConfiguration.class */
public class DataSourceTransactionManagerAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnSingleCandidate(DataSource.class)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/DataSourceTransactionManagerAutoConfiguration$DataSourceTransactionManagerConfiguration.class */
    static class DataSourceTransactionManagerConfiguration {
        DataSourceTransactionManagerConfiguration() {
        }

        @ConditionalOnMissingBean({PlatformTransactionManager.class})
        @Bean
        DataSourceTransactionManager transactionManager(DataSource dataSource, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
            DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
            transactionManagerCustomizers.ifAvailable(customizers -> {
                customizers.customize(transactionManager);
            });
            return transactionManager;
        }
    }
}
