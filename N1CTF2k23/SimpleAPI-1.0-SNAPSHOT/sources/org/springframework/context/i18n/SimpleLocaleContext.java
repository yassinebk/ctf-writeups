package org.springframework.context.i18n;

import java.util.Locale;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/i18n/SimpleLocaleContext.class */
public class SimpleLocaleContext implements LocaleContext {
    @Nullable
    private final Locale locale;

    public SimpleLocaleContext(@Nullable Locale locale) {
        this.locale = locale;
    }

    @Override // org.springframework.context.i18n.LocaleContext
    @Nullable
    public Locale getLocale() {
        return this.locale;
    }

    public String toString() {
        return this.locale != null ? this.locale.toString() : "-";
    }
}
