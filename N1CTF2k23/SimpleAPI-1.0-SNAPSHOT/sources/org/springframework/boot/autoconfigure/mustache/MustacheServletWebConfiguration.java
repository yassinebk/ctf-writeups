package org.springframework.boot.autoconfigure.mustache;

import com.samskivert.mustache.Mustache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/mustache/MustacheServletWebConfiguration.class */
class MustacheServletWebConfiguration {
    MustacheServletWebConfiguration() {
    }

    @ConditionalOnMissingBean
    @Bean
    MustacheViewResolver mustacheViewResolver(Mustache.Compiler mustacheCompiler, MustacheProperties mustache) {
        MustacheViewResolver resolver = new MustacheViewResolver(mustacheCompiler);
        mustache.applyToMvcViewResolver(resolver);
        resolver.setCharset(mustache.getCharsetName());
        resolver.setOrder(2147483637);
        return resolver;
    }
}
