package org.springframework.expression;

import java.util.List;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/ConstructorResolver.class */
public interface ConstructorResolver {
    @Nullable
    ConstructorExecutor resolve(EvaluationContext evaluationContext, String str, List<TypeDescriptor> list) throws AccessException;
}
