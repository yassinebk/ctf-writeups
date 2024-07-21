package org.springframework.core;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/Ordered.class */
public interface Ordered {
    public static final int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;
    public static final int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    int getOrder();
}
