package org.springframework.boot.jdbc;

import javax.sql.DataSource;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/jdbc/SchemaManagementProvider.class */
public interface SchemaManagementProvider {
    SchemaManagement getSchemaManagement(DataSource dataSource);
}
