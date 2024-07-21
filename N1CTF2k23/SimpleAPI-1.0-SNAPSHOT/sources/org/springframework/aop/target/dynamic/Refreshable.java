package org.springframework.aop.target.dynamic;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/target/dynamic/Refreshable.class */
public interface Refreshable {
    void refresh();

    long getRefreshCount();

    long getLastRefreshTime();
}
