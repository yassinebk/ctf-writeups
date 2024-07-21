package org.springframework.boot.autoconfigure.orm.jpa;

import java.util.Map;
import java.util.function.Supplier;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceSchemaCreatedEvent;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/DataSourceInitializedPublisher.class */
public class DataSourceInitializedPublisher implements BeanPostProcessor {
    @Autowired
    private ApplicationContext applicationContext;
    private DataSource dataSource;
    private JpaProperties jpaProperties;
    private HibernateProperties hibernateProperties;
    private DataSourceSchemaCreatedPublisher schemaCreatedPublisher;

    DataSourceInitializedPublisher() {
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof LocalContainerEntityManagerFactoryBean) {
            LocalContainerEntityManagerFactoryBean factory = (LocalContainerEntityManagerFactoryBean) bean;
            if (factory.getBootstrapExecutor() != null && factory.getJpaVendorAdapter() != null) {
                this.schemaCreatedPublisher = new DataSourceSchemaCreatedPublisher(factory);
                factory.setJpaVendorAdapter(this.schemaCreatedPublisher);
            }
        }
        return bean;
    }

    @Override // org.springframework.beans.factory.config.BeanPostProcessor
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource) {
            this.dataSource = (DataSource) bean;
        }
        if (bean instanceof JpaProperties) {
            this.jpaProperties = (JpaProperties) bean;
        }
        if (bean instanceof HibernateProperties) {
            this.hibernateProperties = (HibernateProperties) bean;
        }
        if ((bean instanceof LocalContainerEntityManagerFactoryBean) && this.schemaCreatedPublisher == null) {
            LocalContainerEntityManagerFactoryBean factoryBean = (LocalContainerEntityManagerFactoryBean) bean;
            EntityManagerFactory entityManagerFactory = factoryBean.getNativeEntityManagerFactory();
            publishEventIfRequired(factoryBean, entityManagerFactory);
        }
        return bean;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void publishEventIfRequired(LocalContainerEntityManagerFactoryBean factoryBean, EntityManagerFactory entityManagerFactory) {
        DataSource dataSource = findDataSource(factoryBean, entityManagerFactory);
        if (dataSource != null && isInitializingDatabase(dataSource)) {
            this.applicationContext.publishEvent((ApplicationEvent) new DataSourceSchemaCreatedEvent(dataSource));
        }
    }

    private DataSource findDataSource(LocalContainerEntityManagerFactoryBean factoryBean, EntityManagerFactory entityManagerFactory) {
        Object dataSource = entityManagerFactory.getProperties().get("javax.persistence.nonJtaDataSource");
        if (dataSource == null) {
            dataSource = factoryBean.getPersistenceUnitInfo().getNonJtaDataSource();
        }
        return dataSource instanceof DataSource ? (DataSource) dataSource : this.dataSource;
    }

    private boolean isInitializingDatabase(DataSource dataSource) {
        if (this.jpaProperties == null || this.hibernateProperties == null) {
            return true;
        }
        Supplier<String> defaultDdlAuto = () -> {
            return EmbeddedDatabaseConnection.isEmbedded(dataSource) ? "create-drop" : LoggingSystem.NONE;
        };
        Map<String, Object> hibernate = this.hibernateProperties.determineHibernateProperties(this.jpaProperties.getProperties(), new HibernateSettings().ddlAuto(defaultDdlAuto));
        return hibernate.containsKey("hibernate.hbm2ddl.auto");
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/DataSourceInitializedPublisher$Registrar.class */
    static class Registrar implements ImportBeanDefinitionRegistrar {
        private static final String BEAN_NAME = "dataSourceInitializedPublisher";

        Registrar() {
        }

        @Override // org.springframework.context.annotation.ImportBeanDefinitionRegistrar
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            if (!registry.containsBeanDefinition(BEAN_NAME)) {
                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setBeanClass(DataSourceInitializedPublisher.class);
                beanDefinition.setRole(2);
                beanDefinition.setSynthetic(true);
                registry.registerBeanDefinition(BEAN_NAME, beanDefinition);
            }
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/DataSourceInitializedPublisher$DataSourceSchemaCreatedPublisher.class */
    final class DataSourceSchemaCreatedPublisher implements JpaVendorAdapter {
        private final LocalContainerEntityManagerFactoryBean factoryBean;
        private final JpaVendorAdapter delegate;

        private DataSourceSchemaCreatedPublisher(LocalContainerEntityManagerFactoryBean factoryBean) {
            this.factoryBean = factoryBean;
            this.delegate = factoryBean.getJpaVendorAdapter();
        }

        public PersistenceProvider getPersistenceProvider() {
            return this.delegate.getPersistenceProvider();
        }

        public String getPersistenceProviderRootPackage() {
            return this.delegate.getPersistenceProviderRootPackage();
        }

        public Map<String, ?> getJpaPropertyMap(PersistenceUnitInfo pui) {
            return this.delegate.getJpaPropertyMap(pui);
        }

        public Map<String, ?> getJpaPropertyMap() {
            return this.delegate.getJpaPropertyMap();
        }

        public JpaDialect getJpaDialect() {
            return this.delegate.getJpaDialect();
        }

        public Class<? extends EntityManagerFactory> getEntityManagerFactoryInterface() {
            return this.delegate.getEntityManagerFactoryInterface();
        }

        public Class<? extends EntityManager> getEntityManagerInterface() {
            return this.delegate.getEntityManagerInterface();
        }

        public void postProcessEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
            this.delegate.postProcessEntityManagerFactory(entityManagerFactory);
            AsyncTaskExecutor bootstrapExecutor = this.factoryBean.getBootstrapExecutor();
            if (bootstrapExecutor != null) {
                bootstrapExecutor.execute(() -> {
                    DataSourceInitializedPublisher.this.publishEventIfRequired(this.factoryBean, entityManagerFactory);
                });
            }
        }
    }
}
