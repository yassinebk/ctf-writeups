package com.sun.el.lang;

import com.sun.el.MethodExpressionImpl;
import com.sun.el.MethodExpressionLiteral;
import com.sun.el.ValueExpressionImpl;
import com.sun.el.parser.AstCompositeExpression;
import com.sun.el.parser.AstDeferredExpression;
import com.sun.el.parser.AstDynamicExpression;
import com.sun.el.parser.AstFunction;
import com.sun.el.parser.AstIdentifier;
import com.sun.el.parser.AstLiteralExpression;
import com.sun.el.parser.AstMethodArguments;
import com.sun.el.parser.AstValue;
import com.sun.el.parser.ELParser;
import com.sun.el.parser.ELParserTokenManager;
import com.sun.el.parser.Node;
import com.sun.el.parser.NodeVisitor;
import com.sun.el.parser.ParseException;
import com.sun.el.parser.SimpleCharStream;
import com.sun.el.util.MessageFactory;
import java.io.StringReader;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import javax.el.ELContext;
import javax.el.ELException;
import javax.el.FunctionMapper;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.el.VariableMapper;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/lang/ExpressionBuilder.class */
public final class ExpressionBuilder implements NodeVisitor {
    private static final SoftConcurrentHashMap cache = new SoftConcurrentHashMap();
    private FunctionMapper fnMapper;
    private VariableMapper varMapper;
    private String expression;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/lang/ExpressionBuilder$NodeSoftReference.class */
    public static class NodeSoftReference extends SoftReference<Node> {
        final String key;

