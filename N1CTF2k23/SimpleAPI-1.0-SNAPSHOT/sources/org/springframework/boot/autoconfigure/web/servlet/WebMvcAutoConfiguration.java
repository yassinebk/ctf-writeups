package org.springframework.boot.autoconfigure.web.servlet;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import javax.servlet.Servlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProviders;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidatorAdapter;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.boot.autoconfigure.web.format.WebConversionService;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.boot.web.servlet.filter.OrderedFormContentFilter;
import org.springframework.boot.web.servlet.filter.OrderedHiddenHttpMethodFilter;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceChainRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.EncodedResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import org.springframework.web.servlet.resource.VersionResourceResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
@AutoConfigureAfter({DispatcherServletAutoConfiguration.class, TaskExecutionAutoConfiguration.class, ValidationAutoConfiguration.class})
@ConditionalOnMissingBean({WebMvcConfigurationSupport.class})
@AutoConfigureOrder(ConfigFileApplicationListener.DEFAULT_ORDER)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration.class */
public class WebMvcAutoConfiguration {
    public static final String DEFAULT_PREFIX = "";
    public static final String DEFAULT_SUFFIX = "";
    private static final String[] SERVLET_LOCATIONS = {"/"};

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration$ResourceHandlerRegistrationCustomizer.class */
    public interface ResourceHandlerRegistrationCustomizer {
        void customize(ResourceHandlerRegistration registration);
    }

