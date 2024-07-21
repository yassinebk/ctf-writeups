package org.springframework.expression;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/TypeLocator.class */
public interface TypeLocator {
    Class<?> findType(String str) throws EvaluationException;
}
