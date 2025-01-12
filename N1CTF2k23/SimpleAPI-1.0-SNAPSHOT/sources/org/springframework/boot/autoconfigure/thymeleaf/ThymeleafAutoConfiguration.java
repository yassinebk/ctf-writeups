package org.springframework.boot.autoconfigure.thymeleaf;

import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;
import java.util.LinkedHashMap;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.servlet.DispatcherType;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.websocket.BasicAuthenticator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.template.TemplateLocation;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.MimeType;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
@EnableConfigurationProperties({ThymeleafProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({TemplateMode.class, SpringTemplateEngine.class})
@AutoConfigureAfter({WebMvcAutoConfiguration.class, WebFluxAutoConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration.class */
public class ThymeleafAutoConfiguration {

    @ConditionalOnMissingBean(name = {"defaultTemplateResolver"})
    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$DefaultTemplateResolverConfiguration.class */
    static class DefaultTemplateResolverConfiguration {
        private static final Log logger = LogFactory.getLog(DefaultTemplateResolverConfiguration.class);
        private final ThymeleafProperties properties;
        private final ApplicationContext applicationContext;

        DefaultTemplateResolverConfiguration(ThymeleafProperties properties, ApplicationContext applicationContext) {
            this.properties = properties;
            this.applicationContext = applicationContext;
        }

        @PostConstruct
        void checkTemplateLocationExists() {
            boolean checkTemplateLocation = this.properties.isCheckTemplateLocation();
            if (checkTemplateLocation) {
                TemplateLocation location = new TemplateLocation(this.properties.getPrefix());
                if (!location.exists(this.applicationContext)) {
                    logger.warn("Cannot find template location: " + location + " (please add some templates or check your Thymeleaf configuration)");
                }
            }
        }

        @Bean
        SpringResourceTemplateResolver defaultTemplateResolver() {
            SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
            resolver.setApplicationContext(this.applicationContext);
            resolver.setPrefix(this.properties.getPrefix());
            resolver.setSuffix(this.properties.getSuffix());
            resolver.setTemplateMode(this.properties.getMode());
            if (this.properties.getEncoding() != null) {
                resolver.setCharacterEncoding(this.properties.getEncoding().name());
            }
            resolver.setCacheable(this.properties.isCache());
            Integer order = this.properties.getTemplateResolverOrder();
            if (order != null) {
                resolver.setOrder(order);
            }
            resolver.setCheckExistence(this.properties.isCheckTemplate());
            return resolver;
        }
    }

    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$ThymeleafDefaultConfiguration.class */
    protected static class ThymeleafDefaultConfiguration {
        protected ThymeleafDefaultConfiguration() {
        }

        @ConditionalOnMissingBean({ISpringTemplateEngine.class})
        @Bean
        SpringTemplateEngine templateEngine(ThymeleafProperties properties, ObjectProvider<ITemplateResolver> templateResolvers, ObjectProvider<IDialect> dialects) {
            SpringTemplateEngine engine = new SpringTemplateEngine();
            engine.setEnableSpringELCompiler(properties.isEnableSpringElCompiler());
            engine.setRenderHiddenMarkersBeforeCheckboxes(properties.isRenderHiddenMarkersBeforeCheckboxes());
            Stream<ITemplateResolver> orderedStream = templateResolvers.orderedStream();
            engine.getClass();
            orderedStream.forEach(this::addTemplateResolver);
            Stream<IDialect> orderedStream2 = dialects.orderedStream();
            engine.getClass();
            orderedStream2.forEach(this::addDialect);
            return engine;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = {"spring.thymeleaf.enabled"}, matchIfMissing = true)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$ThymeleafWebMvcConfiguration.class */
    static class ThymeleafWebMvcConfiguration {
        ThymeleafWebMvcConfiguration() {
        }

        @ConditionalOnMissingFilterBean({ResourceUrlEncodingFilter.class})
        @ConditionalOnEnabledResourceChain
        @Bean
        FilterRegistrationBean<ResourceUrlEncodingFilter> resourceUrlEncodingFilter() {
            FilterRegistrationBean<ResourceUrlEncodingFilter> registration = new FilterRegistrationBean<>(new ResourceUrlEncodingFilter(), new ServletRegistrationBean[0]);
            registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR);
            return registration;
        }

        @Configuration(proxyBeanMethods = false)
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$ThymeleafWebMvcConfiguration$ThymeleafViewResolverConfiguration.class */
        static class ThymeleafViewResolverConfiguration {
            ThymeleafViewResolverConfiguration() {
            }

            @ConditionalOnMissingBean(name = {"thymeleafViewResolver"})
            @Bean
            ThymeleafViewResolver thymeleafViewResolver(ThymeleafProperties properties, SpringTemplateEngine templateEngine) {
                ThymeleafViewResolver resolver = new ThymeleafViewResolver();
                resolver.setTemplateEngine(templateEngine);
                resolver.setCharacterEncoding(properties.getEncoding().name());
                resolver.setContentType(appendCharset(properties.getServlet().getContentType(), resolver.getCharacterEncoding()));
                resolver.setProducePartialOutputWhileProcessing(properties.getServlet().isProducePartialOutputWhileProcessing());
                resolver.setExcludedViewNames(properties.getExcludedViewNames());
                resolver.setViewNames(properties.getViewNames());
                resolver.setOrder((int) SecurityProperties.BASIC_AUTH_ORDER);
                resolver.setCache(properties.isCache());
                return resolver;
            }

            private String appendCharset(MimeType type, String charset) {
                if (type.getCharset() != null) {
                    return type.toString();
                }
                LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
                parameters.put(BasicAuthenticator.charsetparam, charset);
                parameters.putAll(type.getParameters());
                return new MimeType(type, parameters).toString();
            }
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = {"spring.thymeleaf.enabled"}, matchIfMissing = true)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$ThymeleafReactiveConfiguration.class */
    static class ThymeleafReactiveConfiguration {
        ThymeleafReactiveConfiguration() {
        }

        @ConditionalOnMissingBean({ISpringWebFluxTemplateEngine.class})
        @Bean
        SpringWebFluxTemplateEngine templateEngine(ThymeleafProperties properties, ObjectProvider<ITemplateResolver> templateResolvers, ObjectProvider<IDialect> dialects) {
            SpringWebFluxTemplateEngine engine = new SpringWebFluxTemplateEngine();
            engine.setEnableSpringELCompiler(properties.isEnableSpringElCompiler());
            engine.setRenderHiddenMarkersBeforeCheckboxes(properties.isRenderHiddenMarkersBeforeCheckboxes());
            Stream<ITemplateResolver> orderedStream = templateResolvers.orderedStream();
            engine.getClass();
            orderedStream.forEach(this::addTemplateResolver);
            Stream<IDialect> orderedStream2 = dialects.orderedStream();
            engine.getClass();
            orderedStream2.forEach(this::addDialect);
            return engine;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(name = {"spring.thymeleaf.enabled"}, matchIfMissing = true)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$ThymeleafWebFluxConfiguration.class */
    static class ThymeleafWebFluxConfiguration {
        ThymeleafWebFluxConfiguration() {
        }

        @ConditionalOnMissingBean(name = {"thymeleafReactiveViewResolver"})
        @Bean
        ThymeleafReactiveViewResolver thymeleafViewResolver(ISpringWebFluxTemplateEngine templateEngine, ThymeleafProperties properties) {
            ThymeleafReactiveViewResolver resolver = new ThymeleafReactiveViewResolver();
            resolver.setTemplateEngine(templateEngine);
            mapProperties(properties, resolver);
            mapReactiveProperties(properties.getReactive(), resolver);
            resolver.setOrder((int) SecurityProperties.BASIC_AUTH_ORDER);
            return resolver;
        }

        private void mapProperties(ThymeleafProperties properties, ThymeleafReactiveViewResolver resolver) {
            PropertyMapper map = PropertyMapper.get();
            properties.getClass();
            PropertyMapper.Source from = map.from(this::getEncoding);
            resolver.getClass();
            from.to(this::setDefaultCharset);
            resolver.setExcludedViewNames(properties.getExcludedViewNames());
            resolver.setViewNames(properties.getViewNames());
        }

        private void mapReactiveProperties(ThymeleafProperties.Reactive properties, ThymeleafReactiveViewResolver resolver) {
            PropertyMapper map = PropertyMapper.get();
            properties.getClass();
            PropertyMapper.Source whenNonNull = map.from(this::getMediaTypes).whenNonNull();
            resolver.getClass();
            whenNonNull.to(this::setSupportedMediaTypes);
            properties.getClass();
            PropertyMapper.Source<Integer> when = map.from(this::getMaxChunkSize).asInt((v0) -> {
                return v0.toBytes();
            }).when(size -> {
                return size.intValue() > 0;
            });
            resolver.getClass();
            when.to((v1) -> {
                r1.setResponseMaxChunkSizeBytes(v1);
            });
            properties.getClass();
            PropertyMapper.Source from = map.from(this::getFullModeViewNames);
            resolver.getClass();
            from.to(this::setFullModeViewNames);
            properties.getClass();
            PropertyMapper.Source from2 = map.from(this::getChunkedModeViewNames);
            resolver.getClass();
            from2.to(this::setChunkedModeViewNames);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({LayoutDialect.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$ThymeleafWebLayoutConfiguration.class */
    static class ThymeleafWebLayoutConfiguration {
        ThymeleafWebLayoutConfiguration() {
        }

        @ConditionalOnMissingBean
        @Bean
        LayoutDialect layoutDialect() {
            return new LayoutDialect();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({DataAttributeDialect.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$DataAttributeDialectConfiguration.class */
    static class DataAttributeDialectConfiguration {
        DataAttributeDialectConfiguration() {
        }

        @ConditionalOnMissingBean
        @Bean
        DataAttributeDialect dialect() {
            return new DataAttributeDialect();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({SpringSecurityDialect.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$ThymeleafSecurityDialectConfiguration.class */
    static class ThymeleafSecurityDialectConfiguration {
        ThymeleafSecurityDialectConfiguration() {
        }

        @ConditionalOnMissingBean
        @Bean
        SpringSecurityDialect securityDialect() {
            return new SpringSecurityDialect();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({Java8TimeDialect.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/thymeleaf/ThymeleafAutoConfiguration$ThymeleafJava8TimeDialect.class */
    static class ThymeleafJava8TimeDialect {
        ThymeleafJava8TimeDialect() {
        }

        @ConditionalOnMissingBean
        @Bean
        Java8TimeDialect java8TimeDialect() {
            return new Java8TimeDialect();
        }
    }
}
