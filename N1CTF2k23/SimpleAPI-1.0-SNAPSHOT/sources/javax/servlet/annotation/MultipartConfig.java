package javax.servlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/annotation/MultipartConfig.class */
public @interface MultipartConfig {
    String location() default "";

    long maxFileSize() default -1;

    long maxRequestSize() default -1;

    int fileSizeThreshold() default 0;
}
