package org.springframework.cglib.core;

import org.springframework.asm.Type;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/core/FieldTypeCustomizer.class */
public interface FieldTypeCustomizer extends KeyFactoryCustomizer {
    void customize(CodeEmitter codeEmitter, int i, Type type);

    Type getOutType(int i, Type type);
}
