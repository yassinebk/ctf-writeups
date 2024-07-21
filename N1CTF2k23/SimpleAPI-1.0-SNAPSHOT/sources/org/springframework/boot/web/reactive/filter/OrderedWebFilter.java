package org.springframework.boot.web.reactive.filter;

import org.springframework.core.Ordered;
import org.springframework.web.server.WebFilter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/reactive/filter/OrderedWebFilter.class */
public interface OrderedWebFilter extends WebFilter, Ordered {
    public static final int REQUEST_WRAPPER_FILTER_MAX_ORDER = 0;
}
