package javax.el;

import java.io.Serializable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/Expression.class */
public abstract class Expression implements Serializable {
    private static final long serialVersionUID = -6663767980471823812L;

    public abstract String getExpressionString();

    public abstract boolean equals(Object obj);

    public abstract int hashCode();

    public abstract boolean isLiteralText();
}
