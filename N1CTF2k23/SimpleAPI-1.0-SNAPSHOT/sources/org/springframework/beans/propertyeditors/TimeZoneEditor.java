package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.util.TimeZone;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/propertyeditors/TimeZoneEditor.class */
public class TimeZoneEditor extends PropertyEditorSupport {
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(StringUtils.parseTimeZoneString(text));
    }

    public String getAsText() {
        TimeZone value = (TimeZone) getValue();
        return value != null ? value.getID() : "";
    }
}
