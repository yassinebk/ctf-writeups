package org.springframework.cglib.transform;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/transform/ClassFilterTransformer.class */
public class ClassFilterTransformer extends AbstractClassFilterTransformer {
    private ClassFilter filter;

    public ClassFilterTransformer(ClassFilter filter, ClassTransformer pass) {
        super(pass);
        this.filter = filter;
    }

    @Override // org.springframework.cglib.transform.AbstractClassFilterTransformer
    protected boolean accept(int version, int access, String name, String signature, String superName, String[] interfaces) {
        return this.filter.accept(name.replace('/', '.'));
    }
}
