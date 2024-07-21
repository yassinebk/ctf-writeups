package org.springframework.boot.web.embedded.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.core.StandardContext;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/tomcat/DisableReferenceClearingContextCustomizer.class */
public class DisableReferenceClearingContextCustomizer implements TomcatContextCustomizer {
    @Override // org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer
    public void customize(Context context) {
        if (!(context instanceof StandardContext)) {
            return;
        }
        StandardContext standardContext = (StandardContext) context;
        try {
            standardContext.setClearReferencesObjectStreamClassCaches(false);
            standardContext.setClearReferencesRmiTargets(false);
            standardContext.setClearReferencesThreadLocals(false);
        } catch (NoSuchMethodError e) {
        }
    }
}
