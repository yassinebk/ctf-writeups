package org.springframework.boot.autoconfigure.transaction.jta;

import com.atomikos.icatch.config.UserTransactionService;
import com.atomikos.icatch.config.UserTransactionServiceImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import java.io.File;
import java.util.Properties;
import javax.jms.Message;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import org.apache.coyote.http11.Constants;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.XADataSourceWrapper;
import org.springframework.boot.jms.XAConnectionFactoryWrapper;
import org.springframework.boot.jta.atomikos.AtomikosDependsOnBeanFactoryPostProcessor;
import org.springframework.boot.jta.atomikos.AtomikosProperties;
import org.springframework.boot.jta.atomikos.AtomikosXAConnectionFactoryWrapper;
import org.springframework.boot.jta.atomikos.AtomikosXADataSourceWrapper;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.StringUtils;
@EnableConfigurationProperties({AtomikosProperties.class, JtaProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({JtaTransactionManager.class, UserTransactionManager.class})
@ConditionalOnMissingBean({PlatformTransactionManager.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/transaction/jta/AtomikosJtaConfiguration.class */
class AtomikosJtaConfiguration {
    AtomikosJtaConfiguration() {
    }

    @ConditionalOnMissingBean({UserTransactionService.class})
    @Bean(initMethod = "init", destroyMethod = "shutdownWait")
    UserTransactionServiceImp userTransactionService(AtomikosProperties atomikosProperties, JtaProperties jtaProperties) {
        Properties properties = new Properties();
        if (StringUtils.hasText(jtaProperties.getTransactionManagerId())) {
            properties.setProperty("com.atomikos.icatch.tm_unique_name", jtaProperties.getTransactionManagerId());
        }
        properties.setProperty("com.atomikos.icatch.log_base_dir", getLogBaseDir(jtaProperties));
        properties.putAll(atomikosProperties.asProperties());
        return new UserTransactionServiceImp(properties);
    }

    private String getLogBaseDir(JtaProperties jtaProperties) {
        if (StringUtils.hasLength(jtaProperties.getLogDir())) {
            return jtaProperties.getLogDir();
        }
        File home = new ApplicationHome().getDir();
        return new File(home, "transaction-logs").getAbsolutePath();
    }

    @ConditionalOnMissingBean({TransactionManager.class})
    @Bean(initMethod = "init", destroyMethod = Constants.CLOSE)
    UserTransactionManager atomikosTransactionManager(UserTransactionService userTransactionService) throws Exception {
        UserTransactionManager manager = new UserTransactionManager();
        manager.setStartupTransactionService(false);
        manager.setForceShutdown(true);
        return manager;
    }

    @ConditionalOnMissingBean({XADataSourceWrapper.class})
    @Bean
    AtomikosXADataSourceWrapper xaDataSourceWrapper() {
        return new AtomikosXADataSourceWrapper();
    }

    @ConditionalOnMissingBean
    @Bean
    static AtomikosDependsOnBeanFactoryPostProcessor atomikosDependsOnBeanFactoryPostProcessor() {
        return new AtomikosDependsOnBeanFactoryPostProcessor();
    }

    @Bean
    JtaTransactionManager transactionManager(UserTransaction userTransaction, TransactionManager transactionManager, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager(userTransaction, transactionManager);
        transactionManagerCustomizers.ifAvailable(customizers -> {
            customizers.customize(jtaTransactionManager);
        });
        return jtaTransactionManager;
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({Message.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/transaction/jta/AtomikosJtaConfiguration$AtomikosJtaJmsConfiguration.class */
    static class AtomikosJtaJmsConfiguration {
        AtomikosJtaJmsConfiguration() {
        }

        @ConditionalOnMissingBean({XAConnectionFactoryWrapper.class})
        @Bean
        AtomikosXAConnectionFactoryWrapper xaConnectionFactoryWrapper() {
            return new AtomikosXAConnectionFactoryWrapper();
        }
    }
}
