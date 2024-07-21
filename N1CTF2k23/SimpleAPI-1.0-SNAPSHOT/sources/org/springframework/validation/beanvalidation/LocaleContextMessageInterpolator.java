package org.springframework.validation.beanvalidation;

import java.util.Locale;
import javax.validation.MessageInterpolator;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/validation/beanvalidation/LocaleContextMessageInterpolator.class */
public class LocaleContextMessageInterpolator implements MessageInterpolator {
    private final MessageInterpolator targetInterpolator;

    public LocaleContextMessageInterpolator(MessageInterpolator targetInterpolator) {
        Assert.notNull(targetInterpolator, "Target MessageInterpolator must not be null");
        this.targetInterpolator = targetInterpolator;
    }

    public String interpolate(String message, MessageInterpolator.Context context) {
        return this.targetInterpolator.interpolate(message, context, LocaleContextHolder.getLocale());
    }

    public String interpolate(String message, MessageInterpolator.Context context, Locale locale) {
        return this.targetInterpolator.interpolate(message, context, locale);
    }
}