        NodeSoftReference(String key, Node node, ReferenceQueue<Node> refQ) {
            super(node, refQ);
            this.key = key;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/lang/ExpressionBuilder$SoftConcurrentHashMap.class */
    public static class SoftConcurrentHashMap extends ConcurrentHashMap<String, Node> {
        private static final int CACHE_INIT_SIZE = 256;
        private ConcurrentHashMap<String, NodeSoftReference> map;
        private ReferenceQueue<Node> refQ;

        private SoftConcurrentHashMap() {
            this.map = new ConcurrentHashMap<>(256);
            this.refQ = new ReferenceQueue<>();
        }

        private void cleanup() {
            while (true) {
                NodeSoftReference nodeRef = (NodeSoftReference) this.refQ.poll();
                if (nodeRef != null) {
                    this.map.remove(nodeRef.key);
                } else {
                    return;
                }
            }
        }

        @Override // java.util.concurrent.ConcurrentHashMap, java.util.AbstractMap, java.util.Map
        public Node put(String key, Node value) {
            cleanup();
            NodeSoftReference prev = this.map.put(key, new NodeSoftReference(key, value, this.refQ));
            if (prev == null) {
                return null;
            }
            return prev.get();
        }

        @Override // java.util.concurrent.ConcurrentHashMap, java.util.Map, java.util.concurrent.ConcurrentMap
        public Node putIfAbsent(String key, Node value) {
            cleanup();
            NodeSoftReference prev = this.map.putIfAbsent(key, new NodeSoftReference(key, value, this.refQ));
            if (prev == null) {
                return null;
            }
            return prev.get();
        }

        @Override // java.util.concurrent.ConcurrentHashMap, java.util.AbstractMap, java.util.Map
        public Node get(Object key) {
            cleanup();
            NodeSoftReference nodeRef = this.map.get(key);
            if (nodeRef == null) {
                return null;
            }
            if (nodeRef.get() == null) {
                this.map.remove(key);
                return null;
            }
            return nodeRef.get();
        }
    }

    public ExpressionBuilder(String expression, ELContext ctx) throws ELException {
        this.expression = expression;
        FunctionMapper ctxFn = ctx.getFunctionMapper();
        VariableMapper ctxVar = ctx.getVariableMapper();
        if (ctxFn != null) {
            this.fnMapper = new FunctionMapperFactory(ctxFn);
        }
        if (ctxVar != null) {
            this.varMapper = new VariableMapperFactory(ctxVar);
        }
    }

    public static Node createNode(String expr) throws ELException {
        Node n = createNodeInternal(expr);
        return n;
    }

    private static Node createNodeInternal(String expr) throws ELException {
        if (expr == null) {
            throw new ELException(MessageFactory.get("error.null"));
        }
        Node n = cache.get((Object) expr);
        if (n == null) {
            try {
                n = new ELParser(new ELParserTokenManager(new SimpleCharStream(new StringReader(expr), 1, 1, expr.length() + 1))).CompositeExpression();
                if (n instanceof AstCompositeExpression) {
                    int numChildren = n.jjtGetNumChildren();
                    if (numChildren == 1) {
                        n = n.jjtGetChild(0);
                    } else {
                        Class type = null;
                        for (int i = 0; i < numChildren; i++) {
                            Node child = n.jjtGetChild(i);
                            if (!(child instanceof AstLiteralExpression)) {
                                if (type == null) {
                                    type = child.getClass();
                                } else if (!type.equals(child.getClass())) {
                                    throw new ELException(MessageFactory.get("error.mixed", expr));
                                }
                            }
                        }
                    }
                }
                if ((n instanceof AstDeferredExpression) || (n instanceof AstDynamicExpression)) {
                    n = n.jjtGetChild(0);
                }
                cache.putIfAbsent(expr, n);
            } catch (ParseException pe) {
                throw new ELException("Error Parsing: " + expr, pe);
            }
        }
        return n;
    }

    private void prepare(Node node) throws ELException {
        node.accept(this);
        if (this.fnMapper instanceof FunctionMapperFactory) {
            this.fnMapper = ((FunctionMapperFactory) this.fnMapper).create();
        }
        if (this.varMapper instanceof VariableMapperFactory) {
            this.varMapper = ((VariableMapperFactory) this.varMapper).create();
        }
    }

    private Node build() throws ELException {
        Node n = createNodeInternal(this.expression);
        prepare(n);
        if ((n instanceof AstDeferredExpression) || (n instanceof AstDynamicExpression)) {
            n = n.jjtGetChild(0);
        }
        return n;
    }

    @Override // com.sun.el.parser.NodeVisitor
    public void visit(Node node) throws ELException {
        if (!(node instanceof AstFunction)) {
            if ((node instanceof AstIdentifier) && this.varMapper != null) {
                String variable = ((AstIdentifier) node).getImage();
                this.varMapper.resolveVariable(variable);
                return;
            }
            return;
        }
        AstFunction funcNode = (AstFunction) node;
        if (funcNode.getPrefix().length() == 0 && (this.fnMapper == null || this.fnMapper.resolveFunction(funcNode.getPrefix(), funcNode.getLocalName()) == null)) {
            if (this.varMapper != null) {
                this.varMapper.resolveVariable(funcNode.getLocalName());
            }
        } else if (this.fnMapper == null) {
            throw new ELException(MessageFactory.get("error.fnMapper.null"));
        } else {
            Method m = this.fnMapper.resolveFunction(funcNode.getPrefix(), funcNode.getLocalName());
            if (m == null) {
                throw new ELException(MessageFactory.get("error.fnMapper.method", funcNode.getOutputName()));
            }
            int pcnt = m.getParameterTypes().length;
            int acnt = ((AstMethodArguments) node.jjtGetChild(0)).getParameterCount();
            if (acnt != pcnt) {
                throw new ELException(MessageFactory.get("error.fnMapper.paramcount", funcNode.getOutputName(), "" + pcnt, "" + acnt));
            }
        }
    }

    public ValueExpression createValueExpression(Class expectedType) throws ELException {
        Node n = build();
        return new ValueExpressionImpl(this.expression, n, this.fnMapper, this.varMapper, expectedType);
    }

    public MethodExpression createMethodExpression(Class expectedReturnType, Class[] expectedParamTypes) throws ELException {
        Node n = build();
        if ((n instanceof AstValue) || (n instanceof AstIdentifier)) {
            return new MethodExpressionImpl(this.expression, n, this.fnMapper, this.varMapper, expectedReturnType, expectedParamTypes);
        }
        if (n instanceof AstLiteralExpression) {
            return new MethodExpressionLiteral(this.expression, expectedReturnType, expectedParamTypes);
        }
        throw new ELException("Not a Valid Method Expression: " + this.expression);
    }
}
