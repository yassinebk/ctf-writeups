package org.springframework.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/annotation/AliasFor.class */
public @interface AliasFor {
    @AliasFor(BeanDefinitionParserDelegate.QUALIFIER_ATTRIBUTE_ELEMENT)
    String value() default "";

    @AliasFor("value")
    String attribute() default "";

    Class<? extends Annotation> annotation() default Annotation.class;
}
