package org.springframework.boot.jdbc.metadata;

import javax.sql.DataSource;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/jdbc/metadata/DataSourcePoolMetadataProvider.class */
public interface DataSourcePoolMetadataProvider {
    DataSourcePoolMetadata getDataSourcePoolMetadata(DataSource dataSource);
}
