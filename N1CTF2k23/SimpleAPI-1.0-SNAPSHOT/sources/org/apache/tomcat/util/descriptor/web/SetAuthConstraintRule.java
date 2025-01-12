package org.apache.tomcat.util.descriptor.web;

import org.apache.tomcat.util.digester.Rule;
import org.xml.sax.Attributes;
/* compiled from: WebRuleSet.java */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/descriptor/web/SetAuthConstraintRule.class */
final class SetAuthConstraintRule extends Rule {
    @Override // org.apache.tomcat.util.digester.Rule
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        SecurityConstraint securityConstraint = (SecurityConstraint) this.digester.peek();
        securityConstraint.setAuthConstraint(true);
        if (this.digester.getLogger().isDebugEnabled()) {
            this.digester.getLogger().debug("Calling SecurityConstraint.setAuthConstraint(true)");
        }
    }
}
