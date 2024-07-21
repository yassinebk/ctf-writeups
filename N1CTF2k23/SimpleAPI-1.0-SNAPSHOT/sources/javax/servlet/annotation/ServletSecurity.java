package javax.servlet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/annotation/ServletSecurity.class */
public @interface ServletSecurity {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/annotation/ServletSecurity$EmptyRoleSemantic.class */
    public enum EmptyRoleSemantic {
        PERMIT,
        DENY
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/annotation/ServletSecurity$TransportGuarantee.class */
    public enum TransportGuarantee {
        NONE,
        CONFIDENTIAL
    }

    HttpConstraint value() default @HttpConstraint;

    HttpMethodConstraint[] httpMethodConstraints() default {};
}
