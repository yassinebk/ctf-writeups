package org.springframework.boot.autoconfigure.transaction.jta;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJndi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.config.JtaTransactionManagerFactoryBean;
import org.springframework.transaction.jta.JtaTransactionManager;
@ConditionalOnJndi({"java:comp/UserTransaction", "java:comp/TransactionManager", "java:appserver/TransactionManager", "java:pm/TransactionManager", "java:/TransactionManager"})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({JtaTransactionManager.class})
@ConditionalOnMissingBean({PlatformTransactionManager.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/transaction/jta/JndiJtaConfiguration.class */
class JndiJtaConfiguration {
    JndiJtaConfiguration() {
    }

    @Bean
    JtaTransactionManager transactionManager(ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManagerFactoryBean().getObject();
        transactionManagerCustomizers.ifAvailable(customizers -> {
            customizers.customize(jtaTransactionManager);
        });
        return jtaTransactionManager;
    }
}
