package org.springframework.web.servlet.tags.form;

import java.beans.PropertyEditor;
import javax.servlet.jsp.JspException;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.tags.HtmlEscapingAwareTag;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/tags/form/AbstractFormTag.class */
public abstract class AbstractFormTag extends HtmlEscapingAwareTag {
    protected abstract int writeTagContent(TagWriter tagWriter) throws JspException;

    /* JADX INFO: Access modifiers changed from: protected */
    @Nullable
    public Object evaluate(String attributeName, @Nullable Object value) throws JspException {
        return value;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void writeOptionalAttribute(TagWriter tagWriter, String attributeName, @Nullable String value) throws JspException {
        if (value != null) {
            tagWriter.writeOptionalAttributeValue(attributeName, getDisplayString(evaluate(attributeName, value)));
        }
    }

    protected TagWriter createTagWriter() {
        return new TagWriter(this.pageContext);
    }

    @Override // org.springframework.web.servlet.tags.RequestContextAwareTag
    protected final int doStartTagInternal() throws Exception {
        return writeTagContent(createTagWriter());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getDisplayString(@Nullable Object value) {
        return ValueFormatter.getDisplayString(value, isHtmlEscape());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getDisplayString(@Nullable Object value, @Nullable PropertyEditor propertyEditor) {
        return ValueFormatter.getDisplayString(value, propertyEditor, isHtmlEscape());
    }

    @Override // org.springframework.web.servlet.tags.HtmlEscapingAwareTag
    protected boolean isDefaultHtmlEscape() {
        Boolean defaultHtmlEscape = getRequestContext().getDefaultHtmlEscape();
        return defaultHtmlEscape == null || defaultHtmlEscape.booleanValue();
    }
}
