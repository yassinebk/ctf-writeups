package org.springframework.boot.context.properties.bind;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.boot.context.properties.source.ConfigurationProperty;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.context.properties.source.ConfigurationPropertyState;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/bind/Binder.class */
public class Binder {
    private static final Set<Class<?>> NON_BEAN_CLASSES = Collections.unmodifiableSet(new HashSet(Arrays.asList(Object.class, Class.class)));
    private final Iterable<ConfigurationPropertySource> sources;
    private final PlaceholdersResolver placeholdersResolver;
    private final ConversionService conversionService;
    private final Consumer<PropertyEditorRegistry> propertyEditorInitializer;
    private final BindHandler defaultBindHandler;
    private final List<DataObjectBinder> dataObjectBinders;

    public Binder(ConfigurationPropertySource... sources) {
        this(Arrays.asList(sources), null, null, null);
    }

    public Binder(Iterable<ConfigurationPropertySource> sources) {
        this(sources, null, null, null);
    }

    public Binder(Iterable<ConfigurationPropertySource> sources, PlaceholdersResolver placeholdersResolver) {
        this(sources, placeholdersResolver, null, null);
    }

    public Binder(Iterable<ConfigurationPropertySource> sources, PlaceholdersResolver placeholdersResolver, ConversionService conversionService) {
        this(sources, placeholdersResolver, conversionService, null);
    }

    public Binder(Iterable<ConfigurationPropertySource> sources, PlaceholdersResolver placeholdersResolver, ConversionService conversionService, Consumer<PropertyEditorRegistry> propertyEditorInitializer) {
        this(sources, placeholdersResolver, conversionService, propertyEditorInitializer, null);
    }

    public Binder(Iterable<ConfigurationPropertySource> sources, PlaceholdersResolver placeholdersResolver, ConversionService conversionService, Consumer<PropertyEditorRegistry> propertyEditorInitializer, BindHandler defaultBindHandler) {
        this(sources, placeholdersResolver, conversionService, propertyEditorInitializer, defaultBindHandler, null);
    }

    public Binder(Iterable<ConfigurationPropertySource> sources, PlaceholdersResolver placeholdersResolver, ConversionService conversionService, Consumer<PropertyEditorRegistry> propertyEditorInitializer, BindHandler defaultBindHandler, BindConstructorProvider constructorProvider) {
        Assert.notNull(sources, "Sources must not be null");
        this.sources = sources;
        this.placeholdersResolver = placeholdersResolver != null ? placeholdersResolver : PlaceholdersResolver.NONE;
        this.conversionService = conversionService != null ? conversionService : ApplicationConversionService.getSharedInstance();
        this.propertyEditorInitializer = propertyEditorInitializer;
        this.defaultBindHandler = defaultBindHandler != null ? defaultBindHandler : BindHandler.DEFAULT;
        ValueObjectBinder valueObjectBinder = new ValueObjectBinder(constructorProvider == null ? BindConstructorProvider.DEFAULT : constructorProvider);
        JavaBeanBinder javaBeanBinder = JavaBeanBinder.INSTANCE;
        this.dataObjectBinders = Collections.unmodifiableList(Arrays.asList(valueObjectBinder, javaBeanBinder));
    }

    public <T> BindResult<T> bind(String name, Class<T> target) {
        return bind(name, Bindable.of(target));
    }

    public <T> BindResult<T> bind(String name, Bindable<T> target) {
        return bind(ConfigurationPropertyName.of(name), target, (BindHandler) null);
    }

    public <T> BindResult<T> bind(ConfigurationPropertyName name, Bindable<T> target) {
        return bind(name, target, (BindHandler) null);
    }

    public <T> BindResult<T> bind(String name, Bindable<T> target, BindHandler handler) {
        return bind(ConfigurationPropertyName.of(name), target, handler);
    }

    public <T> BindResult<T> bind(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler) {
        return BindResult.of(bind(name, target, handler, false));
    }

    public <T> T bindOrCreate(String name, Class<T> target) {
        return (T) bindOrCreate(name, Bindable.of(target));
    }

    public <T> T bindOrCreate(String name, Bindable<T> target) {
        return (T) bindOrCreate(ConfigurationPropertyName.of(name), target, (BindHandler) null);
    }

    public <T> T bindOrCreate(String name, Bindable<T> target, BindHandler handler) {
        return (T) bindOrCreate(ConfigurationPropertyName.of(name), target, handler);
    }

    public <T> T bindOrCreate(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler) {
        return (T) bind(name, target, handler, true);
    }

    private <T> T bind(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler, boolean create) {
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(target, "Target must not be null");
        BindHandler handler2 = handler != null ? handler : this.defaultBindHandler;
        Context context = new Context();
        return (T) bind(name, target, handler2, context, false, create);
    }

