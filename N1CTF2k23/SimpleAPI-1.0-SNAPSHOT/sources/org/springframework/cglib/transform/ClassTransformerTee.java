package org.springframework.cglib.transform;

import org.springframework.asm.ClassVisitor;
import org.springframework.cglib.core.Constants;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/transform/ClassTransformerTee.class */
public class ClassTransformerTee extends ClassTransformer {
    private ClassVisitor branch;

    public ClassTransformerTee(ClassVisitor branch) {
        super(Constants.ASM_API);
        this.branch = branch;
    }

    @Override // org.springframework.cglib.transform.ClassTransformer
    public void setTarget(ClassVisitor target) {
        this.cv = new ClassVisitorTee(this.branch, target);
    }
}
