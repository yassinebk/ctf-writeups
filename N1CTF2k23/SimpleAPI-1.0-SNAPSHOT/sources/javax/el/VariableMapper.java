package javax.el;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/VariableMapper.class */
public abstract class VariableMapper {
    public abstract ValueExpression resolveVariable(String str);

    public abstract ValueExpression setVariable(String str, ValueExpression valueExpression);
}
