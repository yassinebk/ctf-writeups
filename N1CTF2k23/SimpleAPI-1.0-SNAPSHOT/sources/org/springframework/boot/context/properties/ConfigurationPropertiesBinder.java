package org.springframework.boot.context.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.BoundPropertiesTrackingBindHandler;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.bind.handler.IgnoreErrorsBindHandler;
import org.springframework.boot.context.properties.bind.handler.IgnoreTopLevelConverterNotFoundBindHandler;
import org.springframework.boot.context.properties.bind.handler.NoUnboundElementsBindHandler;
import org.springframework.boot.context.properties.bind.validation.ValidationBindHandler;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.context.properties.source.UnboundElementsSourceFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.env.PropertySources;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationPropertiesBinder.class */
class ConfigurationPropertiesBinder {
    private static final String BEAN_NAME = "org.springframework.boot.context.internalConfigurationPropertiesBinder";
    private static final String FACTORY_BEAN_NAME = "org.springframework.boot.context.internalConfigurationPropertiesBinderFactory";
    private static final String VALIDATOR_BEAN_NAME = "configurationPropertiesValidator";
    private final ApplicationContext applicationContext;
    private final PropertySources propertySources;
    private final Validator configurationPropertiesValidator;
    private final boolean jsr303Present;
    private volatile Validator jsr303Validator;
    private volatile Binder binder;

    ConfigurationPropertiesBinder(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.propertySources = new PropertySourcesDeducer(applicationContext).getPropertySources();
        this.configurationPropertiesValidator = getConfigurationPropertiesValidator(applicationContext);
        this.jsr303Present = ConfigurationPropertiesJsr303Validator.isJsr303Present(applicationContext);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BindResult<?> bind(ConfigurationPropertiesBean propertiesBean) {
        Bindable<?> target = propertiesBean.asBindTarget();
        ConfigurationProperties annotation = propertiesBean.getAnnotation();
        BindHandler bindHandler = getBindHandler(target, annotation);
        return getBinder().bind(annotation.prefix(), target, bindHandler);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object bindOrCreate(ConfigurationPropertiesBean propertiesBean) {
        Bindable<?> target = propertiesBean.asBindTarget();
        ConfigurationProperties annotation = propertiesBean.getAnnotation();
        BindHandler bindHandler = getBindHandler(target, annotation);
        return getBinder().bindOrCreate(annotation.prefix(), target, bindHandler);
    }

    private Validator getConfigurationPropertiesValidator(ApplicationContext applicationContext) {
        if (applicationContext.containsBean("configurationPropertiesValidator")) {
            return (Validator) applicationContext.getBean("configurationPropertiesValidator", Validator.class);
        }
        return null;
    }

    private <T> BindHandler getBindHandler(Bindable<T> target, ConfigurationProperties annotation) {
        List<Validator> validators = getValidators(target);
        BindHandler handler = getHandler();
        if (annotation.ignoreInvalidFields()) {
            handler = new IgnoreErrorsBindHandler(handler);
        }
        if (!annotation.ignoreUnknownFields()) {
            UnboundElementsSourceFilter filter = new UnboundElementsSourceFilter();
            handler = new NoUnboundElementsBindHandler(handler, filter);
        }
        if (!validators.isEmpty()) {
            handler = new ValidationBindHandler(handler, (Validator[]) validators.toArray(new Validator[0]));
        }
        for (ConfigurationPropertiesBindHandlerAdvisor advisor : getBindHandlerAdvisors()) {
            handler = advisor.apply(handler);
        }
        return handler;
    }

    private IgnoreTopLevelConverterNotFoundBindHandler getHandler() {
        BoundConfigurationProperties bound = BoundConfigurationProperties.get(this.applicationContext);
        if (bound != null) {
            bound.getClass();
            return new IgnoreTopLevelConverterNotFoundBindHandler(new BoundPropertiesTrackingBindHandler(this::add));
        }
        return new IgnoreTopLevelConverterNotFoundBindHandler();
    }

    private List<Validator> getValidators(Bindable<?> target) {
        List<Validator> validators = new ArrayList<>(3);
        if (this.configurationPropertiesValidator != null) {
            validators.add(this.configurationPropertiesValidator);
        }
        if (this.jsr303Present && target.getAnnotation(Validated.class) != null) {
            validators.add(getJsr303Validator());
        }
        if (target.getValue() != null && (target.getValue().get() instanceof Validator)) {
            validators.add((Validator) target.getValue().get());
        }
        return validators;
    }

    private Validator getJsr303Validator() {
        if (this.jsr303Validator == null) {
            this.jsr303Validator = new ConfigurationPropertiesJsr303Validator(this.applicationContext);
        }
        return this.jsr303Validator;
    }

    private List<ConfigurationPropertiesBindHandlerAdvisor> getBindHandlerAdvisors() {
        return (List) this.applicationContext.getBeanProvider(ConfigurationPropertiesBindHandlerAdvisor.class).orderedStream().collect(Collectors.toList());
    }

    private Binder getBinder() {
        if (this.binder == null) {
            this.binder = new Binder(getConfigurationPropertySources(), getPropertySourcesPlaceholdersResolver(), getConversionService(), getPropertyEditorInitializer(), null, ConfigurationPropertiesBindConstructorProvider.INSTANCE);
        }
        return this.binder;
    }

    private Iterable<ConfigurationPropertySource> getConfigurationPropertySources() {
        return ConfigurationPropertySources.from(this.propertySources);
    }

    private PropertySourcesPlaceholdersResolver getPropertySourcesPlaceholdersResolver() {
        return new PropertySourcesPlaceholdersResolver(this.propertySources);
    }

    private ConversionService getConversionService() {
        return new ConversionServiceDeducer(this.applicationContext).getConversionService();
    }

    private Consumer<PropertyEditorRegistry> getPropertyEditorInitializer() {
        if (this.applicationContext instanceof ConfigurableApplicationContext) {
            ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) this.applicationContext).getBeanFactory();
            beanFactory.getClass();
            return this::copyRegisteredEditorsTo;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void register(BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(FACTORY_BEAN_NAME)) {
            GenericBeanDefinition definition = new GenericBeanDefinition();
            definition.setBeanClass(Factory.class);
            definition.setRole(2);
            registry.registerBeanDefinition(FACTORY_BEAN_NAME, definition);
        }
        if (!registry.containsBeanDefinition(BEAN_NAME)) {
            GenericBeanDefinition definition2 = new GenericBeanDefinition();
            definition2.setBeanClass(ConfigurationPropertiesBinder.class);
            definition2.setRole(2);
            definition2.setFactoryBeanName(FACTORY_BEAN_NAME);
            definition2.setFactoryMethodName("create");
            registry.registerBeanDefinition(BEAN_NAME, definition2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ConfigurationPropertiesBinder get(BeanFactory beanFactory) {
        return (ConfigurationPropertiesBinder) beanFactory.getBean(BEAN_NAME, ConfigurationPropertiesBinder.class);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationPropertiesBinder$Factory.class */
    static class Factory implements ApplicationContextAware {
        private ApplicationContext applicationContext;

        Factory() {
        }

        @Override // org.springframework.context.ApplicationContextAware
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

        ConfigurationPropertiesBinder create() {
            return new ConfigurationPropertiesBinder(this.applicationContext);
        }
    }
}
