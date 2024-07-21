package org.springframework.boot.autoconfigure.data.neo4j;

import java.util.List;
import java.util.stream.Stream;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.session.event.EventListener;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.data.neo4j.web.support.OpenSessionInViewInterceptor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@EnableConfigurationProperties({Neo4jProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({SessionFactory.class, Neo4jTransactionManager.class, PlatformTransactionManager.class})
@Import({Neo4jBookmarkManagementConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/neo4j/Neo4jDataAutoConfiguration.class */
public class Neo4jDataAutoConfiguration {
    @ConditionalOnMissingBean({PlatformTransactionManager.class})
    @Bean
    public Neo4jTransactionManager transactionManager(SessionFactory sessionFactory, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers) {
        Neo4jTransactionManager transactionManager = new Neo4jTransactionManager(sessionFactory);
        transactionManagerCustomizers.ifAvailable(customizers -> {
            customizers.customize(transactionManager);
        });
        return transactionManager;
    }

    @ConditionalOnMissingBean({SessionFactory.class})
    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/neo4j/Neo4jDataAutoConfiguration$Neo4jOgmSessionFactoryConfiguration.class */
    static class Neo4jOgmSessionFactoryConfiguration {
        Neo4jOgmSessionFactoryConfiguration() {
        }

        @ConditionalOnMissingBean
        @Bean
        org.neo4j.ogm.config.Configuration configuration(Neo4jProperties properties) {
            return properties.createConfiguration();
        }

        @Bean
        SessionFactory sessionFactory(org.neo4j.ogm.config.Configuration configuration, BeanFactory beanFactory, ObjectProvider<EventListener> eventListeners) {
            SessionFactory sessionFactory = new SessionFactory(configuration, getPackagesToScan(beanFactory));
            Stream<EventListener> orderedStream = eventListeners.orderedStream();
            sessionFactory.getClass();
            orderedStream.forEach(this::register);
            return sessionFactory;
        }

        private String[] getPackagesToScan(BeanFactory beanFactory) {
            List<String> packages = EntityScanPackages.get(beanFactory).getPackageNames();
            if (packages.isEmpty() && AutoConfigurationPackages.has(beanFactory)) {
                packages = AutoConfigurationPackages.get(beanFactory);
            }
            return StringUtils.toStringArray(packages);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({WebMvcConfigurer.class, OpenSessionInViewInterceptor.class})
    @ConditionalOnMissingBean({OpenSessionInViewInterceptor.class})
    @ConditionalOnProperty(prefix = "spring.data.neo4j", name = {"open-in-view"}, havingValue = "true")
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/neo4j/Neo4jDataAutoConfiguration$Neo4jWebConfiguration.class */
    static class Neo4jWebConfiguration {
        Neo4jWebConfiguration() {
        }

        @Bean
        OpenSessionInViewInterceptor neo4jOpenSessionInViewInterceptor() {
            return new OpenSessionInViewInterceptor();
        }

        @Bean
        WebMvcConfigurer neo4jOpenSessionInViewInterceptorConfigurer(final OpenSessionInViewInterceptor interceptor) {
            return new WebMvcConfigurer() { // from class: org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration.Neo4jWebConfiguration.1
                @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurer
                public void addInterceptors(InterceptorRegistry registry) {
                    registry.addWebRequestInterceptor(interceptor);
                }
            };
        }
    }
}
