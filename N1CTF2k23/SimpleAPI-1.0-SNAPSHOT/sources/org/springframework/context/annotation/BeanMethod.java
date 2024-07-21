package org.springframework.context.annotation;

import org.springframework.beans.factory.parsing.Problem;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.core.type.MethodMetadata;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/BeanMethod.class */
final class BeanMethod extends ConfigurationMethod {
    public BeanMethod(MethodMetadata metadata, ConfigurationClass configurationClass) {
        super(metadata, configurationClass);
    }

    @Override // org.springframework.context.annotation.ConfigurationMethod
    public void validate(ProblemReporter problemReporter) {
        if (!getMetadata().isStatic() && this.configurationClass.getMetadata().isAnnotated(Configuration.class.getName()) && !getMetadata().isOverridable()) {
            problemReporter.error(new NonOverridableMethodError());
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/BeanMethod$NonOverridableMethodError.class */
    private class NonOverridableMethodError extends Problem {
        public NonOverridableMethodError() {
            super(String.format("@Bean method '%s' must not be private or final; change the method's modifiers to continue", BeanMethod.this.getMetadata().getMethodName()), BeanMethod.this.getResourceLocation());
        }
    }
}
