package org.springframework.cglib.core;

import org.springframework.asm.Type;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/core/Customizer.class */
public interface Customizer extends KeyFactoryCustomizer {
    void customize(CodeEmitter codeEmitter, Type type);
}
