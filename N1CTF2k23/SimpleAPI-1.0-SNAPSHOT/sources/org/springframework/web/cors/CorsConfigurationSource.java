package org.springframework.web.cors;

import javax.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/cors/CorsConfigurationSource.class */
public interface CorsConfigurationSource {
    @Nullable
    CorsConfiguration getCorsConfiguration(HttpServletRequest httpServletRequest);
}
