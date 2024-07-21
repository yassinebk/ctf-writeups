package org.springframework.beans.factory.support;

import java.beans.PropertyEditor;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.PropertyEditorRegistrySupport;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanIsAbstractException;
import org.springframework.beans.factory.BeanIsNotAFactoryException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.SmartFactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.Scope;
import org.springframework.core.AttributeAccessor;
import org.springframework.core.DecoratingClassLoader;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.log.LogMessage;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/factory/support/AbstractBeanFactory.class */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {
    @Nullable
    private BeanFactory parentBeanFactory;
    @Nullable
    private ClassLoader tempClassLoader;
    @Nullable
    private BeanExpressionResolver beanExpressionResolver;
    @Nullable
    private ConversionService conversionService;
    @Nullable
    private TypeConverter typeConverter;
    private volatile boolean hasInstantiationAwareBeanPostProcessors;
    private volatile boolean hasDestructionAwareBeanPostProcessors;
    @Nullable
    private SecurityContextProvider securityContextProvider;
    @Nullable
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    private boolean cacheBeanMetadata = true;
    private final Set<PropertyEditorRegistrar> propertyEditorRegistrars = new LinkedHashSet(4);
    private final Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap(4);
    private final List<StringValueResolver> embeddedValueResolvers = new CopyOnWriteArrayList();
    private final List<BeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList();
    private final Map<String, Scope> scopes = new LinkedHashMap(8);
    private final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap(256);
    private final Set<String> alreadyCreated = Collections.newSetFromMap(new ConcurrentHashMap(256));
    private final ThreadLocal<Object> prototypesCurrentlyInCreation = new NamedThreadLocal("Prototype beans currently in creation");

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract boolean containsBeanDefinition(String str);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract BeanDefinition getBeanDefinition(String str) throws BeansException;

    protected abstract Object createBean(String str, RootBeanDefinition rootBeanDefinition, @Nullable Object[] objArr) throws BeanCreationException;

    public AbstractBeanFactory() {
    }

    public AbstractBeanFactory(@Nullable BeanFactory parentBeanFactory) {
        this.parentBeanFactory = parentBeanFactory;
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null, null, false);
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) doGetBean(name, requiredType, null, false);
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, null, args, false);
    }

    public <T> T getBean(String name, @Nullable Class<T> requiredType, @Nullable Object... args) throws BeansException {
        return (T) doGetBean(name, requiredType, args, false);
    }

    protected <T> T doGetBean(String name, @Nullable Class<T> requiredType, @Nullable Object[] args, boolean typeCheckOnly) throws BeansException {
        Object bean;
        String beanName = transformedBeanName(name);
        Object sharedInstance = getSingleton(beanName);
        if (sharedInstance != null && args == null) {
            if (this.logger.isTraceEnabled()) {
                if (isSingletonCurrentlyInCreation(beanName)) {
                    this.logger.trace("Returning eagerly cached instance of singleton bean '" + beanName + "' that is not fully initialized yet - a consequence of a circular reference");
                } else {
                    this.logger.trace("Returning cached instance of singleton bean '" + beanName + "'");
                }
            }
            bean = getObjectForBeanInstance(sharedInstance, name, beanName, null);
        } else if (isPrototypeCurrentlyInCreation(beanName)) {
            throw new BeanCurrentlyInCreationException(beanName);
        } else {
            BeanFactory parentBeanFactory = getParentBeanFactory();
            if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
                String nameToLookup = originalBeanName(name);
                if (parentBeanFactory instanceof AbstractBeanFactory) {
                    return (T) ((AbstractBeanFactory) parentBeanFactory).doGetBean(nameToLookup, requiredType, args, typeCheckOnly);
                }
                if (args != null) {
                    return (T) parentBeanFactory.getBean(nameToLookup, args);
                }
                if (requiredType != null) {
                    return (T) parentBeanFactory.getBean(nameToLookup, requiredType);
                }
                return (T) parentBeanFactory.getBean(nameToLookup);
            }
            if (!typeCheckOnly) {
                markBeanAsCreated(beanName);
            }
            try {
                RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
                checkMergedBeanDefinition(mbd, beanName, args);
                String[] dependsOn = mbd.getDependsOn();
                if (dependsOn != null) {
                    for (String dep : dependsOn) {
                        if (isDependent(beanName, dep)) {
                            throw new BeanCreationException(mbd.getResourceDescription(), beanName, "Circular depends-on relationship between '" + beanName + "' and '" + dep + "'");
                        }
                        registerDependentBean(dep, beanName);
                        try {
                            getBean(dep);
                        } catch (NoSuchBeanDefinitionException ex) {
                            throw new BeanCreationException(mbd.getResourceDescription(), beanName, "'" + beanName + "' depends on missing bean '" + dep + "'", ex);
                        }
                    }
                }
                if (mbd.isSingleton()) {
                    bean = getObjectForBeanInstance(getSingleton(beanName, () -> {
                        try {
                            return createBean(beanName, mbd, args);
                        } catch (BeansException ex2) {
                            destroySingleton(beanName);
                            throw ex2;
                        }
                    }), name, beanName, mbd);
                } else if (mbd.isPrototype()) {
                    beforePrototypeCreation(beanName);
                    Object prototypeInstance = createBean(beanName, mbd, args);
                    afterPrototypeCreation(beanName);
                    bean = getObjectForBeanInstance(prototypeInstance, name, beanName, mbd);
                } else {
                    String scopeName = mbd.getScope();
                    Scope scope = this.scopes.get(scopeName);
                    if (scope == null) {
                        throw new IllegalStateException("No Scope registered for scope name '" + scopeName + "'");
                    }
                    try {
                        Object scopedInstance = scope.get(beanName, () -> {
                            beforePrototypeCreation(beanName);
                            try {
                                Object createBean = createBean(beanName, mbd, args);
                                afterPrototypeCreation(beanName);
                                return createBean;
                            } catch (Throwable th) {
                                afterPrototypeCreation(beanName);
                                throw th;
                            }
                        });
                        bean = getObjectForBeanInstance(scopedInstance, name, beanName, mbd);
                    } catch (IllegalStateException ex2) {
                        throw new BeanCreationException(beanName, "Scope '" + scopeName + "' is not active for the current thread; consider defining a scoped proxy for this bean if you intend to refer to it from a singleton", ex2);
                    }
                }
            } catch (BeansException ex3) {
                cleanupAfterBeanCreationFailure(beanName);
                throw ex3;
            }
        }
        if (requiredType != null && !requiredType.isInstance(bean)) {
            try {
                T convertedBean = (T) getTypeConverter().convertIfNecessary(bean, requiredType);
                if (convertedBean == null) {
                    throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
                }
                return convertedBean;
            } catch (TypeMismatchException ex4) {
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("Failed to convert bean '" + name + "' to required type '" + ClassUtils.getQualifiedName(requiredType) + "'", ex4);
                }
                throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
            }
        }
        return (T) bean;
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public boolean containsBean(String name) {
        String beanName = transformedBeanName(name);
        if (containsSingleton(beanName) || containsBeanDefinition(beanName)) {
            return !BeanFactoryUtils.isFactoryDereference(name) || isFactoryBean(name);
        }
        BeanFactory parentBeanFactory = getParentBeanFactory();
        return parentBeanFactory != null && parentBeanFactory.containsBean(originalBeanName(name));
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        String beanName = transformedBeanName(name);
        Object beanInstance = getSingleton(beanName, false);
        if (beanInstance != null) {
            return beanInstance instanceof FactoryBean ? BeanFactoryUtils.isFactoryDereference(name) || ((FactoryBean) beanInstance).isSingleton() : !BeanFactoryUtils.isFactoryDereference(name);
        }
        BeanFactory parentBeanFactory = getParentBeanFactory();
        if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
            return parentBeanFactory.isSingleton(originalBeanName(name));
        }
        RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
        if (mbd.isSingleton()) {
            if (!isFactoryBean(beanName, mbd)) {
                return !BeanFactoryUtils.isFactoryDereference(name);
            } else if (BeanFactoryUtils.isFactoryDereference(name)) {
                return true;
            } else {
                FactoryBean<?> factoryBean = (FactoryBean) getBean(BeanFactory.FACTORY_BEAN_PREFIX + beanName);
                return factoryBean.isSingleton();
            }
        }
        return false;
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        String beanName = transformedBeanName(name);
        BeanFactory parentBeanFactory = getParentBeanFactory();
        if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
            return parentBeanFactory.isPrototype(originalBeanName(name));
        }
        RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
        if (mbd.isPrototype()) {
            return !BeanFactoryUtils.isFactoryDereference(name) || isFactoryBean(beanName, mbd);
        } else if (!BeanFactoryUtils.isFactoryDereference(name) && isFactoryBean(beanName, mbd)) {
            FactoryBean<?> fb = (FactoryBean) getBean(BeanFactory.FACTORY_BEAN_PREFIX + beanName);
            if (System.getSecurityManager() != null) {
                return ((Boolean) AccessController.doPrivileged(() -> {
                    return Boolean.valueOf(((fb instanceof SmartFactoryBean) && ((SmartFactoryBean) fb).isPrototype()) || !fb.isSingleton());
                }, getAccessControlContext())).booleanValue();
            }
            return ((fb instanceof SmartFactoryBean) && ((SmartFactoryBean) fb).isPrototype()) || !fb.isSingleton();
        } else {
            return false;
        }
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
        return isTypeMatch(name, typeToMatch, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isTypeMatch(String name, ResolvableType typeToMatch, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException {
        String beanName = transformedBeanName(name);
        boolean isFactoryDereference = BeanFactoryUtils.isFactoryDereference(name);
        Object beanInstance = getSingleton(beanName, false);
        if (beanInstance != null && beanInstance.getClass() != NullBean.class) {
            if (beanInstance instanceof FactoryBean) {
                if (!isFactoryDereference) {
                    Class<?> type = getTypeForFactoryBean((FactoryBean) beanInstance);
                    return type != null && typeToMatch.isAssignableFrom(type);
                }
                return typeToMatch.isInstance(beanInstance);
            } else if (!isFactoryDereference) {
                if (typeToMatch.isInstance(beanInstance)) {
                    return true;
                }
                if (typeToMatch.hasGenerics() && containsBeanDefinition(beanName)) {
                    RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
                    Class<?> targetType = mbd.getTargetType();
                    if (targetType != null && targetType != ClassUtils.getUserClass(beanInstance)) {
                        Class<?> classToMatch = typeToMatch.resolve();
                        if (classToMatch != null && !classToMatch.isInstance(beanInstance)) {
                            return false;
                        }
                        if (typeToMatch.isAssignableFrom(targetType)) {
                            return true;
                        }
                    }
                    ResolvableType resolvableType = mbd.targetType;
                    if (resolvableType == null) {
                        resolvableType = mbd.factoryMethodReturnType;
                    }
                    return resolvableType != null && typeToMatch.isAssignableFrom(resolvableType);
                }
                return false;
            } else {
                return false;
            }
        } else if (containsSingleton(beanName) && !containsBeanDefinition(beanName)) {
            return false;
        } else {
            BeanFactory parentBeanFactory = getParentBeanFactory();
            if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
                return parentBeanFactory.isTypeMatch(originalBeanName(name), typeToMatch);
            }
            RootBeanDefinition mbd2 = getMergedLocalBeanDefinition(beanName);
            BeanDefinitionHolder dbd = mbd2.getDecoratedDefinition();
            Class<?> classToMatch2 = typeToMatch.resolve();
            if (classToMatch2 == null) {
                classToMatch2 = FactoryBean.class;
            }
            Class<?>[] typesToMatch = FactoryBean.class == classToMatch2 ? new Class[]{classToMatch2} : new Class[]{FactoryBean.class, classToMatch2};
            Class<?> predictedType = null;
            if (!isFactoryDereference && dbd != null && isFactoryBean(beanName, mbd2) && (!mbd2.isLazyInit() || allowFactoryBeanInit)) {
                RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd2);
                Class<?> targetType2 = predictBeanType(dbd.getBeanName(), tbd, typesToMatch);
                if (targetType2 != null && !FactoryBean.class.isAssignableFrom(targetType2)) {
                    predictedType = targetType2;
                }
            }
            if (predictedType == null) {
                predictedType = predictBeanType(beanName, mbd2, typesToMatch);
                if (predictedType == null) {
                    return false;
                }
            }
            ResolvableType beanType = null;
            if (FactoryBean.class.isAssignableFrom(predictedType)) {
                if (beanInstance == null && !isFactoryDereference) {
                    beanType = getTypeForFactoryBean(beanName, mbd2, allowFactoryBeanInit);
                    predictedType = beanType.resolve();
                    if (predictedType == null) {
                        return false;
                    }
                }
            } else if (isFactoryDereference) {
                predictedType = predictBeanType(beanName, mbd2, FactoryBean.class);
                if (predictedType == null || !FactoryBean.class.isAssignableFrom(predictedType)) {
                    return false;
                }
            }
            if (beanType == null) {
                ResolvableType definedType = mbd2.targetType;
                if (definedType == null) {
                    definedType = mbd2.factoryMethodReturnType;
                }
                if (definedType != null && definedType.resolve() == predictedType) {
                    beanType = definedType;
                }
            }
            if (beanType != null) {
                return typeToMatch.isAssignableFrom(beanType);
            }
            return typeToMatch.isAssignableFrom(predictedType);
        }
    }

    @Override // org.springframework.beans.factory.BeanFactory
    public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
        return isTypeMatch(name, ResolvableType.forRawClass(typeToMatch));
    }

    @Override // org.springframework.beans.factory.BeanFactory
    @Nullable
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return getType(name, true);
    }

    @Override // org.springframework.beans.factory.BeanFactory
    @Nullable
    public Class<?> getType(String name, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException {
        String beanName = transformedBeanName(name);
        Object beanInstance = getSingleton(beanName, false);
        if (beanInstance != null && beanInstance.getClass() != NullBean.class) {
            if ((beanInstance instanceof FactoryBean) && !BeanFactoryUtils.isFactoryDereference(name)) {
                return getTypeForFactoryBean((FactoryBean) beanInstance);
            }
            return beanInstance.getClass();
        }
        BeanFactory parentBeanFactory = getParentBeanFactory();
        if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
            return parentBeanFactory.getType(originalBeanName(name));
        }
        RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
        BeanDefinitionHolder dbd = mbd.getDecoratedDefinition();
        if (dbd != null && !BeanFactoryUtils.isFactoryDereference(name)) {
            RootBeanDefinition tbd = getMergedBeanDefinition(dbd.getBeanName(), dbd.getBeanDefinition(), mbd);
            Class<?> targetClass = predictBeanType(dbd.getBeanName(), tbd, new Class[0]);
            if (targetClass != null && !FactoryBean.class.isAssignableFrom(targetClass)) {
                return targetClass;
            }
        }
        Class<?> beanClass = predictBeanType(beanName, mbd, new Class[0]);
        if (beanClass != null && FactoryBean.class.isAssignableFrom(beanClass)) {
            if (!BeanFactoryUtils.isFactoryDereference(name)) {
                return getTypeForFactoryBean(beanName, mbd, allowFactoryBeanInit).resolve();
            }
            return beanClass;
        } else if (BeanFactoryUtils.isFactoryDereference(name)) {
            return null;
        } else {
            return beanClass;
        }
    }

    @Override // org.springframework.core.SimpleAliasRegistry, org.springframework.core.AliasRegistry, org.springframework.beans.factory.BeanFactory
    public String[] getAliases(String name) {
        BeanFactory parentBeanFactory;
        String beanName = transformedBeanName(name);
        List<String> aliases = new ArrayList<>();
        boolean factoryPrefix = name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX);
        String fullBeanName = beanName;
        if (factoryPrefix) {
            fullBeanName = BeanFactory.FACTORY_BEAN_PREFIX + beanName;
        }
        if (!fullBeanName.equals(name)) {
            aliases.add(fullBeanName);
        }
        String[] retrievedAliases = super.getAliases(beanName);
        String prefix = factoryPrefix ? BeanFactory.FACTORY_BEAN_PREFIX : "";
        for (String retrievedAlias : retrievedAliases) {
            String alias = prefix + retrievedAlias;
            if (!alias.equals(name)) {
                aliases.add(alias);
            }
        }
        if (!containsSingleton(beanName) && !containsBeanDefinition(beanName) && (parentBeanFactory = getParentBeanFactory()) != null) {
            aliases.addAll(Arrays.asList(parentBeanFactory.getAliases(fullBeanName)));
        }
        return StringUtils.toStringArray(aliases);
    }

    @Override // org.springframework.beans.factory.HierarchicalBeanFactory
    @Nullable
    public BeanFactory getParentBeanFactory() {
        return this.parentBeanFactory;
    }

    @Override // org.springframework.beans.factory.HierarchicalBeanFactory
    public boolean containsLocalBean(String name) {
        String beanName = transformedBeanName(name);
        return (containsSingleton(beanName) || containsBeanDefinition(beanName)) && (!BeanFactoryUtils.isFactoryDereference(name) || isFactoryBean(beanName));
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void setParentBeanFactory(@Nullable BeanFactory parentBeanFactory) {
        if (this.parentBeanFactory != null && this.parentBeanFactory != parentBeanFactory) {
            throw new IllegalStateException("Already associated with parent BeanFactory: " + this.parentBeanFactory);
        }
        if (this == parentBeanFactory) {
            throw new IllegalStateException("Cannot set parent bean factory to self");
        }
        this.parentBeanFactory = parentBeanFactory;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void setBeanClassLoader(@Nullable ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader != null ? beanClassLoader : ClassUtils.getDefaultClassLoader();
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    @Nullable
    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void setTempClassLoader(@Nullable ClassLoader tempClassLoader) {
        this.tempClassLoader = tempClassLoader;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    @Nullable
    public ClassLoader getTempClassLoader() {
        return this.tempClassLoader;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void setCacheBeanMetadata(boolean cacheBeanMetadata) {
        this.cacheBeanMetadata = cacheBeanMetadata;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public boolean isCacheBeanMetadata() {
        return this.cacheBeanMetadata;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void setBeanExpressionResolver(@Nullable BeanExpressionResolver resolver) {
        this.beanExpressionResolver = resolver;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    @Nullable
    public BeanExpressionResolver getBeanExpressionResolver() {
        return this.beanExpressionResolver;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void setConversionService(@Nullable ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    @Nullable
    public ConversionService getConversionService() {
        return this.conversionService;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar) {
        Assert.notNull(registrar, "PropertyEditorRegistrar must not be null");
        this.propertyEditorRegistrars.add(registrar);
    }

    public Set<PropertyEditorRegistrar> getPropertyEditorRegistrars() {
        return this.propertyEditorRegistrars;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass) {
        Assert.notNull(requiredType, "Required type must not be null");
        Assert.notNull(propertyEditorClass, "PropertyEditor class must not be null");
        this.customEditors.put(requiredType, propertyEditorClass);
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void copyRegisteredEditorsTo(PropertyEditorRegistry registry) {
        registerCustomEditors(registry);
    }

    public Map<Class<?>, Class<? extends PropertyEditor>> getCustomEditors() {
        return this.customEditors;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void setTypeConverter(TypeConverter typeConverter) {
        this.typeConverter = typeConverter;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Nullable
    public TypeConverter getCustomTypeConverter() {
        return this.typeConverter;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public TypeConverter getTypeConverter() {
        TypeConverter customConverter = getCustomTypeConverter();
        if (customConverter != null) {
            return customConverter;
        }
        SimpleTypeConverter typeConverter = new SimpleTypeConverter();
        typeConverter.setConversionService(getConversionService());
        registerCustomEditors(typeConverter);
        return typeConverter;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        Assert.notNull(valueResolver, "StringValueResolver must not be null");
        this.embeddedValueResolvers.add(valueResolver);
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public boolean hasEmbeddedValueResolver() {
        return !this.embeddedValueResolvers.isEmpty();
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    @Nullable
    public String resolveEmbeddedValue(@Nullable String value) {
        if (value == null) {
            return null;
        }
        String result = value;
        for (StringValueResolver resolver : this.embeddedValueResolvers) {
            result = resolver.resolveStringValue(result);
            if (result == null) {
                return null;
            }
        }
        return result;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        Assert.notNull(beanPostProcessor, "BeanPostProcessor must not be null");
        this.beanPostProcessors.remove(beanPostProcessor);
        if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
            this.hasInstantiationAwareBeanPostProcessors = true;
        }
        if (beanPostProcessor instanceof DestructionAwareBeanPostProcessor) {
            this.hasDestructionAwareBeanPostProcessors = true;
        }
        this.beanPostProcessors.add(beanPostProcessor);
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public int getBeanPostProcessorCount() {
        return this.beanPostProcessors.size();
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean hasInstantiationAwareBeanPostProcessors() {
        return this.hasInstantiationAwareBeanPostProcessors;
    }

    protected boolean hasDestructionAwareBeanPostProcessors() {
        return this.hasDestructionAwareBeanPostProcessors;
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void registerScope(String scopeName, Scope scope) {
        Assert.notNull(scopeName, "Scope identifier must not be null");
        Assert.notNull(scope, "Scope must not be null");
        if ("singleton".equals(scopeName) || "prototype".equals(scopeName)) {
            throw new IllegalArgumentException("Cannot replace existing scopes 'singleton' and 'prototype'");
        }
        Scope previous = this.scopes.put(scopeName, scope);
        if (previous != null && previous != scope) {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Replacing scope '" + scopeName + "' from [" + previous + "] to [" + scope + "]");
            }
        } else if (this.logger.isTraceEnabled()) {
            this.logger.trace("Registering scope '" + scopeName + "' with implementation [" + scope + "]");
        }
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public String[] getRegisteredScopeNames() {
        return StringUtils.toStringArray(this.scopes.keySet());
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    @Nullable
    public Scope getRegisteredScope(String scopeName) {
        Assert.notNull(scopeName, "Scope identifier must not be null");
        return this.scopes.get(scopeName);
    }

    public void setSecurityContextProvider(SecurityContextProvider securityProvider) {
        this.securityContextProvider = securityProvider;
    }

    @Override // org.springframework.beans.factory.support.FactoryBeanRegistrySupport, org.springframework.beans.factory.config.ConfigurableBeanFactory
    public AccessControlContext getAccessControlContext() {
        if (this.securityContextProvider != null) {
            return this.securityContextProvider.getAccessControlContext();
        }
        return AccessController.getContext();
    }

    public void copyConfigurationFrom(ConfigurableBeanFactory otherFactory) {
        Assert.notNull(otherFactory, "BeanFactory must not be null");
        setBeanClassLoader(otherFactory.getBeanClassLoader());
        setCacheBeanMetadata(otherFactory.isCacheBeanMetadata());
        setBeanExpressionResolver(otherFactory.getBeanExpressionResolver());
        setConversionService(otherFactory.getConversionService());
        if (otherFactory instanceof AbstractBeanFactory) {
            AbstractBeanFactory otherAbstractFactory = (AbstractBeanFactory) otherFactory;
            this.propertyEditorRegistrars.addAll(otherAbstractFactory.propertyEditorRegistrars);
            this.customEditors.putAll(otherAbstractFactory.customEditors);
            this.typeConverter = otherAbstractFactory.typeConverter;
            this.beanPostProcessors.addAll(otherAbstractFactory.beanPostProcessors);
            this.hasInstantiationAwareBeanPostProcessors = this.hasInstantiationAwareBeanPostProcessors || otherAbstractFactory.hasInstantiationAwareBeanPostProcessors;
            this.hasDestructionAwareBeanPostProcessors = this.hasDestructionAwareBeanPostProcessors || otherAbstractFactory.hasDestructionAwareBeanPostProcessors;
            this.scopes.putAll(otherAbstractFactory.scopes);
            this.securityContextProvider = otherAbstractFactory.securityContextProvider;
            return;
        }
        setTypeConverter(otherFactory.getTypeConverter());
        String[] otherScopeNames = otherFactory.getRegisteredScopeNames();
        for (String scopeName : otherScopeNames) {
            this.scopes.put(scopeName, otherFactory.getRegisteredScope(scopeName));
        }
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public BeanDefinition getMergedBeanDefinition(String name) throws BeansException {
        String beanName = transformedBeanName(name);
        if (!containsBeanDefinition(beanName) && (getParentBeanFactory() instanceof ConfigurableBeanFactory)) {
            return ((ConfigurableBeanFactory) getParentBeanFactory()).getMergedBeanDefinition(beanName);
        }
        return getMergedLocalBeanDefinition(beanName);
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException {
        String beanName = transformedBeanName(name);
        Object beanInstance = getSingleton(beanName, false);
        if (beanInstance != null) {
            return beanInstance instanceof FactoryBean;
        }
        if (!containsBeanDefinition(beanName) && (getParentBeanFactory() instanceof ConfigurableBeanFactory)) {
            return ((ConfigurableBeanFactory) getParentBeanFactory()).isFactoryBean(name);
        }
        return isFactoryBean(beanName, getMergedLocalBeanDefinition(beanName));
    }

    @Override // org.springframework.beans.factory.support.DefaultSingletonBeanRegistry
    public boolean isActuallyInCreation(String beanName) {
        return isSingletonCurrentlyInCreation(beanName) || isPrototypeCurrentlyInCreation(beanName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isPrototypeCurrentlyInCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        return curVal != null && (curVal.equals(beanName) || ((curVal instanceof Set) && ((Set) curVal).contains(beanName)));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void beforePrototypeCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        if (curVal == null) {
            this.prototypesCurrentlyInCreation.set(beanName);
        } else if (curVal instanceof String) {
            Set<String> beanNameSet = new HashSet<>(2);
            beanNameSet.add((String) curVal);
            beanNameSet.add(beanName);
            this.prototypesCurrentlyInCreation.set(beanNameSet);
        } else {
            ((Set) curVal).add(beanName);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void afterPrototypeCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        if (curVal instanceof String) {
            this.prototypesCurrentlyInCreation.remove();
        } else if (curVal instanceof Set) {
            Set<String> beanNameSet = (Set) curVal;
            beanNameSet.remove(beanName);
            if (beanNameSet.isEmpty()) {
                this.prototypesCurrentlyInCreation.remove();
            }
        }
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void destroyBean(String beanName, Object beanInstance) {
        destroyBean(beanName, beanInstance, getMergedLocalBeanDefinition(beanName));
    }

    protected void destroyBean(String beanName, Object bean, RootBeanDefinition mbd) {
        new DisposableBeanAdapter(bean, beanName, mbd, getBeanPostProcessors(), getAccessControlContext()).destroy();
    }

    @Override // org.springframework.beans.factory.config.ConfigurableBeanFactory
    public void destroyScopedBean(String beanName) {
        RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
        if (mbd.isSingleton() || mbd.isPrototype()) {
            throw new IllegalArgumentException("Bean name '" + beanName + "' does not correspond to an object in a mutable scope");
        }
        String scopeName = mbd.getScope();
        Scope scope = this.scopes.get(scopeName);
        if (scope == null) {
            throw new IllegalStateException("No Scope SPI registered for scope name '" + scopeName + "'");
        }
        Object bean = scope.remove(beanName);
        if (bean != null) {
            destroyBean(beanName, bean, mbd);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String transformedBeanName(String name) {
        return canonicalName(BeanFactoryUtils.transformedBeanName(name));
    }

    protected String originalBeanName(String name) {
        String beanName = transformedBeanName(name);
        if (name.startsWith(BeanFactory.FACTORY_BEAN_PREFIX)) {
            beanName = BeanFactory.FACTORY_BEAN_PREFIX + beanName;
        }
        return beanName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initBeanWrapper(BeanWrapper bw) {
        bw.setConversionService(getConversionService());
        registerCustomEditors(bw);
    }

    protected void registerCustomEditors(PropertyEditorRegistry registry) {
        PropertyEditorRegistrySupport registrySupport = registry instanceof PropertyEditorRegistrySupport ? (PropertyEditorRegistrySupport) registry : null;
        if (registrySupport != null) {
            registrySupport.useConfigValueEditors();
        }
        if (!this.propertyEditorRegistrars.isEmpty()) {
            for (PropertyEditorRegistrar registrar : this.propertyEditorRegistrars) {
                try {
                    registrar.registerCustomEditors(registry);
                } catch (BeanCreationException ex) {
                    Throwable rootCause = ex.getMostSpecificCause();
                    if (rootCause instanceof BeanCurrentlyInCreationException) {
                        BeanCreationException bce = (BeanCreationException) rootCause;
                        String bceBeanName = bce.getBeanName();
                        if (bceBeanName != null && isCurrentlyInCreation(bceBeanName)) {
                            if (this.logger.isDebugEnabled()) {
                                this.logger.debug("PropertyEditorRegistrar [" + registrar.getClass().getName() + "] failed because it tried to obtain currently created bean '" + ex.getBeanName() + "': " + ex.getMessage());
                            }
                            onSuppressedException(ex);
                        }
                    }
                    throw ex;
                }
            }
        }
        if (!this.customEditors.isEmpty()) {
            this.customEditors.forEach(requiredType, editorClass -> {
                registry.registerCustomEditor(requiredType, (PropertyEditor) BeanUtils.instantiateClass(editorClass));
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RootBeanDefinition getMergedLocalBeanDefinition(String beanName) throws BeansException {
        RootBeanDefinition mbd = this.mergedBeanDefinitions.get(beanName);
        if (mbd != null && !mbd.stale) {
            return mbd;
        }
        return getMergedBeanDefinition(beanName, getBeanDefinition(beanName));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd) throws BeanDefinitionStoreException {
        return getMergedBeanDefinition(beanName, bd, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd, @Nullable BeanDefinition containingBd) throws BeanDefinitionStoreException {
        BeanDefinition pbd;
        RootBeanDefinition rootBeanDefinition;
        synchronized (this.mergedBeanDefinitions) {
            RootBeanDefinition mbd = null;
            RootBeanDefinition previous = null;
            if (containingBd == null) {
                mbd = this.mergedBeanDefinitions.get(beanName);
            }
            if (mbd == null || mbd.stale) {
                previous = mbd;
                if (bd.getParentName() == null) {
                    if (bd instanceof RootBeanDefinition) {
                        mbd = ((RootBeanDefinition) bd).cloneBeanDefinition();
                    } else {
                        mbd = new RootBeanDefinition(bd);
                    }
                } else {
                    try {
                        String parentBeanName = transformedBeanName(bd.getParentName());
                        if (!beanName.equals(parentBeanName)) {
                            pbd = getMergedBeanDefinition(parentBeanName);
                        } else {
                            BeanFactory parent = getParentBeanFactory();
                            if (parent instanceof ConfigurableBeanFactory) {
                                pbd = ((ConfigurableBeanFactory) parent).getMergedBeanDefinition(parentBeanName);
                            } else {
                                throw new NoSuchBeanDefinitionException(parentBeanName, "Parent name '" + parentBeanName + "' is equal to bean name '" + beanName + "': cannot be resolved without a ConfigurableBeanFactory parent");
                            }
                        }
                        mbd = new RootBeanDefinition(pbd);
                        mbd.overrideFrom(bd);
                    } catch (NoSuchBeanDefinitionException ex) {
                        throw new BeanDefinitionStoreException(bd.getResourceDescription(), beanName, "Could not resolve parent bean definition '" + bd.getParentName() + "'", ex);
                    }
                }
                if (!StringUtils.hasLength(mbd.getScope())) {
                    mbd.setScope("singleton");
                }
                if (containingBd != null && !containingBd.isSingleton() && mbd.isSingleton()) {
                    mbd.setScope(containingBd.getScope());
                }
                if (containingBd == null && isCacheBeanMetadata()) {
                    this.mergedBeanDefinitions.put(beanName, mbd);
                }
            }
            if (previous != null) {
                copyRelevantMergedBeanDefinitionCaches(previous, mbd);
            }
            rootBeanDefinition = mbd;
        }
        return rootBeanDefinition;
    }

    private void copyRelevantMergedBeanDefinitionCaches(RootBeanDefinition previous, RootBeanDefinition mbd) {
        if (ObjectUtils.nullSafeEquals(mbd.getBeanClassName(), previous.getBeanClassName()) && ObjectUtils.nullSafeEquals(mbd.getFactoryBeanName(), previous.getFactoryBeanName()) && ObjectUtils.nullSafeEquals(mbd.getFactoryMethodName(), previous.getFactoryMethodName())) {
            ResolvableType targetType = mbd.targetType;
            ResolvableType previousTargetType = previous.targetType;
            if (targetType == null || targetType.equals(previousTargetType)) {
                mbd.targetType = previousTargetType;
                mbd.isFactoryBean = previous.isFactoryBean;
                mbd.resolvedTargetType = previous.resolvedTargetType;
                mbd.factoryMethodReturnType = previous.factoryMethodReturnType;
                mbd.factoryMethodToIntrospect = previous.factoryMethodToIntrospect;
            }
        }
    }

    protected void checkMergedBeanDefinition(RootBeanDefinition mbd, String beanName, @Nullable Object[] args) throws BeanDefinitionStoreException {
        if (mbd.isAbstract()) {
            throw new BeanIsAbstractException(beanName);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void clearMergedBeanDefinition(String beanName) {
        RootBeanDefinition bd = this.mergedBeanDefinitions.get(beanName);
        if (bd != null) {
            bd.stale = true;
        }
    }

    public void clearMetadataCache() {
        this.mergedBeanDefinitions.forEach(beanName, bd -> {
            if (!isBeanEligibleForMetadataCaching(beanName)) {
                bd.stale = true;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Nullable
    public Class<?> resolveBeanClass(RootBeanDefinition mbd, String beanName, Class<?>... typesToMatch) throws CannotLoadBeanClassException {
        try {
            if (mbd.hasBeanClass()) {
                return mbd.getBeanClass();
            }
            if (System.getSecurityManager() != null) {
                return (Class) AccessController.doPrivileged(() -> {
                    return doResolveBeanClass(mbd, typesToMatch);
                }, getAccessControlContext());
            }
            return doResolveBeanClass(mbd, typesToMatch);
        } catch (ClassNotFoundException ex) {
            throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
        } catch (LinkageError err) {
            throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), err);
        } catch (PrivilegedActionException pae) {
            ClassNotFoundException ex2 = (ClassNotFoundException) pae.getException();
            throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex2);
        }
    }

    @Nullable
    private Class<?> doResolveBeanClass(RootBeanDefinition mbd, Class<?>... typesToMatch) throws ClassNotFoundException {
        ClassLoader tempClassLoader;
        ClassLoader beanClassLoader = getBeanClassLoader();
        ClassLoader dynamicLoader = beanClassLoader;
        boolean freshResolve = false;
        if (!ObjectUtils.isEmpty((Object[]) typesToMatch) && (tempClassLoader = getTempClassLoader()) != null) {
            dynamicLoader = tempClassLoader;
            freshResolve = true;
            if (tempClassLoader instanceof DecoratingClassLoader) {
                DecoratingClassLoader dcl = (DecoratingClassLoader) tempClassLoader;
                for (Class<?> typeToMatch : typesToMatch) {
                    dcl.excludeClass(typeToMatch.getName());
                }
            }
        }
        String className = mbd.getBeanClassName();
        if (className != null) {
            Object evaluated = evaluateBeanDefinitionString(className, mbd);
            if (!className.equals(evaluated)) {
                if (evaluated instanceof Class) {
                    return (Class) evaluated;
                }
                if (evaluated instanceof String) {
                    className = (String) evaluated;
                    freshResolve = true;
                } else {
                    throw new IllegalStateException("Invalid class name expression result: " + evaluated);
                }
            }
            if (freshResolve) {
                if (dynamicLoader != null) {
                    try {
                        return dynamicLoader.loadClass(className);
                    } catch (ClassNotFoundException ex) {
                        if (this.logger.isTraceEnabled()) {
                            this.logger.trace("Could not load class [" + className + "] from " + dynamicLoader + ": " + ex);
                        }
                    }
                }
                return ClassUtils.forName(className, dynamicLoader);
            }
        }
        return mbd.resolveBeanClass(beanClassLoader);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Nullable
    public Object evaluateBeanDefinitionString(@Nullable String value, @Nullable BeanDefinition beanDefinition) {
        String scopeName;
        if (this.beanExpressionResolver == null) {
            return value;
        }
        Scope scope = null;
        if (beanDefinition != null && (scopeName = beanDefinition.getScope()) != null) {
            scope = getRegisteredScope(scopeName);
        }
        return this.beanExpressionResolver.evaluate(value, new BeanExpressionContext(this, scope));
    }

    @Nullable
    protected Class<?> predictBeanType(String beanName, RootBeanDefinition mbd, Class<?>... typesToMatch) {
        Class<?> targetType = mbd.getTargetType();
        if (targetType != null) {
            return targetType;
        }
        if (mbd.getFactoryMethodName() != null) {
            return null;
        }
        return resolveBeanClass(mbd, beanName, typesToMatch);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isFactoryBean(String beanName, RootBeanDefinition mbd) {
        Boolean result = mbd.isFactoryBean;
        if (result == null) {
            Class<?> beanType = predictBeanType(beanName, mbd, FactoryBean.class);
            result = Boolean.valueOf(beanType != null && FactoryBean.class.isAssignableFrom(beanType));
            mbd.isFactoryBean = result;
        }
        return result.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ResolvableType getTypeForFactoryBean(String beanName, RootBeanDefinition mbd, boolean allowInit) {
        ResolvableType result = getTypeForFactoryBeanFromAttributes(mbd);
        if (result != ResolvableType.NONE) {
            return result;
        }
        if (allowInit && mbd.isSingleton()) {
            try {
                FactoryBean<?> factoryBean = (FactoryBean) doGetBean(BeanFactory.FACTORY_BEAN_PREFIX + beanName, FactoryBean.class, null, true);
                Class<?> objectType = getTypeForFactoryBean(factoryBean);
                return objectType != null ? ResolvableType.forClass(objectType) : ResolvableType.NONE;
            } catch (BeanCreationException ex) {
                if (ex.contains(BeanCurrentlyInCreationException.class)) {
                    this.logger.trace(LogMessage.format("Bean currently in creation on FactoryBean type check: %s", ex));
                } else if (mbd.isLazyInit()) {
                    this.logger.trace(LogMessage.format("Bean creation exception on lazy FactoryBean type check: %s", ex));
                } else {
                    this.logger.debug(LogMessage.format("Bean creation exception on non-lazy FactoryBean type check: %s", ex));
                }
                onSuppressedException(ex);
            }
        }
        return ResolvableType.NONE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ResolvableType getTypeForFactoryBeanFromAttributes(AttributeAccessor attributes) {
        Object attribute = attributes.getAttribute(FactoryBean.OBJECT_TYPE_ATTRIBUTE);
        if (attribute instanceof ResolvableType) {
            return (ResolvableType) attribute;
        }
        if (attribute instanceof Class) {
            return ResolvableType.forClass((Class) attribute);
        }
        return ResolvableType.NONE;
    }

    @Nullable
    @Deprecated
    protected Class<?> getTypeForFactoryBean(String beanName, RootBeanDefinition mbd) {
        return getTypeForFactoryBean(beanName, mbd, true).resolve();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void markBeanAsCreated(String beanName) {
        if (!this.alreadyCreated.contains(beanName)) {
            synchronized (this.mergedBeanDefinitions) {
                if (!this.alreadyCreated.contains(beanName)) {
                    clearMergedBeanDefinition(beanName);
                    this.alreadyCreated.add(beanName);
                }
            }
        }
    }

    protected void cleanupAfterBeanCreationFailure(String beanName) {
        synchronized (this.mergedBeanDefinitions) {
            this.alreadyCreated.remove(beanName);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isBeanEligibleForMetadataCaching(String beanName) {
        return this.alreadyCreated.contains(beanName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean removeSingletonIfCreatedForTypeCheckOnly(String beanName) {
        if (!this.alreadyCreated.contains(beanName)) {
            removeSingleton(beanName);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean hasBeanCreationStarted() {
        return !this.alreadyCreated.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Object getObjectForBeanInstance(Object beanInstance, String name, String beanName, @Nullable RootBeanDefinition mbd) {
        if (BeanFactoryUtils.isFactoryDereference(name)) {
            if (beanInstance instanceof NullBean) {
                return beanInstance;
            }
            if (!(beanInstance instanceof FactoryBean)) {
                throw new BeanIsNotAFactoryException(beanName, beanInstance.getClass());
            }
            if (mbd != null) {
                mbd.isFactoryBean = true;
            }
            return beanInstance;
        } else if (!(beanInstance instanceof FactoryBean)) {
            return beanInstance;
        } else {
            Object object = null;
            if (mbd != null) {
                mbd.isFactoryBean = true;
            } else {
                object = getCachedObjectForFactoryBean(beanName);
            }
            if (object == null) {
                FactoryBean<?> factory = (FactoryBean) beanInstance;
                if (mbd == null && containsBeanDefinition(beanName)) {
                    mbd = getMergedLocalBeanDefinition(beanName);
                }
                boolean synthetic = mbd != null && mbd.isSynthetic();
                object = getObjectFromFactoryBean(factory, beanName, !synthetic);
            }
            return object;
        }
    }

    public boolean isBeanNameInUse(String beanName) {
        return isAlias(beanName) || containsLocalBean(beanName) || hasDependentBean(beanName);
    }

    protected boolean requiresDestruction(Object bean, RootBeanDefinition mbd) {
        return bean.getClass() != NullBean.class && (DisposableBeanAdapter.hasDestroyMethod(bean, mbd) || (hasDestructionAwareBeanPostProcessors() && DisposableBeanAdapter.hasApplicableProcessors(bean, getBeanPostProcessors())));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void registerDisposableBeanIfNecessary(String beanName, Object bean, RootBeanDefinition mbd) {
        AccessControlContext acc = System.getSecurityManager() != null ? getAccessControlContext() : null;
        if (!mbd.isPrototype() && requiresDestruction(bean, mbd)) {
            if (mbd.isSingleton()) {
                registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, mbd, getBeanPostProcessors(), acc));
                return;
            }
            Scope scope = this.scopes.get(mbd.getScope());
            if (scope == null) {
                throw new IllegalStateException("No Scope registered for scope name '" + mbd.getScope() + "'");
            }
            scope.registerDestructionCallback(beanName, new DisposableBeanAdapter(bean, beanName, mbd, getBeanPostProcessors(), acc));
        }
    }
}
