package org.springframework.boot.autoconfigure.r2dbc;

import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/r2dbc/EmbeddedDatabaseConnection.class */
public enum EmbeddedDatabaseConnection {
    NONE(null, null, null),
    H2("H2", "io.r2dbc.h2.H2ConnectionFactoryProvider", "r2dbc:h2:mem://in-memory/%s?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
    
    private final String type;
    private final String driverClassName;
    private final String url;

    EmbeddedDatabaseConnection(String type, String driverClassName, String url) {
        this.type = type;
        this.driverClassName = driverClassName;
        this.url = url;
    }

    public String getDriverClassName() {
        return this.driverClassName;
    }

    public String getType() {
        return this.type;
    }

    public String getUrl(String databaseName) {
        Assert.hasText(databaseName, "DatabaseName must not be empty");
        if (this.url != null) {
            return String.format(this.url, databaseName);
        }
        return null;
    }

    public static EmbeddedDatabaseConnection get(ClassLoader classLoader) {
        EmbeddedDatabaseConnection[] values;
        for (EmbeddedDatabaseConnection candidate : values()) {
            if (candidate != NONE && ClassUtils.isPresent(candidate.getDriverClassName(), classLoader)) {
                return candidate;
            }
        }
        return NONE;
    }
}
