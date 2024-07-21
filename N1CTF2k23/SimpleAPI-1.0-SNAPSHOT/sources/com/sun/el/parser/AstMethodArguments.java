package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import javax.el.ELException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstMethodArguments.class */
public class AstMethodArguments extends SimpleNode {
    public AstMethodArguments(int id) {
        super(id);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Class<?>[] getParamTypes() {
        return null;
    }

    public Object[] getParameters(EvaluationContext ctx) throws ELException {
        if (this.children == null) {
            return new Object[0];
        }
        Object[] obj = new Object[this.children.length];
        for (int i = 0; i < obj.length; i++) {
            obj[i] = this.children[i].getValue(ctx);
        }
        return obj;
    }

    public int getParameterCount() {
        if (this.children == null) {
            return 0;
        }
        return this.children.length;
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public boolean isParametersProvided() {
        return true;
    }
}
