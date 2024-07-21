package com.sun.el.parser;

import javax.el.ELException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/NodeVisitor.class */
public interface NodeVisitor {
    void visit(Node node) throws ELException;
}
