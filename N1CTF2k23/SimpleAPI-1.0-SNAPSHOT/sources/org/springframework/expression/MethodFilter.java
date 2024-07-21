package org.springframework.expression;

import java.lang.reflect.Method;
import java.util.List;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/MethodFilter.class */
public interface MethodFilter {
    List<Method> filter(List<Method> list);
}
