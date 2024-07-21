package org.springframework.boot.autoconfigure.webservices.client;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.webservices.client.WebServiceTemplateBuilder;
import org.springframework.boot.webservices.client.WebServiceTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({WebServiceTemplate.class, Unmarshaller.class, Marshaller.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/webservices/client/WebServiceTemplateAutoConfiguration.class */
public class WebServiceTemplateAutoConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public WebServiceTemplateBuilder webServiceTemplateBuilder(ObjectProvider<WebServiceTemplateCustomizer> webServiceTemplateCustomizers) {
        WebServiceTemplateBuilder builder = new WebServiceTemplateBuilder(new WebServiceTemplateCustomizer[0]);
        List<WebServiceTemplateCustomizer> customizers = (List) webServiceTemplateCustomizers.orderedStream().collect(Collectors.toList());
        if (!customizers.isEmpty()) {
            builder = builder.customizers(customizers);
        }
        return builder;
    }
}
