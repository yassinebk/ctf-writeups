package org.springframework.web.servlet.mvc.condition;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/mvc/condition/NameValueExpression.class */
public interface NameValueExpression<T> {
    String getName();

    @Nullable
    T getValue();

    boolean isNegated();
}
