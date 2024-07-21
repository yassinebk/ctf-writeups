package org.springframework.boot.autoconfigure.transaction;

import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.aop.config.AopNamespaceUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.jta.JtaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.AbstractTransactionManagementConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.transaction.support.TransactionOperations;
import org.springframework.transaction.support.TransactionTemplate;
@EnableConfigurationProperties({TransactionProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({PlatformTransactionManager.class})
@AutoConfigureAfter({JtaAutoConfiguration.class, HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, Neo4jDataAutoConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/transaction/TransactionAutoConfiguration.class */
public class TransactionAutoConfiguration {

    @ConditionalOnMissingBean({AbstractTransactionManagementConfiguration.class})
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnBean({TransactionManager.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/transaction/TransactionAutoConfiguration$EnableTransactionManagementConfiguration.class */
    public static class EnableTransactionManagementConfiguration {

        @Configuration(proxyBeanMethods = false)
        @EnableTransactionManagement(proxyTargetClass = true)
        @ConditionalOnProperty(prefix = "spring.aop", name = {AopNamespaceUtils.PROXY_TARGET_CLASS_ATTRIBUTE}, havingValue = "true", matchIfMissing = true)
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/transaction/TransactionAutoConfiguration$EnableTransactionManagementConfiguration$CglibAutoProxyConfiguration.class */
        public static class CglibAutoProxyConfiguration {
        }

        @Configuration(proxyBeanMethods = false)
        @EnableTransactionManagement(proxyTargetClass = false)
        @ConditionalOnProperty(prefix = "spring.aop", name = {AopNamespaceUtils.PROXY_TARGET_CLASS_ATTRIBUTE}, havingValue = "false", matchIfMissing = false)
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/transaction/TransactionAutoConfiguration$EnableTransactionManagementConfiguration$JdkDynamicAutoProxyConfiguration.class */
        public static class JdkDynamicAutoProxyConfiguration {
        }
    }

    @ConditionalOnMissingBean
    @Bean
    public TransactionManagerCustomizers platformTransactionManagerCustomizers(ObjectProvider<PlatformTransactionManagerCustomizer<?>> customizers) {
        return new TransactionManagerCustomizers((Collection) customizers.orderedStream().collect(Collectors.toList()));
    }

    @ConditionalOnMissingBean
    @Bean
    @ConditionalOnSingleCandidate(ReactiveTransactionManager.class)
    public TransactionalOperator transactionalOperator(ReactiveTransactionManager transactionManager) {
        return TransactionalOperator.create(transactionManager);
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnSingleCandidate(PlatformTransactionManager.class)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/transaction/TransactionAutoConfiguration$TransactionTemplateConfiguration.class */
    public static class TransactionTemplateConfiguration {
        @ConditionalOnMissingBean({TransactionOperations.class})
        @Bean
        public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
            return new TransactionTemplate(transactionManager);
        }
    }
}
