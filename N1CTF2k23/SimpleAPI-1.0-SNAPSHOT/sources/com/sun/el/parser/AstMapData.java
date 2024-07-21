package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import java.util.HashMap;
import java.util.HashSet;
import javax.el.ELException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstMapData.class */
public class AstMapData extends SimpleNode {
    public AstMapData(int id) {
        super(id);
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) {
        HashSet<Object> set = new HashSet<>();
        HashMap<Object, Object> map = new HashMap<>();
        int paramCount = jjtGetNumChildren();
        for (int i = 0; i < paramCount; i++) {
            Node entry = this.children[i];
            Object v1 = entry.jjtGetChild(0).getValue(ctx);
            if (entry.jjtGetNumChildren() > 1) {
                map.put(v1, entry.jjtGetChild(1).getValue(ctx));
            } else {
                set.add(v1);
            }
        }
        if (set.size() > 0 && map.size() > 0) {
            throw new ELException("Cannot mix set entry with map entry.");
        }
        if (map.size() > 0) {
            return map;
        }
        return set;
    }
}
