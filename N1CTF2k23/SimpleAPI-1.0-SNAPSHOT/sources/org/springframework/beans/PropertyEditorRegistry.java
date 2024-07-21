package org.springframework.beans;

import java.beans.PropertyEditor;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/PropertyEditorRegistry.class */
public interface PropertyEditorRegistry {
    void registerCustomEditor(Class<?> cls, PropertyEditor propertyEditor);

    void registerCustomEditor(@Nullable Class<?> cls, @Nullable String str, PropertyEditor propertyEditor);

    @Nullable
    PropertyEditor findCustomEditor(@Nullable Class<?> cls, @Nullable String str);
}
