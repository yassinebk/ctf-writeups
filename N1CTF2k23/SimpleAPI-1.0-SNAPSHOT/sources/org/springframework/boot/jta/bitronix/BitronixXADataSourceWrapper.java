package org.springframework.boot.jta.bitronix;

import javax.sql.XADataSource;
import org.springframework.boot.jdbc.XADataSourceWrapper;
@Deprecated
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/jta/bitronix/BitronixXADataSourceWrapper.class */
public class BitronixXADataSourceWrapper implements XADataSourceWrapper {
    @Override // org.springframework.boot.jdbc.XADataSourceWrapper
    /* renamed from: wrapDataSource */
    public PoolingDataSourceBean mo1284wrapDataSource(XADataSource dataSource) throws Exception {
        PoolingDataSourceBean pool = new PoolingDataSourceBean();
        pool.setDataSource(dataSource);
        return pool;
    }
}
