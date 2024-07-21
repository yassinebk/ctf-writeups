package org.springframework.aop.target;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/target/PoolingConfig.class */
public interface PoolingConfig {
    int getMaxSize();

    int getActiveCount() throws UnsupportedOperationException;

    int getIdleCount() throws UnsupportedOperationException;
}
