package org.apache.tomcat.util.descriptor.web;

import org.apache.tomcat.util.digester.Rule;
import org.xml.sax.Attributes;
/* compiled from: WebRuleSet.java */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/descriptor/web/VersionRule.class */
final class VersionRule extends Rule {
    @Override // org.apache.tomcat.util.digester.Rule
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        WebXml webxml = (WebXml) this.digester.peek(this.digester.getCount() - 1);
        webxml.setVersion(attributes.getValue("version"));
        if (this.digester.getLogger().isDebugEnabled()) {
            this.digester.getLogger().debug(webxml.getClass().getName() + ".setVersion( " + webxml.getVersion() + ")");
        }
    }
}
