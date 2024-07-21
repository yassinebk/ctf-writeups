package org.springframework.util;

import org.springframework.lang.Nullable;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/StringValueResolver.class */
public interface StringValueResolver {
    @Nullable
    String resolveStringValue(String str);
}
