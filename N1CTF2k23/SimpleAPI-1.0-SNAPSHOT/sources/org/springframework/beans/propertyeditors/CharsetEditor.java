package org.springframework.beans.propertyeditors;

import java.beans.PropertyEditorSupport;
import java.nio.charset.Charset;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/propertyeditors/CharsetEditor.class */
public class CharsetEditor extends PropertyEditorSupport {
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            setValue(Charset.forName(text));
        } else {
            setValue(null);
        }
    }

    public String getAsText() {
        Charset value = (Charset) getValue();
        return value != null ? value.name() : "";
    }
}
