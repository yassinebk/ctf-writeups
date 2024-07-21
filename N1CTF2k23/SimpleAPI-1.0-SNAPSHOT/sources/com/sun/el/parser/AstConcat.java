package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import javax.el.ELException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstConcat.class */
public final class AstConcat extends SimpleNode {
    public AstConcat(int id) {
        super(id);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        return this.children[0].getValue(ctx).toString() + this.children[1].getValue(ctx).toString();
    }
}
