package javax.servlet.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.servlet.annotation.ServletSecurity;
@Retention(RetentionPolicy.RUNTIME)
@Documented
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/annotation/HttpMethodConstraint.class */
public @interface HttpMethodConstraint {
    String value();

    ServletSecurity.EmptyRoleSemantic emptyRoleSemantic() default ServletSecurity.EmptyRoleSemantic.PERMIT;

    ServletSecurity.TransportGuarantee transportGuarantee() default ServletSecurity.TransportGuarantee.NONE;

    String[] rolesAllowed() default {};
}
