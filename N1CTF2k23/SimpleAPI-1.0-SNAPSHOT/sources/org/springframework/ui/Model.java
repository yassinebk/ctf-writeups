package org.springframework.ui;

import java.util.Collection;
import java.util.Map;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/ui/Model.class */
public interface Model {
    Model addAttribute(String str, @Nullable Object obj);

    Model addAttribute(Object obj);

    Model addAllAttributes(Collection<?> collection);

    Model addAllAttributes(Map<String, ?> map);

    Model mergeAttributes(Map<String, ?> map);

    boolean containsAttribute(String str);

    @Nullable
    Object getAttribute(String str);

    Map<String, Object> asMap();
}
