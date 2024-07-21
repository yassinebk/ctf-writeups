package org.springframework.expression.spel.support;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.AccessException;
import org.springframework.expression.ConstructorExecutor;
import org.springframework.expression.ConstructorResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.TypeConverter;
import org.springframework.expression.spel.support.ReflectionHelper;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/spel/support/ReflectiveConstructorResolver.class */
public class ReflectiveConstructorResolver implements ConstructorResolver {
    @Override // org.springframework.expression.ConstructorResolver
    @Nullable
    public ConstructorExecutor resolve(EvaluationContext context, String typeName, List<TypeDescriptor> argumentTypes) throws AccessException {
        try {
            TypeConverter typeConverter = context.getTypeConverter();
            Class<?> type = context.getTypeLocator().findType(typeName);
            Constructor<?>[] ctors = type.getConstructors();
            Arrays.sort(ctors, c1, c2 -> {
                int c1pl = c1.getParameterCount();
                int c2pl = c2.getParameterCount();
                return Integer.compare(c1pl, c2pl);
            });
            Constructor<?> closeMatch = null;
            Constructor<?> matchRequiringConversion = null;
            for (Constructor<?> ctor : ctors) {
                int paramCount = ctor.getParameterCount();
                List<TypeDescriptor> paramDescriptors = new ArrayList<>(paramCount);
                for (int i = 0; i < paramCount; i++) {
                    paramDescriptors.add(new TypeDescriptor(new MethodParameter(ctor, i)));
                }
                ReflectionHelper.ArgumentsMatchInfo matchInfo = null;
                if (ctor.isVarArgs() && argumentTypes.size() >= paramCount - 1) {
                    matchInfo = ReflectionHelper.compareArgumentsVarargs(paramDescriptors, argumentTypes, typeConverter);
                } else if (paramCount == argumentTypes.size()) {
                    matchInfo = ReflectionHelper.compareArguments(paramDescriptors, argumentTypes, typeConverter);
                }
                if (matchInfo != null) {
                    if (matchInfo.isExactMatch()) {
                        return new ReflectiveConstructorExecutor(ctor);
                    }
                    if (matchInfo.isCloseMatch()) {
                        closeMatch = ctor;
                    } else if (matchInfo.isMatchRequiringConversion()) {
                        matchRequiringConversion = ctor;
                    }
                }
            }
            if (closeMatch != null) {
                return new ReflectiveConstructorExecutor(closeMatch);
            }
            if (matchRequiringConversion != null) {
                return new ReflectiveConstructorExecutor(matchRequiringConversion);
            }
            return null;
        } catch (EvaluationException ex) {
            throw new AccessException("Failed to resolve constructor", ex);
        }
    }
}