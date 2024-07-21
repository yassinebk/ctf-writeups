package javax.el;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/StandardELContext.class */
public class StandardELContext extends ELContext {
    private ELResolver elResolver;
    private CompositeELResolver customResolvers;
    private ELResolver streamELResolver;
    private FunctionMapper functionMapper;
    private Map<String, Method> initFunctionMap;
    private VariableMapper variableMapper;
    private ELContext delegate;
    private Map<String, Object> beans = new HashMap();

    public StandardELContext(ExpressionFactory factory) {
        this.streamELResolver = factory.getStreamELResolver();
        this.initFunctionMap = factory.getInitFunctionMap();
    }

    public StandardELContext(ELContext context) {
        this.delegate = context;
        CompositeELResolver compositeELResolver = new CompositeELResolver();
        compositeELResolver.add(new BeanNameELResolver(new LocalBeanNameResolver()));
        this.customResolvers = new CompositeELResolver();
        compositeELResolver.add(this.customResolvers);
        compositeELResolver.add(context.getELResolver());
        this.elResolver = compositeELResolver;
        this.functionMapper = context.getFunctionMapper();
        this.variableMapper = context.getVariableMapper();
        setLocale(context.getLocale());
    }

    @Override // javax.el.ELContext
    public void putContext(Class key, Object contextObject) {
        if (this.delegate != null) {
            this.delegate.putContext(key, contextObject);
        } else {
            super.putContext(key, contextObject);
        }
    }

    @Override // javax.el.ELContext
    public Object getContext(Class key) {
        if (this.delegate == null) {
            return super.getContext(key);
        }
        return this.delegate.getContext(key);
    }

    @Override // javax.el.ELContext
    public ELResolver getELResolver() {
        if (this.elResolver == null) {
            CompositeELResolver resolver = new CompositeELResolver();
            this.customResolvers = new CompositeELResolver();
            resolver.add(this.customResolvers);
            resolver.add(new BeanNameELResolver(new LocalBeanNameResolver()));
            if (this.streamELResolver != null) {
                resolver.add(this.streamELResolver);
            }
            resolver.add(new StaticFieldELResolver());
            resolver.add(new MapELResolver());
            resolver.add(new ResourceBundleELResolver());
            resolver.add(new ListELResolver());
            resolver.add(new ArrayELResolver());
            resolver.add(new BeanELResolver());
            this.elResolver = resolver;
        }
        return this.elResolver;
    }

    public void addELResolver(ELResolver cELResolver) {
        getELResolver();
        this.customResolvers.add(cELResolver);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<String, Object> getBeans() {
        return this.beans;
    }

    @Override // javax.el.ELContext
    public FunctionMapper getFunctionMapper() {
        if (this.functionMapper == null) {
            this.functionMapper = new DefaultFunctionMapper(this.initFunctionMap);
        }
        return this.functionMapper;
    }

    @Override // javax.el.ELContext
    public VariableMapper getVariableMapper() {
        if (this.variableMapper == null) {
            this.variableMapper = new DefaultVariableMapper();
        }
        return this.variableMapper;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/StandardELContext$DefaultFunctionMapper.class */
    private static class DefaultFunctionMapper extends FunctionMapper {
        private Map<String, Method> functions;

        DefaultFunctionMapper(Map<String, Method> initMap) {
            this.functions = initMap == null ? new HashMap() : new HashMap(initMap);
        }

        @Override // javax.el.FunctionMapper
        public Method resolveFunction(String prefix, String localName) {
            return this.functions.get(prefix + ":" + localName);
        }

        @Override // javax.el.FunctionMapper
        public void mapFunction(String prefix, String localName, Method meth) {
            this.functions.put(prefix + ":" + localName, meth);
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/StandardELContext$DefaultVariableMapper.class */
    private static class DefaultVariableMapper extends VariableMapper {
        private Map<String, ValueExpression> variables;

        private DefaultVariableMapper() {
        }

        @Override // javax.el.VariableMapper
        public ValueExpression resolveVariable(String variable) {
            if (this.variables == null) {
                return null;
            }
            return this.variables.get(variable);
        }

        @Override // javax.el.VariableMapper
        public ValueExpression setVariable(String variable, ValueExpression expression) {
            ValueExpression prev;
            if (this.variables == null) {
                this.variables = new HashMap();
            }
            if (expression == null) {
                prev = this.variables.remove(variable);
            } else {
                prev = this.variables.put(variable, expression);
            }
            return prev;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/StandardELContext$LocalBeanNameResolver.class */
    public class LocalBeanNameResolver extends BeanNameResolver {
        private LocalBeanNameResolver() {
        }

        @Override // javax.el.BeanNameResolver
        public boolean isNameResolved(String beanName) {
            return StandardELContext.this.beans.containsKey(beanName);
        }

        @Override // javax.el.BeanNameResolver
        public Object getBean(String beanName) {
            return StandardELContext.this.beans.get(beanName);
        }

        @Override // javax.el.BeanNameResolver
        public void setBeanValue(String beanName, Object value) {
            StandardELContext.this.beans.put(beanName, value);
        }

        @Override // javax.el.BeanNameResolver
        public boolean isReadOnly(String beanName) {
            return false;
        }

        @Override // javax.el.BeanNameResolver
        public boolean canCreateBean(String beanName) {
            return true;
        }
    }
}
