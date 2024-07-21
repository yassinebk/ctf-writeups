package javax.el;

import java.io.Serializable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ValueReference.class */
public class ValueReference implements Serializable {
    private static final long serialVersionUID = -4076659531951367109L;
    private Object base;
    private Object property;

    public ValueReference(Object base, Object property) {
        this.base = base;
        this.property = property;
    }

    public Object getBase() {
        return this.base;
    }

    public Object getProperty() {
        return this.property;
    }
}
