package org.springframework.expression;

import java.util.List;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/MethodResolver.class */
public interface MethodResolver {
    @Nullable
    MethodExecutor resolve(EvaluationContext evaluationContext, Object obj, String str, List<TypeDescriptor> list) throws AccessException;
}
