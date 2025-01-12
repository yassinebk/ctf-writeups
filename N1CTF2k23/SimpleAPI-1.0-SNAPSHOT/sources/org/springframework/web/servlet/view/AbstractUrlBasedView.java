package org.springframework.web.servlet.view;

import java.util.Locale;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/view/AbstractUrlBasedView.class */
public abstract class AbstractUrlBasedView extends AbstractView implements InitializingBean {
    @Nullable
    private String url;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractUrlBasedView() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractUrlBasedView(String url) {
        this.url = url;
    }

    public void setUrl(@Nullable String url) {
        this.url = url;
    }

    @Nullable
    public String getUrl() {
        return this.url;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws Exception {
        if (isUrlRequired() && getUrl() == null) {
            throw new IllegalArgumentException("Property 'url' is required");
        }
    }

    protected boolean isUrlRequired() {
        return true;
    }

    public boolean checkResource(Locale locale) throws Exception {
        return true;
    }

    @Override // org.springframework.web.servlet.view.AbstractView
    public String toString() {
        return super.toString() + "; URL [" + getUrl() + "]";
    }
}
