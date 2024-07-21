package org.springframework.boot.autoconfigure.jdbc;

import ch.qos.logback.classic.ClassicConstants;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.transaction.TransactionManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.boot.jdbc.XADataSourceWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
@AutoConfigureBefore({DataSourceAutoConfiguration.class})
@EnableConfigurationProperties({DataSourceProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({DataSource.class, TransactionManager.class, EmbeddedDatabaseType.class})
@ConditionalOnMissingBean({DataSource.class})
@ConditionalOnBean({XADataSourceWrapper.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jdbc/XADataSourceAutoConfiguration.class */
public class XADataSourceAutoConfiguration implements BeanClassLoaderAware {
    private ClassLoader classLoader;

    @Bean
    public DataSource dataSource(XADataSourceWrapper wrapper, DataSourceProperties properties, ObjectProvider<XADataSource> xaDataSource) throws Exception {
        return wrapper.mo1284wrapDataSource(xaDataSource.getIfAvailable(() -> {
            return createXaDataSource(properties);
        }));
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    private XADataSource createXaDataSource(DataSourceProperties properties) {
        String className = properties.getXa().getDataSourceClassName();
        if (!StringUtils.hasLength(className)) {
            className = DatabaseDriver.fromJdbcUrl(properties.determineUrl()).getXaDataSourceClassName();
        }
        Assert.state(StringUtils.hasLength(className), "No XA DataSource class name specified");
        XADataSource dataSource = createXaDataSourceInstance(className);
        bindXaProperties(dataSource, properties);
        return dataSource;
    }

    private XADataSource createXaDataSourceInstance(String className) {
        try {
            Class<?> dataSourceClass = ClassUtils.forName(className, this.classLoader);
            Object instance = BeanUtils.instantiateClass(dataSourceClass);
            Assert.isInstanceOf(XADataSource.class, instance);
            return (XADataSource) instance;
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create XADataSource instance from '" + className + "'");
        }
    }

    private void bindXaProperties(XADataSource target, DataSourceProperties dataSourceProperties) {
        Binder binder = new Binder(getBinderSource(dataSourceProperties));
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(target));
    }

    private ConfigurationPropertySource getBinderSource(DataSourceProperties dataSourceProperties) {
        MapConfigurationPropertySource source = new MapConfigurationPropertySource();
        source.put(ClassicConstants.USER_MDC_KEY, dataSourceProperties.determineUsername());
        source.put("password", dataSourceProperties.determinePassword());
        source.put("url", dataSourceProperties.determineUrl());
        source.putAll(dataSourceProperties.getXa().getProperties());
        ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
        aliases.addAliases(ClassicConstants.USER_MDC_KEY, "username");
        return source.withAliases(aliases);
    }
}
