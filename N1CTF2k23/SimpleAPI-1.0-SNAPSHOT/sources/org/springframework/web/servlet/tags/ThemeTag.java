package org.springframework.web.servlet.tags;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/tags/ThemeTag.class */
public class ThemeTag extends MessageTag {
    @Override // org.springframework.web.servlet.tags.MessageTag
    protected MessageSource getMessageSource() {
        return getRequestContext().getTheme().getMessageSource();
    }

    @Override // org.springframework.web.servlet.tags.MessageTag
    protected String getNoSuchMessageExceptionDescription(NoSuchMessageException ex) {
        return "Theme '" + getRequestContext().getTheme().getName() + "': " + ex.getMessage();
    }
}
