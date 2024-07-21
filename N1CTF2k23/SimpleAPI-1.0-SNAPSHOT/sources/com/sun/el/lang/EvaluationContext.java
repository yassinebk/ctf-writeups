package com.sun.el.lang;

import java.util.List;
import java.util.Map;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.EvaluationListener;
import javax.el.FunctionMapper;
import javax.el.ImportHandler;
import javax.el.VariableMapper;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/lang/EvaluationContext.class */
public final class EvaluationContext extends ELContext {
    private final ELContext elContext;
    private final FunctionMapper fnMapper;
    private final VariableMapper varMapper;

    public EvaluationContext(ELContext elContext, FunctionMapper fnMapper, VariableMapper varMapper) {
        this.elContext = elContext;
        this.fnMapper = fnMapper;
        this.varMapper = varMapper;
    }

    public ELContext getELContext() {
        return this.elContext;
    }

    @Override // javax.el.ELContext
    public FunctionMapper getFunctionMapper() {
        return this.fnMapper;
    }

    @Override // javax.el.ELContext
    public VariableMapper getVariableMapper() {
        return this.varMapper;
    }

    @Override // javax.el.ELContext
    public Object getContext(Class key) {
        return this.elContext.getContext(key);
    }

    @Override // javax.el.ELContext
    public ELResolver getELResolver() {
        return this.elContext.getELResolver();
    }

    @Override // javax.el.ELContext
    public boolean isPropertyResolved() {
        return this.elContext.isPropertyResolved();
    }

    @Override // javax.el.ELContext
    public void putContext(Class key, Object contextObject) {
        this.elContext.putContext(key, contextObject);
    }

    @Override // javax.el.ELContext
    public void setPropertyResolved(boolean resolved) {
        this.elContext.setPropertyResolved(resolved);
    }

    @Override // javax.el.ELContext
    public void setPropertyResolved(Object base, Object property) {
        this.elContext.setPropertyResolved(base, property);
    }

    @Override // javax.el.ELContext
    public void addEvaluationListener(EvaluationListener listener) {
        this.elContext.addEvaluationListener(listener);
    }

    @Override // javax.el.ELContext
    public List<EvaluationListener> getEvaluationListeners() {
        return this.elContext.getEvaluationListeners();
    }

    @Override // javax.el.ELContext
    public void notifyBeforeEvaluation(String expr) {
        this.elContext.notifyBeforeEvaluation(expr);
    }

    @Override // javax.el.ELContext
    public void notifyAfterEvaluation(String expr) {
        this.elContext.notifyAfterEvaluation(expr);
    }

    @Override // javax.el.ELContext
    public void notifyPropertyResolved(Object base, Object property) {
        this.elContext.notifyPropertyResolved(base, property);
    }

    @Override // javax.el.ELContext
    public boolean isLambdaArgument(String arg) {
        return this.elContext.isLambdaArgument(arg);
    }

    @Override // javax.el.ELContext
    public Object getLambdaArgument(String arg) {
        return this.elContext.getLambdaArgument(arg);
    }

    @Override // javax.el.ELContext
    public void enterLambdaScope(Map<String, Object> args) {
        this.elContext.enterLambdaScope(args);
    }

    @Override // javax.el.ELContext
    public void exitLambdaScope() {
        this.elContext.exitLambdaScope();
    }

    @Override // javax.el.ELContext
    public Object convertToType(Object obj, Class<?> targetType) {
        return this.elContext.convertToType(obj, targetType);
    }

    @Override // javax.el.ELContext
    public ImportHandler getImportHandler() {
        return this.elContext.getImportHandler();
    }
}