    private <T> T bind(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler, Context context, boolean allowRecursiveBinding, boolean create) {
        try {
            Bindable<T> replacementTarget = handler.onStart(name, target, context);
            if (replacementTarget == null) {
                return (T) handleBindResult(name, target, handler, context, null, create);
            }
            Object bound = bindObject(name, replacementTarget, handler, context, allowRecursiveBinding);
            return (T) handleBindResult(name, replacementTarget, handler, context, bound, create);
        } catch (Exception ex) {
            return (T) handleBindError(name, target, handler, context, ex);
        }
    }

    private <T> T handleBindResult(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler, Context context, Object result, boolean create) throws Exception {
        if (result != null) {
            result = context.getConverter().convert(handler.onSuccess(name, target, context, result), target);
        }
        if (result == null && create) {
            result = context.getConverter().convert(handler.onCreate(name, target, context, create(target, context)), target);
            Assert.state(result != null, () -> {
                return "Unable to create instance for " + target.getType();
            });
        }
        handler.onFinish(name, target, context, result);
        return (T) context.getConverter().convert(result, target);
    }

    private Object create(Bindable<?> target, Context context) {
        for (DataObjectBinder dataObjectBinder : this.dataObjectBinders) {
            Object instance = dataObjectBinder.create(target, context);
            if (instance != null) {
                return instance;
            }
        }
        return null;
    }

    private <T> T handleBindError(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler, Context context, Exception error) {
        try {
            Object result = handler.onFailure(name, target, context, error);
            return (T) context.getConverter().convert(result, target);
        } catch (Exception ex) {
            if (ex instanceof BindException) {
                throw ((BindException) ex);
            }
            throw new BindException(name, target, context.getConfigurationProperty(), ex);
        }
    }

    private <T> Object bindObject(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler, Context context, boolean allowRecursiveBinding) {
        ConfigurationProperty property = findProperty(name, context);
        if (property == null && context.depth != 0 && containsNoDescendantOf(context.getSources(), name)) {
            return null;
        }
        AggregateBinder<?> aggregateBinder = getAggregateBinder(target, context);
        if (aggregateBinder != null) {
            return bindAggregate(name, target, handler, context, aggregateBinder);
        }
        if (property != null) {
            try {
                return bindProperty(target, context, property);
            } catch (ConverterNotFoundException ex) {
                Object instance = bindDataObject(name, target, handler, context, allowRecursiveBinding);
                if (instance != null) {
                    return instance;
                }
                throw ex;
            }
        }
        return bindDataObject(name, target, handler, context, allowRecursiveBinding);
    }

    private AggregateBinder<?> getAggregateBinder(Bindable<?> target, Context context) {
        Class<?> resolvedType = target.getType().resolve(Object.class);
        if (Map.class.isAssignableFrom(resolvedType)) {
            return new MapBinder(context);
        }
        if (Collection.class.isAssignableFrom(resolvedType)) {
            return new CollectionBinder(context);
        }
        if (target.getType().isArray()) {
            return new ArrayBinder(context);
        }
        return null;
    }

    private <T> Object bindAggregate(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler, Context context, AggregateBinder<?> aggregateBinder) {
        AggregateElementBinder elementBinder = itemName, itemTarget, source -> {
            boolean allowRecursiveBinding = aggregateBinder.isAllowRecursiveBinding(source);
            Supplier<?> supplier = () -> {
                return bind(itemName, itemTarget, handler, context, allowRecursiveBinding, false);
            };
            return context.withSource(source, supplier);
        };
        return context.withIncreasedDepth(() -> {
            return aggregateBinder.bind(name, target, elementBinder);
        });
    }

    private ConfigurationProperty findProperty(ConfigurationPropertyName name, Context context) {
        if (name.isEmpty()) {
            return null;
        }
        for (ConfigurationPropertySource source : context.getSources()) {
            ConfigurationProperty property = source.getConfigurationProperty(name);
            if (property != null) {
                return property;
            }
        }
        return null;
    }

    private <T> Object bindProperty(Bindable<T> target, Context context, ConfigurationProperty property) {
        context.setConfigurationProperty(property);
        Object result = property.getValue();
        return context.getConverter().convert(this.placeholdersResolver.resolvePlaceholders(result), target);
    }

