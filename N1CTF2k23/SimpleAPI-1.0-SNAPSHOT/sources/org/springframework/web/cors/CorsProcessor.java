package org.springframework.web.cors;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/cors/CorsProcessor.class */
public interface CorsProcessor {
    boolean processRequest(@Nullable CorsConfiguration corsConfiguration, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException;
}