    @ConditionalOnMissingBean({HiddenHttpMethodFilter.class})
    @ConditionalOnProperty(prefix = "spring.mvc.hiddenmethod.filter", name = {"enabled"}, matchIfMissing = false)
    @Bean
    public OrderedHiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new OrderedHiddenHttpMethodFilter();
    }

    @ConditionalOnMissingBean({FormContentFilter.class})
    @ConditionalOnProperty(prefix = "spring.mvc.formcontent.filter", name = {"enabled"}, matchIfMissing = true)
    @Bean
    public OrderedFormContentFilter formContentFilter() {
        return new OrderedFormContentFilter();
    }

    static String[] getResourceLocations(String[] staticLocations) {
        String[] locations = new String[staticLocations.length + SERVLET_LOCATIONS.length];
        System.arraycopy(staticLocations, 0, locations, 0, staticLocations.length);
        System.arraycopy(SERVLET_LOCATIONS, 0, locations, staticLocations.length, SERVLET_LOCATIONS.length);
        return locations;
    }

    @EnableConfigurationProperties({WebMvcProperties.class, ResourceProperties.class})
    @Configuration(proxyBeanMethods = false)
    @Import({EnableWebMvcConfiguration.class})
    @Order(0)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration$WebMvcAutoConfigurationAdapter.class */
    public static class WebMvcAutoConfigurationAdapter implements WebMvcConfigurer {
        private static final Log logger = LogFactory.getLog(WebMvcConfigurer.class);
        private final ResourceProperties resourceProperties;
        private final WebMvcProperties mvcProperties;
        private final ListableBeanFactory beanFactory;
        private final ObjectProvider<HttpMessageConverters> messageConvertersProvider;
        final ResourceHandlerRegistrationCustomizer resourceHandlerRegistrationCustomizer;

        public WebMvcAutoConfigurationAdapter(ResourceProperties resourceProperties, WebMvcProperties mvcProperties, ListableBeanFactory beanFactory, ObjectProvider<HttpMessageConverters> messageConvertersProvider, ObjectProvider<ResourceHandlerRegistrationCustomizer> resourceHandlerRegistrationCustomizerProvider) {
            this.resourceProperties = resourceProperties;
            this.mvcProperties = mvcProperties;
            this.beanFactory = beanFactory;
            this.messageConvertersProvider = messageConvertersProvider;
            this.resourceHandlerRegistrationCustomizer = resourceHandlerRegistrationCustomizerProvider.getIfAvailable();
        }

        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurer
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            this.messageConvertersProvider.ifAvailable(customConverters -> {
                converters.addAll(customConverters.getConverters());
            });
        }

        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurer
        public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
            if (this.beanFactory.containsBean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)) {
                Object taskExecutor = this.beanFactory.getBean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME);
                if (taskExecutor instanceof AsyncTaskExecutor) {
                    configurer.setTaskExecutor((AsyncTaskExecutor) taskExecutor);
                }
            }
            Duration timeout = this.mvcProperties.getAsync().getRequestTimeout();
            if (timeout != null) {
                configurer.setDefaultTimeout(timeout.toMillis());
            }
        }

        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurer
        public void configurePathMatch(PathMatchConfigurer configurer) {
            configurer.setUseSuffixPatternMatch(Boolean.valueOf(this.mvcProperties.getPathmatch().isUseSuffixPattern()));
            configurer.setUseRegisteredSuffixPatternMatch(Boolean.valueOf(this.mvcProperties.getPathmatch().isUseRegisteredSuffixPattern()));
        }

        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurer
        public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
            WebMvcProperties.Contentnegotiation contentnegotiation = this.mvcProperties.getContentnegotiation();
            configurer.favorPathExtension(contentnegotiation.isFavorPathExtension());
            configurer.favorParameter(contentnegotiation.isFavorParameter());
            if (contentnegotiation.getParameterName() != null) {
                configurer.parameterName(contentnegotiation.getParameterName());
            }
            Map<String, MediaType> mediaTypes = this.mvcProperties.getContentnegotiation().getMediaTypes();
            configurer.getClass();
            mediaTypes.forEach(this::mediaType);
        }

        @ConditionalOnMissingBean
        @Bean
        public InternalResourceViewResolver defaultViewResolver() {
            InternalResourceViewResolver resolver = new InternalResourceViewResolver();
            resolver.setPrefix(this.mvcProperties.getView().getPrefix());
            resolver.setSuffix(this.mvcProperties.getView().getSuffix());
            return resolver;
        }

        @ConditionalOnMissingBean
        @ConditionalOnBean({View.class})
        @Bean
        public BeanNameViewResolver beanNameViewResolver() {
            BeanNameViewResolver resolver = new BeanNameViewResolver();
            resolver.setOrder(2147483637);
            return resolver;
        }

        @ConditionalOnMissingBean(name = {DispatcherServlet.VIEW_RESOLVER_BEAN_NAME}, value = {ContentNegotiatingViewResolver.class})
        @ConditionalOnBean({ViewResolver.class})
        @Bean
        public ContentNegotiatingViewResolver viewResolver(BeanFactory beanFactory) {
            ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
            resolver.setContentNegotiationManager((ContentNegotiationManager) beanFactory.getBean(ContentNegotiationManager.class));
            resolver.setOrder(Integer.MIN_VALUE);
            return resolver;
        }

        @ConditionalOnMissingBean
        @ConditionalOnProperty(prefix = "spring.mvc", name = {LocaleChangeInterceptor.DEFAULT_PARAM_NAME})
        @Bean
        public LocaleResolver localeResolver() {
            if (this.mvcProperties.getLocaleResolver() == WebMvcProperties.LocaleResolver.FIXED) {
                return new FixedLocaleResolver(this.mvcProperties.getLocale());
            }
            AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
            localeResolver.setDefaultLocale(this.mvcProperties.getLocale());
            return localeResolver;
        }

        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurer
        public MessageCodesResolver getMessageCodesResolver() {
            if (this.mvcProperties.getMessageCodesResolverFormat() != null) {
                DefaultMessageCodesResolver resolver = new DefaultMessageCodesResolver();
                resolver.setMessageCodeFormatter(this.mvcProperties.getMessageCodesResolverFormat());
                return resolver;
            }
            return null;
        }

        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurer
        public void addFormatters(FormatterRegistry registry) {
            ApplicationConversionService.addBeans(registry, this.beanFactory);
        }

        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurer
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            if (!this.resourceProperties.isAddMappings()) {
                logger.debug("Default resource handling disabled");
                return;
            }
            Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
            CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
            if (!registry.hasMappingForPattern("/webjars/**")) {
                customizeResourceHandlerRegistration(registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
            }
            String staticPathPattern = this.mvcProperties.getStaticPathPattern();
            if (!registry.hasMappingForPattern(staticPathPattern)) {
                customizeResourceHandlerRegistration(registry.addResourceHandler(staticPathPattern).addResourceLocations(WebMvcAutoConfiguration.getResourceLocations(this.resourceProperties.getStaticLocations())).setCachePeriod(getSeconds(cachePeriod)).setCacheControl(cacheControl));
            }
        }

        private Integer getSeconds(Duration cachePeriod) {
            if (cachePeriod != null) {
                return Integer.valueOf((int) cachePeriod.getSeconds());
            }
            return null;
        }

        private void customizeResourceHandlerRegistration(ResourceHandlerRegistration registration) {
            if (this.resourceHandlerRegistrationCustomizer != null) {
                this.resourceHandlerRegistrationCustomizer.customize(registration);
            }
        }

        @ConditionalOnMissingBean({RequestContextListener.class, RequestContextFilter.class})
        @ConditionalOnMissingFilterBean({RequestContextFilter.class})
        @Bean
        public static RequestContextFilter requestContextFilter() {
            return new OrderedRequestContextFilter();
        }
    }

    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration$EnableWebMvcConfiguration.class */
    public static class EnableWebMvcConfiguration extends DelegatingWebMvcConfiguration implements ResourceLoaderAware {
        private final ResourceProperties resourceProperties;
        private final WebMvcProperties mvcProperties;
        private final ListableBeanFactory beanFactory;
        private final WebMvcRegistrations mvcRegistrations;
        private ResourceLoader resourceLoader;

        public EnableWebMvcConfiguration(ResourceProperties resourceProperties, ObjectProvider<WebMvcProperties> mvcPropertiesProvider, ObjectProvider<WebMvcRegistrations> mvcRegistrationsProvider, ListableBeanFactory beanFactory) {
            this.resourceProperties = resourceProperties;
            this.mvcProperties = mvcPropertiesProvider.getIfAvailable();
            this.mvcRegistrations = mvcRegistrationsProvider.getIfUnique();
            this.beanFactory = beanFactory;
        }

        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
        @Bean
        public RequestMappingHandlerAdapter requestMappingHandlerAdapter(@Qualifier("mvcContentNegotiationManager") ContentNegotiationManager contentNegotiationManager, @Qualifier("mvcConversionService") FormattingConversionService conversionService, @Qualifier("mvcValidator") Validator validator) {
            RequestMappingHandlerAdapter adapter = super.requestMappingHandlerAdapter(contentNegotiationManager, conversionService, validator);
            adapter.setIgnoreDefaultModelOnRedirect(this.mvcProperties == null || this.mvcProperties.isIgnoreDefaultModelOnRedirect());
            return adapter;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
        public RequestMappingHandlerAdapter createRequestMappingHandlerAdapter() {
            if (this.mvcRegistrations != null && this.mvcRegistrations.getRequestMappingHandlerAdapter() != null) {
                return this.mvcRegistrations.getRequestMappingHandlerAdapter();
            }
            return super.createRequestMappingHandlerAdapter();
        }

        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
        @Bean
        @Primary
        public RequestMappingHandlerMapping requestMappingHandlerMapping(@Qualifier("mvcContentNegotiationManager") ContentNegotiationManager contentNegotiationManager, @Qualifier("mvcConversionService") FormattingConversionService conversionService, @Qualifier("mvcResourceUrlProvider") ResourceUrlProvider resourceUrlProvider) {
            return super.requestMappingHandlerMapping(contentNegotiationManager, conversionService, resourceUrlProvider);
        }

        @Bean
        public WelcomePageHandlerMapping welcomePageHandlerMapping(ApplicationContext applicationContext, FormattingConversionService mvcConversionService, ResourceUrlProvider mvcResourceUrlProvider) {
            WelcomePageHandlerMapping welcomePageHandlerMapping = new WelcomePageHandlerMapping(new TemplateAvailabilityProviders(applicationContext), applicationContext, getWelcomePage(), this.mvcProperties.getStaticPathPattern());
            welcomePageHandlerMapping.setInterceptors(getInterceptors(mvcConversionService, mvcResourceUrlProvider));
            welcomePageHandlerMapping.setCorsConfigurations(getCorsConfigurations());
            return welcomePageHandlerMapping;
        }

        private Optional<Resource> getWelcomePage() {
            String[] locations = WebMvcAutoConfiguration.getResourceLocations(this.resourceProperties.getStaticLocations());
            return Arrays.stream(locations).map(this::getIndexHtml).filter(this::isReadable).findFirst();
        }

        private Resource getIndexHtml(String location) {
            return this.resourceLoader.getResource(location + "index.html");
        }

        private boolean isReadable(Resource resource) {
            try {
                if (resource.exists()) {
                    if (resource.getURL() != null) {
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
                return false;
            }
        }

        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
        @Bean
        public FormattingConversionService mvcConversionService() {
            WebMvcProperties.Format format = this.mvcProperties.getFormat();
            WebConversionService conversionService = new WebConversionService(new DateTimeFormatters().dateFormat(format.getDate()).timeFormat(format.getTime()).dateTimeFormat(format.getDateTime()));
            addFormatters(conversionService);
            return conversionService;
        }

        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
        @Bean
        public Validator mvcValidator() {
            if (!ClassUtils.isPresent("javax.validation.Validator", getClass().getClassLoader())) {
                return super.mvcValidator();
            }
            return ValidatorAdapter.get(getApplicationContext(), getValidator());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
        public RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
            if (this.mvcRegistrations != null && this.mvcRegistrations.getRequestMappingHandlerMapping() != null) {
                return this.mvcRegistrations.getRequestMappingHandlerMapping();
            }
            return super.createRequestMappingHandlerMapping();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
        public ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer(FormattingConversionService mvcConversionService, Validator mvcValidator) {
            try {
                return (ConfigurableWebBindingInitializer) this.beanFactory.getBean(ConfigurableWebBindingInitializer.class);
            } catch (NoSuchBeanDefinitionException e) {
                return super.getConfigurableWebBindingInitializer(mvcConversionService, mvcValidator);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
        public ExceptionHandlerExceptionResolver createExceptionHandlerExceptionResolver() {
            if (this.mvcRegistrations != null && this.mvcRegistrations.getExceptionHandlerExceptionResolver() != null) {
                return this.mvcRegistrations.getExceptionHandlerExceptionResolver();
            }
            return super.createExceptionHandlerExceptionResolver();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration, org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
        public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
            super.extendHandlerExceptionResolvers(exceptionResolvers);
            if (this.mvcProperties.isLogResolvedException()) {
                for (HandlerExceptionResolver resolver : exceptionResolvers) {
                    if (resolver instanceof AbstractHandlerExceptionResolver) {
                        ((AbstractHandlerExceptionResolver) resolver).setWarnLogCategory(resolver.getClass().getName());
                    }
                }
            }
        }

        @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
        @Bean
        public ContentNegotiationManager mvcContentNegotiationManager() {
            ContentNegotiationManager manager = super.mvcContentNegotiationManager();
            List<ContentNegotiationStrategy> strategies = manager.getStrategies();
            ListIterator<ContentNegotiationStrategy> iterator = strategies.listIterator();
            while (iterator.hasNext()) {
                ContentNegotiationStrategy strategy = iterator.next();
                if (strategy instanceof PathExtensionContentNegotiationStrategy) {
                    iterator.set(new OptionalPathExtensionContentNegotiationStrategy(strategy));
                }
            }
            return manager;
        }

        @Override // org.springframework.context.ResourceLoaderAware
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }
    }

    @ConditionalOnEnabledResourceChain
    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration$ResourceChainCustomizerConfiguration.class */
    static class ResourceChainCustomizerConfiguration {
        ResourceChainCustomizerConfiguration() {
        }

        @Bean
        ResourceChainResourceHandlerRegistrationCustomizer resourceHandlerRegistrationCustomizer() {
            return new ResourceChainResourceHandlerRegistrationCustomizer();
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration$ResourceChainResourceHandlerRegistrationCustomizer.class */
    static class ResourceChainResourceHandlerRegistrationCustomizer implements ResourceHandlerRegistrationCustomizer {
        @Autowired
        private ResourceProperties resourceProperties = new ResourceProperties();

        ResourceChainResourceHandlerRegistrationCustomizer() {
        }

        @Override // org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.ResourceHandlerRegistrationCustomizer
        public void customize(ResourceHandlerRegistration registration) {
            ResourceProperties.Chain properties = this.resourceProperties.getChain();
            configureResourceChain(properties, registration.resourceChain(properties.isCache()));
        }

        private void configureResourceChain(ResourceProperties.Chain properties, ResourceChainRegistration chain) {
            ResourceProperties.Strategy strategy = properties.getStrategy();
            if (properties.isCompressed()) {
                chain.addResolver(new EncodedResourceResolver());
            }
            if (strategy.getFixed().isEnabled() || strategy.getContent().isEnabled()) {
                chain.addResolver(getVersionResourceResolver(strategy));
            }
            if (properties.isHtmlApplicationCache()) {
                chain.addTransformer(new AppCacheManifestTransformer());
            }
        }

        private ResourceResolver getVersionResourceResolver(ResourceProperties.Strategy properties) {
            VersionResourceResolver resolver = new VersionResourceResolver();
            if (properties.getFixed().isEnabled()) {
                String version = properties.getFixed().getVersion();
                String[] paths = properties.getFixed().getPaths();
                resolver.addFixedVersionStrategy(version, paths);
            }
            if (properties.getContent().isEnabled()) {
                String[] paths2 = properties.getContent().getPaths();
                resolver.addContentVersionStrategy(paths2);
            }
            return resolver;
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/servlet/WebMvcAutoConfiguration$OptionalPathExtensionContentNegotiationStrategy.class */
    static class OptionalPathExtensionContentNegotiationStrategy implements ContentNegotiationStrategy {
        private static final String SKIP_ATTRIBUTE = PathExtensionContentNegotiationStrategy.class.getName() + ".SKIP";
        private final ContentNegotiationStrategy delegate;

        OptionalPathExtensionContentNegotiationStrategy(ContentNegotiationStrategy delegate) {
            this.delegate = delegate;
        }

        @Override // org.springframework.web.accept.ContentNegotiationStrategy
        public List<MediaType> resolveMediaTypes(NativeWebRequest webRequest) throws HttpMediaTypeNotAcceptableException {
            Object skip = webRequest.getAttribute(SKIP_ATTRIBUTE, 0);
            if (skip != null && Boolean.parseBoolean(skip.toString())) {
                return MEDIA_TYPE_ALL_LIST;
            }
            return this.delegate.resolveMediaTypes(webRequest);
        }
    }
}
