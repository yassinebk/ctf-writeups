package org.apache.catalina.users;

import org.apache.catalina.UserDatabase;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/users/MemoryRole.class */
public class MemoryRole extends AbstractRole {
    protected final MemoryUserDatabase database;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MemoryRole(MemoryUserDatabase database, String rolename, String description) {
        this.database = database;
        setRolename(rolename);
        setDescription(description);
    }

    @Override // org.apache.catalina.users.AbstractRole, org.apache.catalina.Role
    public UserDatabase getUserDatabase() {
        return this.database;
    }

    @Override // java.security.Principal
    public String toString() {
        StringBuilder sb = new StringBuilder("<role rolename=\"");
        sb.append(this.rolename);
        sb.append("\"");
        if (this.description != null) {
            sb.append(" description=\"");
            sb.append(this.description);
            sb.append("\"");
        }
        sb.append("/>");
        return sb.toString();
    }
}
