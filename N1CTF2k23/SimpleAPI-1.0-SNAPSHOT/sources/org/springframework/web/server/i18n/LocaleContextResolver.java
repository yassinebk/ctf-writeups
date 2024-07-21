package org.springframework.web.server.i18n;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ServerWebExchange;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/server/i18n/LocaleContextResolver.class */
public interface LocaleContextResolver {
    LocaleContext resolveLocaleContext(ServerWebExchange serverWebExchange);

    void setLocaleContext(ServerWebExchange serverWebExchange, @Nullable LocaleContext localeContext);
}
