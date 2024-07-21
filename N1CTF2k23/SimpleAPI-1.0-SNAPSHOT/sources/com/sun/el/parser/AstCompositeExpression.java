package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import javax.el.ELException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstCompositeExpression.class */
public final class AstCompositeExpression extends SimpleNode {
    public AstCompositeExpression(int id) {
        super(id);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Class getType(EvaluationContext ctx) throws ELException {
        return String.class;
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        StringBuffer sb = new StringBuffer(16);
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                Object obj = this.children[i].getValue(ctx);
                if (obj != null) {
                    sb.append(obj);
                }
            }
        }
        return sb.toString();
    }
}
