package org.springframework.web.servlet.view.script;

import org.springframework.web.servlet.view.UrlBasedViewResolver;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/view/script/ScriptTemplateViewResolver.class */
public class ScriptTemplateViewResolver extends UrlBasedViewResolver {
    public ScriptTemplateViewResolver() {
        setViewClass(requiredViewClass());
    }

    public ScriptTemplateViewResolver(String prefix, String suffix) {
        this();
        setPrefix(prefix);
        setSuffix(suffix);
    }

    @Override // org.springframework.web.servlet.view.UrlBasedViewResolver
    protected Class<?> requiredViewClass() {
        return ScriptTemplateView.class;
    }
}
