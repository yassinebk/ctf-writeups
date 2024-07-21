package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import org.springframework.core.MethodParameter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/SynthesizingMethodParameter.class */
public class SynthesizingMethodParameter extends MethodParameter {
    public SynthesizingMethodParameter(Method method, int parameterIndex) {
        super(method, parameterIndex);
    }

    public SynthesizingMethodParameter(Method method, int parameterIndex, int nestingLevel) {
        super(method, parameterIndex, nestingLevel);
    }

    public SynthesizingMethodParameter(Constructor<?> constructor, int parameterIndex) {
        super(constructor, parameterIndex);
    }

    public SynthesizingMethodParameter(Constructor<?> constructor, int parameterIndex, int nestingLevel) {
        super(constructor, parameterIndex, nestingLevel);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public SynthesizingMethodParameter(SynthesizingMethodParameter original) {
        super(original);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.core.MethodParameter
    public <A extends Annotation> A adaptAnnotation(A annotation) {
        return (A) AnnotationUtils.synthesizeAnnotation(annotation, getAnnotatedElement());
    }

    @Override // org.springframework.core.MethodParameter
    protected Annotation[] adaptAnnotationArray(Annotation[] annotations) {
        return AnnotationUtils.synthesizeAnnotationArray(annotations, getAnnotatedElement());
    }

    @Override // org.springframework.core.MethodParameter
    /* renamed from: clone */
    public SynthesizingMethodParameter mo1477clone() {
        return new SynthesizingMethodParameter(this);
    }

    public static SynthesizingMethodParameter forExecutable(Executable executable, int parameterIndex) {
        if (executable instanceof Method) {
            return new SynthesizingMethodParameter((Method) executable, parameterIndex);
        }
        if (executable instanceof Constructor) {
            return new SynthesizingMethodParameter((Constructor) executable, parameterIndex);
        }
        throw new IllegalArgumentException("Not a Method/Constructor: " + executable);
    }

    public static SynthesizingMethodParameter forParameter(Parameter parameter) {
        return forExecutable(parameter.getDeclaringExecutable(), findParameterIndex(parameter));
    }
}
