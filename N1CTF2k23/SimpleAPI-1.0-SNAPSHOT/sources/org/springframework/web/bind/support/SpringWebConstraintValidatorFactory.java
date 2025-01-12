package org.springframework.web.bind.support;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/bind/support/SpringWebConstraintValidatorFactory.class */
public class SpringWebConstraintValidatorFactory implements ConstraintValidatorFactory {
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        return (T) getWebApplicationContext().getAutowireCapableBeanFactory().createBean(key);
    }

    public void releaseInstance(ConstraintValidator<?, ?> instance) {
        getWebApplicationContext().getAutowireCapableBeanFactory().destroyBean(instance);
    }

    protected WebApplicationContext getWebApplicationContext() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        if (wac == null) {
            throw new IllegalStateException("No WebApplicationContext registered for current thread - consider overriding SpringWebConstraintValidatorFactory.getWebApplicationContext()");
        }
        return wac;
    }
}
