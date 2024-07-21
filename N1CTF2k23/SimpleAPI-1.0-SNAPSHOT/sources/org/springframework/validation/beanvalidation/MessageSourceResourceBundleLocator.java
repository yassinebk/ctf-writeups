package org.springframework.validation.beanvalidation;

import java.util.Locale;
import java.util.ResourceBundle;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceResourceBundle;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/validation/beanvalidation/MessageSourceResourceBundleLocator.class */
public class MessageSourceResourceBundleLocator implements ResourceBundleLocator {
    private final MessageSource messageSource;

    public MessageSourceResourceBundleLocator(MessageSource messageSource) {
        Assert.notNull(messageSource, "MessageSource must not be null");
        this.messageSource = messageSource;
    }

    public ResourceBundle getResourceBundle(Locale locale) {
        return new MessageSourceResourceBundle(this.messageSource, locale);
    }
}
