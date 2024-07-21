package org.apache.catalina.session;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.apache.catalina.Globals;
import org.apache.catalina.valves.CrawlerSessionManagerValve;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/session/Constants.class */
public class Constants {
    public static final Set<String> excludedAttributeNames;

    static {
        Set<String> names = new HashSet<>();
        names.add(Globals.SUBJECT_ATTR);
        names.add(CrawlerSessionManagerValve.class.getName());
        excludedAttributeNames = Collections.unmodifiableSet(names);
    }
}