    private Object bindDataObject(ConfigurationPropertyName name, Bindable<?> target, BindHandler handler, Context context, boolean allowRecursiveBinding) {
        if (isUnbindableBean(name, target, context)) {
            return null;
        }
        Class<?> type = target.getType().resolve(Object.class);
        if (!allowRecursiveBinding && context.isBindingDataObject(type)) {
            return null;
        }
        DataObjectPropertyBinder propertyBinder = propertyName, propertyTarget -> {
            return bind(name.append(propertyName), propertyTarget, handler, context, false, false);
        };
        return context.withDataObject(type, () -> {
            for (DataObjectBinder dataObjectBinder : this.dataObjectBinders) {
                Object instance = dataObjectBinder.bind(name, target, context, propertyBinder);
                if (instance != null) {
                    return instance;
                }
            }
            return null;
        });
    }

    private boolean isUnbindableBean(ConfigurationPropertyName name, Bindable<?> target, Context context) {
        for (ConfigurationPropertySource source : context.getSources()) {
            if (source.containsDescendantOf(name) == ConfigurationPropertyState.PRESENT) {
                return false;
            }
        }
        Class<?> resolved = target.getType().resolve(Object.class);
        if (resolved.isPrimitive() || NON_BEAN_CLASSES.contains(resolved)) {
            return true;
        }
        return resolved.getName().startsWith("java.");
    }

    private boolean containsNoDescendantOf(Iterable<ConfigurationPropertySource> sources, ConfigurationPropertyName name) {
        for (ConfigurationPropertySource source : sources) {
            if (source.containsDescendantOf(name) != ConfigurationPropertyState.ABSENT) {
                return false;
            }
        }
        return true;
    }

    public static Binder get(Environment environment) {
        return get(environment, null);
    }

    public static Binder get(Environment environment, BindHandler defaultBindHandler) {
        Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(environment);
        PropertySourcesPlaceholdersResolver placeholdersResolver = new PropertySourcesPlaceholdersResolver(environment);
        return new Binder(sources, placeholdersResolver, null, null, defaultBindHandler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/bind/Binder$Context.class */
    public final class Context implements BindContext {
        private final BindConverter converter;
        private int depth;
        private int sourcePushCount;
        private ConfigurationProperty configurationProperty;
        private final List<ConfigurationPropertySource> source = Arrays.asList(null);
        private final Deque<Class<?>> dataObjectBindings = new ArrayDeque();
        private final Deque<Class<?>> constructorBindings = new ArrayDeque();

        Context() {
            this.converter = BindConverter.get(Binder.this.conversionService, Binder.this.propertyEditorInitializer);
        }

        private void increaseDepth() {
            this.depth++;
        }

        private void decreaseDepth() {
            this.depth--;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public <T> T withSource(ConfigurationPropertySource source, Supplier<T> supplier) {
            if (source == null) {
                return supplier.get();
            }
            this.source.set(0, source);
            this.sourcePushCount++;
            try {
                T t = supplier.get();
                this.sourcePushCount--;
                return t;
            } catch (Throwable th) {
                this.sourcePushCount--;
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public <T> T withDataObject(Class<?> type, Supplier<T> supplier) {
            this.dataObjectBindings.push(type);
            try {
                T t = (T) withIncreasedDepth(supplier);
                this.dataObjectBindings.pop();
                return t;
            } catch (Throwable th) {
                this.dataObjectBindings.pop();
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isBindingDataObject(Class<?> type) {
            return this.dataObjectBindings.contains(type);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public <T> T withIncreasedDepth(Supplier<T> supplier) {
            increaseDepth();
            try {
                return supplier.get();
            } finally {
                decreaseDepth();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setConfigurationProperty(ConfigurationProperty configurationProperty) {
            this.configurationProperty = configurationProperty;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void clearConfigurationProperty() {
            this.configurationProperty = null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void pushConstructorBoundTypes(Class<?> value) {
            this.constructorBindings.push(value);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean isNestedConstructorBinding() {
            return !this.constructorBindings.isEmpty();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void popConstructorBoundTypes() {
            this.constructorBindings.pop();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public PlaceholdersResolver getPlaceholdersResolver() {
            return Binder.this.placeholdersResolver;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public BindConverter getConverter() {
            return this.converter;
        }

        @Override // org.springframework.boot.context.properties.bind.BindContext
        public Binder getBinder() {
            return Binder.this;
        }

        @Override // org.springframework.boot.context.properties.bind.BindContext
        public int getDepth() {
            return this.depth;
        }

        @Override // org.springframework.boot.context.properties.bind.BindContext
        public Iterable<ConfigurationPropertySource> getSources() {
            if (this.sourcePushCount <= 0) {
                return Binder.this.sources;
            }
            return this.source;
        }

        @Override // org.springframework.boot.context.properties.bind.BindContext
        public ConfigurationProperty getConfigurationProperty() {
            return this.configurationProperty;
        }
    }
}
