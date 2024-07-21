package org.apache.catalina.startup;

import java.util.HashMap;
import java.util.Set;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.digester.ObjectCreateRule;
import org.apache.tomcat.util.res.StringManager;
import org.xml.sax.Attributes;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/startup/ListenerCreateRule.class */
public class ListenerCreateRule extends ObjectCreateRule {
    private static final Log log = LogFactory.getLog(ListenerCreateRule.class);
    protected static final StringManager sm = StringManager.getManager(ListenerCreateRule.class);

    public ListenerCreateRule(String className, String attributeName) {
        super(className, attributeName);
    }

    @Override // org.apache.tomcat.util.digester.ObjectCreateRule, org.apache.tomcat.util.digester.Rule
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        if ("true".equals(attributes.getValue("optional"))) {
            try {
                super.begin(namespace, name, attributes);
                return;
            } catch (Exception e) {
                String className = getRealClassName(attributes);
                if (log.isDebugEnabled()) {
                    log.info(sm.getString("listener.createFailed", className), e);
                } else {
                    log.info(sm.getString("listener.createFailed", className));
                }
                this.digester.push(new OptionalListener(className));
                return;
            }
        }
        super.begin(namespace, name, attributes);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/startup/ListenerCreateRule$OptionalListener.class */
    public class OptionalListener implements LifecycleListener {
        protected final String className;
        protected final HashMap<String, String> properties = new HashMap<>();

        public OptionalListener(String className) {
            this.className = className;
        }

        public String getClassName() {
            return this.className;
        }

        @Override // org.apache.catalina.LifecycleListener
        public void lifecycleEvent(LifecycleEvent event) {
        }

        public Set<String> getProperties() {
            return this.properties.keySet();
        }

        public Object getProperty(String name) {
            return this.properties.get(name);
        }

        public boolean setProperty(String name, String value) {
            this.properties.put(name, value);
            return true;
        }
    }
}
