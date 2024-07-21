package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import java.util.ArrayList;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstListData.class */
public class AstListData extends SimpleNode {
    public AstListData(int id) {
        super(id);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) {
        ArrayList<Object> list = new ArrayList<>();
        int paramCount = jjtGetNumChildren();
        for (int i = 0; i < paramCount; i++) {
            list.add(this.children[i].getValue(ctx));
        }
        return list;
    }
}
