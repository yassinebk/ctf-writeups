package org.apache.tomcat.util.descriptor.web;

import org.apache.tomcat.util.digester.Rule;
import org.xml.sax.Attributes;
/* compiled from: WebRuleSet.java */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/descriptor/web/SetJspConfig.class */
final class SetJspConfig extends Rule {
    boolean isJspConfigSet = false;

    @Override // org.apache.tomcat.util.digester.Rule
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        if (this.isJspConfigSet) {
            throw new IllegalArgumentException("<jsp-config> element is limited to 1 occurrence");
        }
        this.isJspConfigSet = true;
    }
}
