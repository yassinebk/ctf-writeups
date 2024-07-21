package org.springframework.cglib.core;

import org.springframework.asm.Type;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/core/HashCodeCustomizer.class */
public interface HashCodeCustomizer extends KeyFactoryCustomizer {
    boolean customize(CodeEmitter codeEmitter, Type type);
}
