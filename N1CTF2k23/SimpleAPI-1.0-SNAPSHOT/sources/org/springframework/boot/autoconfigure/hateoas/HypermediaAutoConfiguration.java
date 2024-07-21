package org.springframework.boot.autoconfigure.hateoas;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.plugin.core.Plugin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
@EnableConfigurationProperties({HateoasProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({EntityModel.class, RequestMapping.class, RequestMappingHandlerAdapter.class, Plugin.class})
@AutoConfigureAfter({WebMvcAutoConfiguration.class, JacksonAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class, RepositoryRestMvcAutoConfiguration.class})
@ConditionalOnWebApplication
@Import({HypermediaHttpMessageConverterConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/hateoas/HypermediaAutoConfiguration.class */
public class HypermediaAutoConfiguration {

    @EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({ObjectMapper.class})
    @ConditionalOnMissingBean({LinkDiscoverers.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/hateoas/HypermediaAutoConfiguration$HypermediaConfiguration.class */
    protected static class HypermediaConfiguration {
        protected HypermediaConfiguration() {
        }
    }
}
