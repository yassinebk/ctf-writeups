package org.springframework.ui.context;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/ui/context/ThemeSource.class */
public interface ThemeSource {
    @Nullable
    Theme getTheme(String str);
}
