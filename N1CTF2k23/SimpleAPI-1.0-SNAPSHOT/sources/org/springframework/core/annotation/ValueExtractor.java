package org.springframework.core.annotation;

import java.lang.reflect.Method;
import org.springframework.lang.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/ValueExtractor.class */
public interface ValueExtractor {
    @Nullable
    Object extract(Method method, @Nullable Object obj);
}
