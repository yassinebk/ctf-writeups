package org.springframework.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.io.support.PropertySourceFactory;
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(PropertySources.class)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/annotation/PropertySource.class */
public @interface PropertySource {
    String name() default "";

    String[] value();

    boolean ignoreResourceNotFound() default false;

    String encoding() default "";

    Class<? extends PropertySourceFactory> factory() default PropertySourceFactory.class;
}
