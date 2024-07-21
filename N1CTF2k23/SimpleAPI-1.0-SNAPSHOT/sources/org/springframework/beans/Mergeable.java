package org.springframework.beans;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/Mergeable.class */
public interface Mergeable {
    boolean isMergeEnabled();

    Object merge(@Nullable Object obj);
}
