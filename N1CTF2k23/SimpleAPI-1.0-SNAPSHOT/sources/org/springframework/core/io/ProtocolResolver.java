package org.springframework.core.io;

import org.springframework.lang.Nullable;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/io/ProtocolResolver.class */
public interface ProtocolResolver {
    @Nullable
    Resource resolve(String str, ResourceLoader resourceLoader);
}
