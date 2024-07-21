package org.springframework.boot.autoconfigure.condition;

import javax.servlet.ServletContext;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.web.context.WebApplicationContext;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/condition/OnWarDeploymentCondition.class */
class OnWarDeploymentCondition extends SpringBootCondition {
    OnWarDeploymentCondition() {
    }

    @Override // org.springframework.boot.autoconfigure.condition.SpringBootCondition
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ResourceLoader resourceLoader = context.getResourceLoader();
        if (resourceLoader instanceof WebApplicationContext) {
            WebApplicationContext applicationContext = (WebApplicationContext) resourceLoader;
            ServletContext servletContext = applicationContext.getServletContext();
            if (servletContext != null) {
                return ConditionOutcome.match("Application is deployed as a WAR file.");
            }
        }
        return ConditionOutcome.noMatch(ConditionMessage.forCondition(ConditionalOnWarDeployment.class, new Object[0]).because("the application is not deployed as a WAR file."));
    }
}
