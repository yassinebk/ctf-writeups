package org.springframework.boot.jdbc;

import javax.sql.DataSource;
import javax.sql.XADataSource;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/jdbc/XADataSourceWrapper.class */
public interface XADataSourceWrapper {
    /* renamed from: wrapDataSource */
    DataSource mo1284wrapDataSource(XADataSource dataSource) throws Exception;
}
