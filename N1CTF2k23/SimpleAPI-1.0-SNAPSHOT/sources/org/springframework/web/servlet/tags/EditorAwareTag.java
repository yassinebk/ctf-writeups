package org.springframework.web.servlet.tags;

import java.beans.PropertyEditor;
import javax.servlet.jsp.JspException;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/tags/EditorAwareTag.class */
public interface EditorAwareTag {
    @Nullable
    PropertyEditor getEditor() throws JspException;
}
