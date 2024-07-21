package org.apache.tomcat.util.descriptor.web;

import org.apache.tomcat.util.digester.Rule;
/* compiled from: WebRuleSet.java */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/descriptor/web/MappedNameRule.class */
final class MappedNameRule extends Rule {
    @Override // org.apache.tomcat.util.digester.Rule
    public void body(String namespace, String name, String text) throws Exception {
        ResourceBase resourceBase = (ResourceBase) this.digester.peek();
        resourceBase.setProperty("mappedName", text.trim());
    }
}
