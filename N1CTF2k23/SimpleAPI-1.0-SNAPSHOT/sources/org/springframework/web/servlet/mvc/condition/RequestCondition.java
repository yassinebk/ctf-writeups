package org.springframework.web.servlet.mvc.condition;

import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/mvc/condition/RequestCondition.class */
public interface RequestCondition<T> {
    T combine(T t);

    @Nullable
    T getMatchingCondition(HttpServletRequest httpServletRequest);

    int compareTo(T t, HttpServletRequest httpServletRequest);
}
