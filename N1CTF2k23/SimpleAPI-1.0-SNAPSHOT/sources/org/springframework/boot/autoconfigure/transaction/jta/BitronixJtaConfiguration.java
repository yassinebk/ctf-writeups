package org.springframework.boot.autoconfigure.transaction.jta;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.jndi.BitronixContext;
import java.io.File;
import javax.jms.Message;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.XADataSourceWrapper;
import org.springframework.boot.jms.XAConnectionFactoryWrapper;
import org.springframework.boot.jta.bitronix.BitronixDependentBeanFactoryPostProcessor;
import org.springframework.boot.jta.bitronix.BitronixXAConnectionFactoryWrapper;
import org.springframework.boot.jta.bitronix.BitronixXADataSourceWrapper;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.StringUtils;
@EnableConfigurationProperties({JtaProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({JtaTransactionManager.class, BitronixContext.class})
@Deprecated
@ConditionalOnMissingBean({PlatformTransactionManager.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/transaction/jta/BitronixJtaConfiguration.class */
class BitronixJtaConfiguration {
    BitronixJtaConfiguration() {
    }

    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix = "spring.jta.bitronix.properties")
    @Bean
    bitronix.tm.Configuration bitronixConfiguration(JtaProperties jtaProperties) {
        bitronix.tm.Configuration config = TransactionManagerServices.getConfiguration();
        if (StringUtils.hasText(jtaProperties.getTransactionManagerId())) {
            config.setServerId(jtaProperties.getTransactionManagerId());
        }
        File logBaseDir = getLogBaseDir(jtaProperties);
        config.setLogPart1Filename(new File(logBaseDir, "part1.btm").getAbsolutePath());
        config.setLogPart2Filename(new File(logBaseDir, "part2.btm").getAbsolutePath());
        config.setDisableJmx(true);
        return config;
    }

    private File getLogBaseDir(JtaProperties jtaProperties) {
        if (StringUtils.hasLength(jtaProperties.getLogDir())) {
            return new File(jtaProperties.getLogDir());
        }
        File home = new ApplicationHome().getDir();
        return new File(home, "transaction-logs");
    }

    @ConditionalOnMissingBean({TransactionManager.class})
    @Bean
    BitronixTransactionManager bitronixTransactionManager(bitronix.tm.Configuration configuration) {
        return TransactionManagerServices.getTransactionManager();
    }

    @ConditionalOnMissingBean({XADataSourceWrapper.class})
    @Bean
    BitronixXADataSourceWrapper xaDataSourceWrapper() {
        return new BitronixXADataSourceWrapper();
    }

    @ConditionalOnMissingBean
    @Bean
    static BitronixDependentBeanFactoryPostProcessor bitronixDependentBeanFactoryPostProcessor() {
        return new BitronixDependentBeanFactoryPostProcessor();
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
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/transaction/jta/BitronixJtaConfiguration$BitronixJtaJmsConfiguration.class */
    static class BitronixJtaJmsConfiguration {
        BitronixJtaJmsConfiguration() {
        }

        @ConditionalOnMissingBean({XAConnectionFactoryWrapper.class})
        @Bean
        BitronixXAConnectionFactoryWrapper xaConnectionFactoryWrapper() {
            return new BitronixXAConnectionFactoryWrapper();
        }
    }
}
