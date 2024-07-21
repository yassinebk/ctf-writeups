package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import javax.el.ELException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstSemiColon.class */
public class AstSemiColon extends SimpleNode {
    public AstSemiColon(int id) {
        super(id);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        this.children[0].getValue(ctx);
        return this.children[1].getValue(ctx);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public void setValue(EvaluationContext ctx, Object value) throws ELException {
        this.children[0].getValue(ctx);
        this.children[1].setValue(ctx, value);
    }
}
