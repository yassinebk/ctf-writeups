package org.springframework.boot.jdbc;

import java.sql.Wrapper;
import javax.sql.DataSource;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.util.ClassUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/jdbc/DataSourceUnwrapper.class */
public final class DataSourceUnwrapper {
    private static final boolean DELEGATING_DATA_SOURCE_PRESENT = ClassUtils.isPresent("org.springframework.jdbc.datasource.DelegatingDataSource", DataSourceUnwrapper.class.getClassLoader());

    private DataSourceUnwrapper() {
    }

    public static <T> T unwrap(DataSource dataSource, Class<T> target) {
        DataSource targetDataSource;
        if (target.isInstance(dataSource)) {
            return target.cast(dataSource);
        }
        T unwrapped = (T) safeUnwrap(dataSource, target);
        if (unwrapped != null) {
            return unwrapped;
        }
        if (DELEGATING_DATA_SOURCE_PRESENT && (targetDataSource = DelegatingDataSourceUnwrapper.getTargetDataSource(dataSource)) != null) {
            return (T) unwrap(targetDataSource, target);
        }
        if (AopUtils.isAopProxy(dataSource)) {
            Object proxyTarget = AopProxyUtils.getSingletonTarget(dataSource);
            if (proxyTarget instanceof DataSource) {
                return (T) unwrap((DataSource) proxyTarget, target);
            }
            return null;
        }
        return null;
    }

    private static <S> S safeUnwrap(Wrapper wrapper, Class<S> target) {
        try {
            if (wrapper.isWrapperFor(target)) {
                return (S) wrapper.unwrap(target);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/jdbc/DataSourceUnwrapper$DelegatingDataSourceUnwrapper.class */
    public static class DelegatingDataSourceUnwrapper {
        private DelegatingDataSourceUnwrapper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static DataSource getTargetDataSource(DataSource dataSource) {
            if (dataSource instanceof DelegatingDataSource) {
                return ((DelegatingDataSource) dataSource).getTargetDataSource();
            }
            return null;
        }
    }
}
