package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import javax.el.ELException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstDeferredExpression.class */
public final class AstDeferredExpression extends SimpleNode {
    public AstDeferredExpression(int id) {
        super(id);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Class getType(EvaluationContext ctx) throws ELException {
        return this.children[0].getType(ctx);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        return this.children[0].getValue(ctx);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public boolean isReadOnly(EvaluationContext ctx) throws ELException {
        return this.children[0].isReadOnly(ctx);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public void setValue(EvaluationContext ctx, Object value) throws ELException {
        this.children[0].setValue(ctx, value);
    }
}
