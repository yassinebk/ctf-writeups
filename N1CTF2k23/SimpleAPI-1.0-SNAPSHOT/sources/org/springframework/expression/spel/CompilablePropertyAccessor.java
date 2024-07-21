package org.springframework.expression.spel;

import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.expression.PropertyAccessor;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/spel/CompilablePropertyAccessor.class */
public interface CompilablePropertyAccessor extends PropertyAccessor, Opcodes {
    boolean isCompilable();

    Class<?> getPropertyType();

    void generateCode(String str, MethodVisitor methodVisitor, CodeFlow codeFlow);
}
