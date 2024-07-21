package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import javax.el.ELException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstString.class */
public final class AstString extends SimpleNode {
    private String string;

    public AstString(int id) {
        super(id);
    }

    public String getString() {
        if (this.string == null) {
            this.string = this.image.substring(1, this.image.length() - 1);
        }
        return this.string;
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Class getType(EvaluationContext ctx) throws ELException {
        return String.class;
    }

    @Override // com.sun.el.parser.SimpleNode, com.sun.el.parser.Node
    public Object getValue(EvaluationContext ctx) throws ELException {
        return getString();
    }

    @Override // com.sun.el.parser.SimpleNode
    public void setImage(String image) {
        char c1;
        if (image.indexOf(92) == -1) {
            this.image = image;
            return;
        }
        int size = image.length();
        StringBuffer buf = new StringBuffer(size);
        int i = 0;
        while (i < size) {
            char c = image.charAt(i);
            if (c == '\\' && i + 1 < size && ((c1 = image.charAt(i + 1)) == '\\' || c1 == '\"' || c1 == '\'' || c1 == '#' || c1 == '$')) {
                c = c1;
                i++;
            }
            buf.append(c);
            i++;
        }
        this.image = buf.toString();
    }
}
