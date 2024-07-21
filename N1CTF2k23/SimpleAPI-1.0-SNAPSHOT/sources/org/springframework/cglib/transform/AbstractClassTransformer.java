package org.springframework.cglib.transform;

import org.springframework.asm.ClassVisitor;
import org.springframework.cglib.core.Constants;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/transform/AbstractClassTransformer.class */
public abstract class AbstractClassTransformer extends ClassTransformer {
    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractClassTransformer() {
        super(Constants.ASM_API);
    }

    @Override // org.springframework.cglib.transform.ClassTransformer
    public void setTarget(ClassVisitor target) {
        this.cv = target;
    }
}
