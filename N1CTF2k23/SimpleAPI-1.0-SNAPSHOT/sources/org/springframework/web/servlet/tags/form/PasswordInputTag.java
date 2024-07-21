package org.springframework.web.servlet.tags.form;

import javax.servlet.jsp.JspException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/tags/form/PasswordInputTag.class */
public class PasswordInputTag extends InputTag {
    private boolean showPassword = false;

    public void setShowPassword(boolean showPassword) {
        this.showPassword = showPassword;
    }

    public boolean isShowPassword() {
        return this.showPassword;
    }

    @Override // org.springframework.web.servlet.tags.form.InputTag, org.springframework.web.servlet.tags.form.AbstractHtmlElementTag
    protected boolean isValidDynamicAttribute(String localName, Object value) {
        return !"type".equals(localName);
    }

    @Override // org.springframework.web.servlet.tags.form.InputTag
    protected String getType() {
        return "password";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.web.servlet.tags.form.InputTag
    public void writeValue(TagWriter tagWriter) throws JspException {
        if (this.showPassword) {
            super.writeValue(tagWriter);
        } else {
            tagWriter.writeAttribute("value", processFieldValue(getName(), "", getType()));
        }
    }
}
