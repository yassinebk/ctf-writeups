package org.springframework.web.servlet.mvc.condition;

import org.springframework.http.MediaType;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/mvc/condition/MediaTypeExpression.class */
public interface MediaTypeExpression {
    MediaType getMediaType();

    boolean isNegated();
}
