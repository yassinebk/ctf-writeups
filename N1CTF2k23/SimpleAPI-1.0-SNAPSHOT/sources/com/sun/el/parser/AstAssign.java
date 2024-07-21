package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import javax.el.ELException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstAssign.class */
public class AstAssign extends SimpleNode {
    public AstAssign(int id) {
        super(id);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        Object value = this.children[1].getValue(ctx);
        this.children[0].setValue(ctx, value);
        return value;
    }
}
