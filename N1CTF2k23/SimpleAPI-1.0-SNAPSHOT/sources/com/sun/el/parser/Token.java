package com.sun.el.parser;

import java.io.Serializable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/Token.class */
public class Token implements Serializable {
    public int kind;
    public int beginLine;
    public int beginColumn;
    public int endLine;
    public int endColumn;
    public String image;
    public Token next;
    public Token specialToken;

    public String toString() {
        return this.image;
    }

    public static final Token newToken(int ofKind) {
        switch (ofKind) {
            default:
                return new Token();
        }
    }
}
