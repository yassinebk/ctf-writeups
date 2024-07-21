package org.apache.tomcat.util.digester;

import org.apache.tomcat.util.IntrospectionUtils;
import org.xml.sax.Attributes;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/digester/SetPropertiesRule.class */
public class SetPropertiesRule extends Rule {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/digester/SetPropertiesRule$Listener.class */
    public interface Listener {
        void endSetPropertiesRule();
    }

    @Override // org.apache.tomcat.util.digester.Rule
    public void begin(String namespace, String theName, Attributes attributes) throws Exception {
        Object top = this.digester.peek();
        if (this.digester.log.isDebugEnabled()) {
            if (top != null) {
                this.digester.log.debug("[SetPropertiesRule]{" + this.digester.match + "} Set " + top.getClass().getName() + " properties");
            } else {
                this.digester.log.debug("[SetPropertiesRule]{" + this.digester.match + "} Set NULL properties");
            }
        }
        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getLocalName(i);
            if ("".equals(name)) {
                name = attributes.getQName(i);
            }
            String value = attributes.getValue(i);
            if (this.digester.log.isDebugEnabled()) {
                this.digester.log.debug("[SetPropertiesRule]{" + this.digester.match + "} Setting property '" + name + "' to '" + value + "'");
            }
            if (!this.digester.isFakeAttribute(top, name) && !IntrospectionUtils.setProperty(top, name, value) && this.digester.getRulesValidation() && !"optional".equals(name)) {
                this.digester.log.warn(sm.getString("rule.noProperty", this.digester.match, name, value));
            }
        }
        if (top instanceof Listener) {
            ((Listener) top).endSetPropertiesRule();
        }
    }

    public String toString() {
        return "SetPropertiesRule[]";
    }
}
