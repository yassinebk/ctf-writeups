package org.springframework.jmx.export.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/jmx/export/annotation/ManagedAttribute.class */
public @interface ManagedAttribute {
    String defaultValue() default "";

    String description() default "";

    int currencyTimeLimit() default -1;

    String persistPolicy() default "";

    int persistPeriod() default -1;
}