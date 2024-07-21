package org.springframework.boot.autoconfigure.security.servlet;

import org.springframework.security.web.util.matcher.RequestMatcher;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/servlet/RequestMatcherProvider.class */
public interface RequestMatcherProvider {
    RequestMatcher getRequestMatcher(String pattern);
}
