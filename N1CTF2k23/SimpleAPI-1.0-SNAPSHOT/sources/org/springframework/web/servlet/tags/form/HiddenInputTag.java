package org.springframework.web.servlet.tags.form;

import javax.servlet.jsp.JspException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/tags/form/HiddenInputTag.class */
public class HiddenInputTag extends AbstractHtmlElementTag {
    public static final String DISABLED_ATTRIBUTE = "disabled";
    private boolean disabled;

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractHtmlElementTag
    protected boolean isValidDynamicAttribute(String localName, Object value) {
        return !"type".equals(localName);
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractFormTag
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        tagWriter.startTag("input");
        writeDefaultAttributes(tagWriter);
        tagWriter.writeAttribute("type", "hidden");
        if (isDisabled()) {
            tagWriter.writeAttribute("disabled", "disabled");
        }
        String value = getDisplayString(getBoundValue(), getPropertyEditor());
        tagWriter.writeAttribute("value", processFieldValue(getName(), value, "hidden"));
        tagWriter.endTag();
        return 0;
    }
}
