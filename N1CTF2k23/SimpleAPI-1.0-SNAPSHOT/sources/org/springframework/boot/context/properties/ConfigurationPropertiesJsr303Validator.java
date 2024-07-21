package org.springframework.boot.context.properties;

import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationPropertiesJsr303Validator.class */
final class ConfigurationPropertiesJsr303Validator implements Validator {
    private static final String[] VALIDATOR_CLASSES = {"javax.validation.Validator", "javax.validation.ValidatorFactory", "javax.validation.bootstrap.GenericBootstrap"};
    private final Delegate delegate;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ConfigurationPropertiesJsr303Validator(ApplicationContext applicationContext) {
        this.delegate = new Delegate(applicationContext);
    }

    @Override // org.springframework.validation.Validator
    public boolean supports(Class<?> type) {
        return this.delegate.supports(type);
    }

    @Override // org.springframework.validation.Validator
    public void validate(Object target, Errors errors) {
        this.delegate.validate(target, errors);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isJsr303Present(ApplicationContext applicationContext) {
        String[] strArr;
        ClassLoader classLoader = applicationContext.getClassLoader();
        for (String validatorClass : VALIDATOR_CLASSES) {
            if (!ClassUtils.isPresent(validatorClass, classLoader)) {
                return false;
            }
        }
        return true;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/ConfigurationPropertiesJsr303Validator$Delegate.class */
    private static class Delegate extends LocalValidatorFactoryBean {
        Delegate(ApplicationContext applicationContext) {
            setApplicationContext(applicationContext);
            setMessageInterpolator(new MessageInterpolatorFactory().getObject());
            afterPropertiesSet();
        }
    }
}
