package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import javax.el.ELException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstOr.class */
public final class AstOr extends BooleanNode {
    public AstOr(int id) {
        super(id);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        Object obj = this.children[0].getValue(ctx);
        Boolean b = coerceToBoolean(obj);
        if (b.booleanValue()) {
            return b;
        }
        Object obj2 = this.children[1].getValue(ctx);
        return coerceToBoolean(obj2);
    }
}
