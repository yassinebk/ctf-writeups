package org.springframework.cglib.core;

import org.springframework.asm.ClassVisitor;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/core/ClassGenerator.class */
public interface ClassGenerator {
    void generateClass(ClassVisitor classVisitor) throws Exception;
}