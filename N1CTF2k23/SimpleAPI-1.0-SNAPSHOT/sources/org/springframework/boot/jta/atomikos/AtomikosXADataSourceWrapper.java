package org.springframework.boot.jta.atomikos;

import javax.sql.XADataSource;
import org.springframework.boot.jdbc.XADataSourceWrapper;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/jta/atomikos/AtomikosXADataSourceWrapper.class */
public class AtomikosXADataSourceWrapper implements XADataSourceWrapper {
    @Override // org.springframework.boot.jdbc.XADataSourceWrapper
    /* renamed from: wrapDataSource */
    public AtomikosDataSourceBean mo1284wrapDataSource(XADataSource dataSource) throws Exception {
        AtomikosDataSourceBean bean = new AtomikosDataSourceBean();
        bean.setXaDataSource(dataSource);
        return bean;
    }
}
