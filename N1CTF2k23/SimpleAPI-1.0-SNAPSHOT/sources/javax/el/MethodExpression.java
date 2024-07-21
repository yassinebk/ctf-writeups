package javax.el;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/MethodExpression.class */
public abstract class MethodExpression extends Expression {
    private static final long serialVersionUID = -1151639017737837708L;

    public abstract MethodInfo getMethodInfo(ELContext eLContext);

    public abstract Object invoke(ELContext eLContext, Object[] objArr);

    public boolean isParametersProvided() {
        return false;
    }

    @Deprecated
    public boolean isParmetersProvided() {
        return isParametersProvided();
    }
}
