package org.apache.catalina.users;

import org.apache.catalina.Role;
import org.apache.tomcat.util.digester.AbstractObjectCreationFactory;
import org.xml.sax.Attributes;
/* compiled from: MemoryUserDatabase.java */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/users/MemoryRoleCreationFactory.class */
class MemoryRoleCreationFactory extends AbstractObjectCreationFactory {
    private final MemoryUserDatabase database;

    public MemoryRoleCreationFactory(MemoryUserDatabase database) {
        this.database = database;
    }

    @Override // org.apache.tomcat.util.digester.AbstractObjectCreationFactory, org.apache.tomcat.util.digester.ObjectCreationFactory
    public Object createObject(Attributes attributes) {
        String rolename = attributes.getValue("rolename");
        if (rolename == null) {
            rolename = attributes.getValue("name");
        }
        String description = attributes.getValue("description");
        Role role = this.database.createRole(rolename, description);
        return role;
    }
}
