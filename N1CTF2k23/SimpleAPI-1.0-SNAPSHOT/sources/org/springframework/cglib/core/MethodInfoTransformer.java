package org.springframework.cglib.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/core/MethodInfoTransformer.class */
public class MethodInfoTransformer implements Transformer {
    private static final MethodInfoTransformer INSTANCE = new MethodInfoTransformer();

    public static MethodInfoTransformer getInstance() {
        return INSTANCE;
    }

    @Override // org.springframework.cglib.core.Transformer
    public Object transform(Object value) {
        if (value instanceof Method) {
            return ReflectUtils.getMethodInfo((Method) value);
        }
        if (value instanceof Constructor) {
            return ReflectUtils.getMethodInfo((Constructor) value);
        }
        throw new IllegalArgumentException("cannot get method info for " + value);
    }
}
