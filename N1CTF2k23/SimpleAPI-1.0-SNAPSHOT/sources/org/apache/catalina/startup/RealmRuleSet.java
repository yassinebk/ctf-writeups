package org.apache.catalina.startup;

import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.RuleSet;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/startup/RealmRuleSet.class */
public class RealmRuleSet implements RuleSet {
    private static final int MAX_NESTED_REALM_LEVELS = Integer.getInteger("org.apache.catalina.startup.RealmRuleSet.MAX_NESTED_REALM_LEVELS", 3).intValue();
    protected final String prefix;

    public RealmRuleSet() {
        this("");
    }

    public RealmRuleSet(String prefix) {
        this.prefix = prefix;
    }

    @Override // org.apache.tomcat.util.digester.RuleSet
    public void addRuleInstances(Digester digester) {
        StringBuilder pattern = new StringBuilder(this.prefix);
        int i = 0;
        while (i < MAX_NESTED_REALM_LEVELS) {
            if (i > 0) {
                pattern.append('/');
            }
            pattern.append("Realm");
            addRuleInstances(digester, pattern.toString(), i == 0 ? "setRealm" : "addRealm");
            i++;
        }
    }

    private void addRuleInstances(Digester digester, String pattern, String methodName) {
        digester.addObjectCreate(pattern, null, "className");
        digester.addSetProperties(pattern);
        digester.addSetNext(pattern, methodName, "org.apache.catalina.Realm");
        digester.addRuleSet(new CredentialHandlerRuleSet(pattern + "/"));
    }
}
