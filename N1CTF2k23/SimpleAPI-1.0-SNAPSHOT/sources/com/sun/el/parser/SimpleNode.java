package com.sun.el.parser;

import com.sun.el.lang.ELSupport;
import com.sun.el.lang.EvaluationContext;
import com.sun.el.util.MessageFactory;
import javax.el.ELException;
import javax.el.MethodInfo;
import javax.el.PropertyNotWritableException;
import javax.el.ValueReference;
import org.springframework.beans.PropertyAccessor;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/SimpleNode.class */
public abstract class SimpleNode extends ELSupport implements Node {
    protected Node parent;
    protected Node[] children;
    protected int id;
    protected String image;

    public SimpleNode(int i) {
        this.id = i;
    }

    @Override // com.sun.el.parser.Node
    public void jjtOpen() {
    }

    @Override // com.sun.el.parser.Node
    public void jjtClose() {
    }

    @Override // com.sun.el.parser.Node
    public void jjtSetParent(Node n) {
        this.parent = n;
    }

    @Override // com.sun.el.parser.Node
    public Node jjtGetParent() {
        return this.parent;
    }

    @Override // com.sun.el.parser.Node
    public void jjtAddChild(Node n, int i) {
        if (this.children == null) {
            this.children = new Node[i + 1];
        } else if (i >= this.children.length) {
            Node[] c = new Node[i + 1];
            System.arraycopy(this.children, 0, c, 0, this.children.length);
            this.children = c;
        }
        this.children[i] = n;
    }

    @Override // com.sun.el.parser.Node
    public Node jjtGetChild(int i) {
        return this.children[i];
    }

    @Override // com.sun.el.parser.Node
    public int jjtGetNumChildren() {
        if (this.children == null) {
            return 0;
        }
        return this.children.length;
    }

    public String toString() {
        if (this.image != null) {
            return ELParserTreeConstants.jjtNodeName[this.id] + PropertyAccessor.PROPERTY_KEY_PREFIX + this.image + "]";
        }
        return ELParserTreeConstants.jjtNodeName[this.id];
    }

    public String toString(String prefix) {
        return prefix + toString();
    }

    public void dump(String prefix) {
        System.out.println(toString(prefix));
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                SimpleNode n = (SimpleNode) this.children[i];
                if (n != null) {
                    n.dump(prefix + " ");
                }
            }
        }
    }

    @Override // com.sun.el.parser.Node
    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Class getType(EvaluationContext ctx) throws ELException {
        throw new UnsupportedOperationException();
    }

    public Object getValue(EvaluationContext ctx) throws ELException {
        throw new UnsupportedOperationException();
    }

    public ValueReference getValueReference(EvaluationContext ctx) throws ELException {
        return null;
    }

    public boolean isReadOnly(EvaluationContext ctx) throws ELException {
        return true;
    }

    public void setValue(EvaluationContext ctx, Object value) throws ELException {
        throw new PropertyNotWritableException(MessageFactory.get("error.syntax.set"));
    }

    @Override // com.sun.el.parser.Node
    public void accept(NodeVisitor visitor) throws ELException {
        visitor.visit(this);
        if (this.children != null && this.children.length > 0) {
            for (int i = 0; i < this.children.length; i++) {
                this.children[i].accept(visitor);
            }
        }
    }

    public Object invoke(EvaluationContext ctx, Class[] paramTypes, Object[] paramValues) throws ELException {
        throw new UnsupportedOperationException();
    }

    public MethodInfo getMethodInfo(EvaluationContext ctx, Class[] paramTypes) throws ELException {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.el.parser.Node
    public boolean equals(Object node) {
        if (!(node instanceof SimpleNode)) {
            return false;
        }
        SimpleNode n = (SimpleNode) node;
        if (this.id != n.id) {
            return false;
        }
        if (this.children == null && n.children == null) {
            if (this.image == null) {
                return n.image == null;
            }
            return this.image.equals(n.image);
        } else if (this.children == null || n.children == null || this.children.length != n.children.length) {
            return false;
        } else {
            if (this.children.length == 0) {
                if (this.image == null) {
                    return n.image == null;
                }
                return this.image.equals(n.image);
            }
            for (int i = 0; i < this.children.length; i++) {
                if (!this.children[i].equals(n.children[i])) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean isParametersProvided() {
        return false;
    }

    @Override // com.sun.el.parser.Node
    public int hashCode() {
        if (this.children == null || this.children.length == 0) {
            if (this.image != null) {
                return this.image.hashCode();
            }
            return this.id;
        }
        int h = 0;
        for (int i = this.children.length - 1; i >= 0; i--) {
            h = h + h + h + this.children[i].hashCode();
        }
        return h + h + h + this.id;
    }
}
