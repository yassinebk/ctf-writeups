package org.apache.naming;

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/naming/NameParserImpl.class */
public class NameParserImpl implements NameParser {
    public Name parse(String name) throws NamingException {
        return new CompositeName(name);
    }
}
