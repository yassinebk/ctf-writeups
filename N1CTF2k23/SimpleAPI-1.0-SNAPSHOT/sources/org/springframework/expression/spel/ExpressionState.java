package org.springframework.expression.spel;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Operation;
import org.springframework.expression.OperatorOverloader;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypeComparator;
import org.springframework.expression.TypeConverter;
import org.springframework.expression.TypedValue;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/spel/ExpressionState.class */
public class ExpressionState {
    private final EvaluationContext relatedContext;
    private final TypedValue rootObject;
    private final SpelParserConfiguration configuration;
    @Nullable
    private Deque<TypedValue> contextObjects;
    @Nullable
    private Deque<VariableScope> variableScopes;
    @Nullable
    private ArrayDeque<TypedValue> scopeRootObjects;

    public ExpressionState(EvaluationContext context) {
        this(context, context.getRootObject(), new SpelParserConfiguration(false, false));
    }

    public ExpressionState(EvaluationContext context, SpelParserConfiguration configuration) {
        this(context, context.getRootObject(), configuration);
    }

    public ExpressionState(EvaluationContext context, TypedValue rootObject) {
        this(context, rootObject, new SpelParserConfiguration(false, false));
    }

    public ExpressionState(EvaluationContext context, TypedValue rootObject, SpelParserConfiguration configuration) {
        Assert.notNull(context, "EvaluationContext must not be null");
        Assert.notNull(configuration, "SpelParserConfiguration must not be null");
        this.relatedContext = context;
        this.rootObject = rootObject;
        this.configuration = configuration;
    }

    public TypedValue getActiveContextObject() {
        if (CollectionUtils.isEmpty(this.contextObjects)) {
            return this.rootObject;
        }
        return this.contextObjects.element();
    }

    public void pushActiveContextObject(TypedValue obj) {
        if (this.contextObjects == null) {
            this.contextObjects = new ArrayDeque();
        }
        this.contextObjects.push(obj);
    }

    public void popActiveContextObject() {
        if (this.contextObjects == null) {
            this.contextObjects = new ArrayDeque();
        }
        try {
            this.contextObjects.pop();
        } catch (NoSuchElementException e) {
            throw new IllegalStateException("Cannot pop active context object: stack is empty");
        }
    }

    public TypedValue getRootContextObject() {
        return this.rootObject;
    }

    public TypedValue getScopeRootContextObject() {
        if (CollectionUtils.isEmpty(this.scopeRootObjects)) {
            return this.rootObject;
        }
        return this.scopeRootObjects.element();
    }

    public void setVariable(String name, @Nullable Object value) {
        this.relatedContext.setVariable(name, value);
    }

    public TypedValue lookupVariable(String name) {
        Object value = this.relatedContext.lookupVariable(name);
        return value != null ? new TypedValue(value) : TypedValue.NULL;
    }

    public TypeComparator getTypeComparator() {
        return this.relatedContext.getTypeComparator();
    }

    public Class<?> findType(String type) throws EvaluationException {
        return this.relatedContext.getTypeLocator().findType(type);
    }

    public Object convertValue(Object value, TypeDescriptor targetTypeDescriptor) throws EvaluationException {
        Object result = this.relatedContext.getTypeConverter().convertValue(value, TypeDescriptor.forObject(value), targetTypeDescriptor);
        if (result == null) {
            throw new IllegalStateException("Null conversion result for value [" + value + "]");
        }
        return result;
    }

    public TypeConverter getTypeConverter() {
        return this.relatedContext.getTypeConverter();
    }

    @Nullable
    public Object convertValue(TypedValue value, TypeDescriptor targetTypeDescriptor) throws EvaluationException {
        Object val = value.getValue();
        return this.relatedContext.getTypeConverter().convertValue(val, TypeDescriptor.forObject(val), targetTypeDescriptor);
    }

    public void enterScope(Map<String, Object> argMap) {
        initVariableScopes().push(new VariableScope(argMap));
        initScopeRootObjects().push(getActiveContextObject());
    }

    public void enterScope() {
        initVariableScopes().push(new VariableScope(Collections.emptyMap()));
        initScopeRootObjects().push(getActiveContextObject());
    }

    public void enterScope(String name, Object value) {
        initVariableScopes().push(new VariableScope(name, value));
        initScopeRootObjects().push(getActiveContextObject());
    }

    public void exitScope() {
        initVariableScopes().pop();
        initScopeRootObjects().pop();
    }

    public void setLocalVariable(String name, Object value) {
        initVariableScopes().element().setVariable(name, value);
    }

    @Nullable
    public Object lookupLocalVariable(String name) {
        for (VariableScope scope : initVariableScopes()) {
            if (scope.definesVariable(name)) {
                return scope.lookupVariable(name);
            }
        }
        return null;
    }

    private Deque<VariableScope> initVariableScopes() {
        if (this.variableScopes == null) {
            this.variableScopes = new ArrayDeque();
            this.variableScopes.add(new VariableScope());
        }
        return this.variableScopes;
    }

    private Deque<TypedValue> initScopeRootObjects() {
        if (this.scopeRootObjects == null) {
            this.scopeRootObjects = new ArrayDeque<>();
        }
        return this.scopeRootObjects;
    }

    public TypedValue operate(Operation op, @Nullable Object left, @Nullable Object right) throws EvaluationException {
        OperatorOverloader overloader = this.relatedContext.getOperatorOverloader();
        if (overloader.overridesOperation(op, left, right)) {
            Object returnValue = overloader.operate(op, left, right);
            return new TypedValue(returnValue);
        }
        String leftType = left == null ? BeanDefinitionParserDelegate.NULL_ELEMENT : left.getClass().getName();
        String rightType = right == null ? BeanDefinitionParserDelegate.NULL_ELEMENT : right.getClass().getName();
        throw new SpelEvaluationException(SpelMessage.OPERATOR_NOT_SUPPORTED_BETWEEN_TYPES, op, leftType, rightType);
    }

    public List<PropertyAccessor> getPropertyAccessors() {
        return this.relatedContext.getPropertyAccessors();
    }

    public EvaluationContext getEvaluationContext() {
        return this.relatedContext;
    }

    public SpelParserConfiguration getConfiguration() {
        return this.configuration;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/spel/ExpressionState$VariableScope.class */
    public static class VariableScope {
        private final Map<String, Object> vars = new HashMap();

        public VariableScope() {
        }

        public VariableScope(@Nullable Map<String, Object> arguments) {
            if (arguments != null) {
                this.vars.putAll(arguments);
            }
        }

        public VariableScope(String name, Object value) {
            this.vars.put(name, value);
        }

        public Object lookupVariable(String name) {
            return this.vars.get(name);
        }

        public void setVariable(String name, Object value) {
            this.vars.put(name, value);
        }

        public boolean definesVariable(String name) {
            return this.vars.containsKey(name);
        }
    }
}
