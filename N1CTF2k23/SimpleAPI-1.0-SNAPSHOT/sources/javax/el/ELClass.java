package javax.el;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ELClass.class */
public class ELClass {
    private Class<?> klass;

    public ELClass(Class<?> klass) {
        this.klass = klass;
    }

    public Class<?> getKlass() {
        return this.klass;
    }
}
