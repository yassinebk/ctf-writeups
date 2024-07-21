package org.springframework.web.servlet.tags.form;

import javax.servlet.jsp.JspException;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/tags/form/CheckboxTag.class */
public class CheckboxTag extends AbstractSingleCheckedElementTag {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.web.servlet.tags.form.AbstractSingleCheckedElementTag, org.springframework.web.servlet.tags.form.AbstractCheckedElementTag, org.springframework.web.servlet.tags.form.AbstractFormTag
    public int writeTagContent(TagWriter tagWriter) throws JspException {
        super.writeTagContent(tagWriter);
        if (!isDisabled()) {
            tagWriter.startTag("input");
            tagWriter.writeAttribute("type", "hidden");
            String name = "_" + getName();
            tagWriter.writeAttribute("name", name);
            tagWriter.writeAttribute("value", processFieldValue(name, CustomBooleanEditor.VALUE_ON, "hidden"));
            tagWriter.endTag();
            return 0;
        }
        return 0;
    }

    @Override // org.springframework.web.servlet.tags.form.AbstractSingleCheckedElementTag
    protected void writeTagDetails(TagWriter tagWriter) throws JspException {
        tagWriter.writeAttribute("type", getInputType());
        Object boundValue = getBoundValue();
        Class<?> valueType = getBindStatus().getValueType();
        if (Boolean.class == valueType || Boolean.TYPE == valueType) {
            if (boundValue instanceof String) {
                boundValue = Boolean.valueOf((String) boundValue);
            }
            Boolean booleanValue = boundValue != null ? (Boolean) boundValue : Boolean.FALSE;
            renderFromBoolean(booleanValue, tagWriter);
            return;
        }
        Object value = getValue();
        if (value == null) {
            throw new IllegalArgumentException("Attribute 'value' is required when binding to non-boolean values");
        }
        Object resolvedValue = value instanceof String ? evaluate("value", value) : value;
        renderFromValue(resolvedValue, tagWriter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.web.servlet.tags.form.AbstractCheckedElementTag
    public String getInputType() {
        return "checkbox";
    }
}
