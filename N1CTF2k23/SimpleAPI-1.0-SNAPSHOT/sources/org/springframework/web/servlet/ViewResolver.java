package org.springframework.web.servlet;

import java.util.Locale;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/ViewResolver.class */
public interface ViewResolver {
    @Nullable
    View resolveViewName(String str, Locale locale) throws Exception;
}
