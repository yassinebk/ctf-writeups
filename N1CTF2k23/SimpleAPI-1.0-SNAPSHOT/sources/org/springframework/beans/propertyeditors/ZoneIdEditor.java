package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.time.ZoneId;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/propertyeditors/ZoneIdEditor.class */
public class ZoneIdEditor extends PropertyEditorSupport {
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(ZoneId.of(text));
    }

    public String getAsText() {
        ZoneId value = (ZoneId) getValue();
        return value != null ? value.getId() : "";
    }
}
