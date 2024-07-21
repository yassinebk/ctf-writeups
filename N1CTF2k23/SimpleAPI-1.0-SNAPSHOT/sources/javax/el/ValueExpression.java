package javax.el;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ValueExpression.class */
public abstract class ValueExpression extends Expression {
    private static final long serialVersionUID = -8466802188968516519L;

    public abstract Object getValue(ELContext eLContext);

    public abstract void setValue(ELContext eLContext, Object obj);

    public abstract boolean isReadOnly(ELContext eLContext);

    public abstract Class<?> getType(ELContext eLContext);

    public abstract Class<?> getExpectedType();

    public ValueReference getValueReference(ELContext context) {
        return null;
    }
}
