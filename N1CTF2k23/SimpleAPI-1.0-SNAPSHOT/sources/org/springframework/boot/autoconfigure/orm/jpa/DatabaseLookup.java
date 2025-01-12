package org.springframework.boot.autoconfigure.orm.jpa;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.orm.jpa.vendor.Database;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/DatabaseLookup.class */
final class DatabaseLookup {
    private static final Log logger = LogFactory.getLog(DatabaseLookup.class);
    private static final Map<DatabaseDriver, Database> LOOKUP;

    static {
        Map<DatabaseDriver, Database> map = new EnumMap<>(DatabaseDriver.class);
        map.put(DatabaseDriver.DERBY, Database.DERBY);
        map.put(DatabaseDriver.H2, Database.H2);
        map.put(DatabaseDriver.HSQLDB, Database.HSQL);
        map.put(DatabaseDriver.MYSQL, Database.MYSQL);
        map.put(DatabaseDriver.ORACLE, Database.ORACLE);
        map.put(DatabaseDriver.POSTGRESQL, Database.POSTGRESQL);
        map.put(DatabaseDriver.SQLSERVER, Database.SQL_SERVER);
        map.put(DatabaseDriver.DB2, Database.DB2);
        map.put(DatabaseDriver.INFORMIX, Database.INFORMIX);
        map.put(DatabaseDriver.HANA, Database.HANA);
        LOOKUP = Collections.unmodifiableMap(map);
    }

    private DatabaseLookup() {
    }

    static Database getDatabase(DataSource dataSource) {
        if (dataSource == null) {
            return Database.DEFAULT;
        }
        try {
            String url = (String) JdbcUtils.extractDatabaseMetaData(dataSource, "getURL");
            DatabaseDriver driver = DatabaseDriver.fromJdbcUrl(url);
            Database database = LOOKUP.get(driver);
            if (database != null) {
                return database;
            }
        } catch (MetaDataAccessException e) {
            logger.warn("Unable to determine jdbc url from datasource", e);
        }
        return Database.DEFAULT;
    }
}
