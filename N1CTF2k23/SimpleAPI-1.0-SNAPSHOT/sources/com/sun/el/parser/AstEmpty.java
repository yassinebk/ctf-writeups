package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import java.util.Collection;
import java.util.Map;
import javax.el.ELException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstEmpty.class */
public final class AstEmpty extends SimpleNode {
    public AstEmpty(int id) {
        super(id);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Class getType(EvaluationContext ctx) throws ELException {
        return Boolean.class;
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        Object obj = this.children[0].getValue(ctx);
        if (obj == null) {
            return Boolean.TRUE;
        }
        if (obj instanceof String) {
            return Boolean.valueOf(((String) obj).length() == 0);
        } else if (obj instanceof Object[]) {
            return Boolean.valueOf(((Object[]) obj).length == 0);
        } else if (obj instanceof Collection) {
            return Boolean.valueOf(((Collection) obj).isEmpty());
        } else {
            if (obj instanceof Map) {
                return Boolean.valueOf(((Map) obj).isEmpty());
            }
            return Boolean.FALSE;
        }
    }
}
