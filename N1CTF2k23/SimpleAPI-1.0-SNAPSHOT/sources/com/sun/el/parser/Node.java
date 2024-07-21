package com.sun.el.parser;

import com.sun.el.lang.EvaluationContext;
import javax.el.ELException;
import javax.el.MethodInfo;
import javax.el.ValueReference;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/Node.class */
public interface Node {
    void jjtOpen();

    void jjtClose();

    void jjtSetParent(Node node);

    Node jjtGetParent();

    void jjtAddChild(Node node, int i);

    Node jjtGetChild(int i);

    int jjtGetNumChildren();

    String getImage();

    Object getValue(EvaluationContext evaluationContext) throws ELException;

    void setValue(EvaluationContext evaluationContext, Object obj) throws ELException;

    Class getType(EvaluationContext evaluationContext) throws ELException;

    ValueReference getValueReference(EvaluationContext evaluationContext) throws ELException;

    boolean isReadOnly(EvaluationContext evaluationContext) throws ELException;

    void accept(NodeVisitor nodeVisitor) throws ELException;

    MethodInfo getMethodInfo(EvaluationContext evaluationContext, Class[] clsArr) throws ELException;

    Object invoke(EvaluationContext evaluationContext, Class[] clsArr, Object[] objArr) throws ELException;

    boolean equals(Object obj);

    int hashCode();

    boolean isParametersProvided();
}
