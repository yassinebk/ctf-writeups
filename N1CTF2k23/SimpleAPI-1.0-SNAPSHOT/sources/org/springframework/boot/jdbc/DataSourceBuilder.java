package org.springframework.boot.jdbc;

import ch.qos.logback.classic.ClassicConstants;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.util.ClassUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/jdbc/DataSourceBuilder.class */
public final class DataSourceBuilder<T extends DataSource> {
    private static final String[] DATA_SOURCE_TYPE_NAMES = {"com.zaxxer.hikari.HikariDataSource", "org.apache.tomcat.jdbc.pool.DataSource", "org.apache.commons.dbcp2.BasicDataSource"};
    private Class<? extends DataSource> type;
    private ClassLoader classLoader;
    private Map<String, String> properties = new HashMap();

    public static DataSourceBuilder<?> create() {
        return new DataSourceBuilder<>(null);
    }

    public static DataSourceBuilder<?> create(ClassLoader classLoader) {
        return new DataSourceBuilder<>(classLoader);
    }

    private DataSourceBuilder(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public T build() {
        Class<? extends DataSource> type = getType();
        T t = (T) BeanUtils.instantiateClass(type);
        maybeGetDriverClassName();
        bind(t);
        return t;
    }

    private void maybeGetDriverClassName() {
        if (!this.properties.containsKey("driverClassName") && this.properties.containsKey("url")) {
            String url = this.properties.get("url");
            String driverClass = DatabaseDriver.fromJdbcUrl(url).getDriverClassName();
            this.properties.put("driverClassName", driverClass);
        }
    }

    private void bind(DataSource result) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(this.properties);
        ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();
        aliases.addAliases("driver-class-name", "driver-class");
        aliases.addAliases("url", "jdbc-url");
        aliases.addAliases("username", ClassicConstants.USER_MDC_KEY);
        Binder binder = new Binder(source.withAliases(aliases));
        binder.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(result));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <D extends DataSource> DataSourceBuilder<D> type(Class<D> type) {
        this.type = type;
        return this;
    }

    public DataSourceBuilder<T> url(String url) {
        this.properties.put("url", url);
        return this;
    }

    public DataSourceBuilder<T> driverClassName(String driverClassName) {
        this.properties.put("driverClassName", driverClassName);
        return this;
    }

    public DataSourceBuilder<T> username(String username) {
        this.properties.put("username", username);
        return this;
    }

    public DataSourceBuilder<T> password(String password) {
        this.properties.put("password", password);
        return this;
    }

    public static Class<? extends DataSource> findType(ClassLoader classLoader) {
        String[] strArr;
        for (String name : DATA_SOURCE_TYPE_NAMES) {
            try {
                return ClassUtils.forName(name, classLoader);
            } catch (Exception e) {
            }
        }
        return null;
    }

    private Class<? extends DataSource> getType() {
        Class<? extends DataSource> type = this.type != null ? this.type : findType(this.classLoader);
        if (type != null) {
            return type;
        }
        throw new IllegalStateException("No supported DataSource type found");
    }
}
