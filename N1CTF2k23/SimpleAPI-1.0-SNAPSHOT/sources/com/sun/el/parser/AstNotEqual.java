package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import javax.el.ELException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstNotEqual.class */
public final class AstNotEqual extends BooleanNode {
    public AstNotEqual(int id) {
        super(id);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        Object obj0 = this.children[0].getValue(ctx);
        Object obj1 = this.children[1].getValue(ctx);
        return Boolean.valueOf(!equals(obj0, obj1));
    }
}
