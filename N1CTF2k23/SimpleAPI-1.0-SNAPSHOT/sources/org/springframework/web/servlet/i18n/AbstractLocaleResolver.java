package org.springframework.web.servlet.i18n;

import java.util.Locale;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.LocaleResolver;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/i18n/AbstractLocaleResolver.class */
public abstract class AbstractLocaleResolver implements LocaleResolver {
    @Nullable
    private Locale defaultLocale;

    public void setDefaultLocale(@Nullable Locale defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Nullable
    public Locale getDefaultLocale() {
        return this.defaultLocale;
    }
}
