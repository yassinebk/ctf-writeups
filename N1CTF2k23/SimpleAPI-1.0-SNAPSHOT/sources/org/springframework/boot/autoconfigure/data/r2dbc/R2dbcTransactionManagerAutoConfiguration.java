package org.springframework.boot.autoconfigure.data.r2dbc;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.connectionfactory.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
@AutoConfigureBefore({TransactionAutoConfiguration.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({R2dbcTransactionManager.class, ReactiveTransactionManager.class})
@ConditionalOnSingleCandidate(ConnectionFactory.class)
@AutoConfigureOrder(Integer.MAX_VALUE)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/r2dbc/R2dbcTransactionManagerAutoConfiguration.class */
public class R2dbcTransactionManagerAutoConfiguration {
    @ConditionalOnMissingBean({ReactiveTransactionManager.class})
    @Bean
    public R2dbcTransactionManager connectionFactoryTransactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }
}
