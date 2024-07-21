package org.springframework.context;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/HierarchicalMessageSource.class */
public interface HierarchicalMessageSource extends MessageSource {
    void setParentMessageSource(@Nullable MessageSource messageSource);

    @Nullable
    MessageSource getParentMessageSource();
}
