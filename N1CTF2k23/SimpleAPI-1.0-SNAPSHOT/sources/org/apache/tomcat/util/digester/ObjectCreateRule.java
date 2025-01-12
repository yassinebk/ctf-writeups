package org.apache.tomcat.util.digester;

import org.xml.sax.Attributes;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/digester/ObjectCreateRule.class */
public class ObjectCreateRule extends Rule {
    protected String attributeName;
    protected String className;

    public ObjectCreateRule(String className) {
        this(className, null);
    }

    public ObjectCreateRule(String className, String attributeName) {
        this.attributeName = null;
        this.className = null;
        this.className = className;
        this.attributeName = attributeName;
    }

    @Override // org.apache.tomcat.util.digester.Rule
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        String realClassName = getRealClassName(attributes);
        if (realClassName == null) {
            throw new NullPointerException(sm.getString("rule.noClassName", namespace, name));
        }
        Class<?> clazz = this.digester.getClassLoader().loadClass(realClassName);
        Object instance = clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
        this.digester.push(instance);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getRealClassName(Attributes attributes) {
        String value;
        String realClassName = this.className;
        if (this.attributeName != null && (value = attributes.getValue(this.attributeName)) != null) {
            realClassName = value;
        }
        return realClassName;
    }

    @Override // org.apache.tomcat.util.digester.Rule
    public void end(String namespace, String name) throws Exception {
        Object top = this.digester.pop();
        if (this.digester.log.isDebugEnabled()) {
            this.digester.log.debug("[ObjectCreateRule]{" + this.digester.match + "} Pop " + top.getClass().getName());
        }
    }

    public String toString() {
        return "ObjectCreateRule[className=" + this.className + ", attributeName=" + this.attributeName + "]";
    }
}
