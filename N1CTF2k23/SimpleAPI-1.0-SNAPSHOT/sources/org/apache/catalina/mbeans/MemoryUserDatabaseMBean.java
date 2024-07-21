package org.apache.catalina.mbeans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.apache.catalina.Group;
import org.apache.catalina.Role;
import org.apache.catalina.User;
import org.apache.catalina.UserDatabase;
import org.apache.tomcat.util.modeler.BaseModelMBean;
import org.apache.tomcat.util.modeler.ManagedBean;
import org.apache.tomcat.util.modeler.Registry;
import org.apache.tomcat.util.res.StringManager;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/mbeans/MemoryUserDatabaseMBean.class */
public class MemoryUserDatabaseMBean extends BaseModelMBean {
    private static final StringManager sm = StringManager.getManager(MemoryUserDatabaseMBean.class);
    protected final Registry registry = MBeanUtils.createRegistry();
    protected final ManagedBean managed = this.registry.findManagedBean("MemoryUserDatabase");
    protected final ManagedBean managedGroup = this.registry.findManagedBean("Group");
    protected final ManagedBean managedRole = this.registry.findManagedBean("Role");
    protected final ManagedBean managedUser = this.registry.findManagedBean("User");

    public String[] getGroups() {
        UserDatabase database = (UserDatabase) this.resource;
        List<String> results = new ArrayList<>();
        Iterator<Group> groups = database.getGroups();
        while (groups.hasNext()) {
            Group group = groups.next();
            results.add(findGroup(group.getGroupname()));
        }
        return (String[]) results.toArray(new String[0]);
    }

    public String[] getRoles() {
        UserDatabase database = (UserDatabase) this.resource;
        List<String> results = new ArrayList<>();
        Iterator<Role> roles = database.getRoles();
        while (roles.hasNext()) {
            Role role = roles.next();
            results.add(findRole(role.getRolename()));
        }
        return (String[]) results.toArray(new String[0]);
    }

    public String[] getUsers() {
        UserDatabase database = (UserDatabase) this.resource;
        List<String> results = new ArrayList<>();
        Iterator<User> users = database.getUsers();
        while (users.hasNext()) {
            User user = users.next();
            results.add(findUser(user.getUsername()));
        }
        return (String[]) results.toArray(new String[0]);
    }

    public String createGroup(String groupname, String description) {
        UserDatabase database = (UserDatabase) this.resource;
        Group group = database.createGroup(groupname, description);
        try {
            MBeanUtils.createMBean(group);
            return findGroup(groupname);
        } catch (Exception e) {
            IllegalArgumentException iae = new IllegalArgumentException(sm.getString("userMBean.createMBeanError.group", groupname));
            iae.initCause(e);
            throw iae;
        }
    }

    public String createRole(String rolename, String description) {
        UserDatabase database = (UserDatabase) this.resource;
        Role role = database.createRole(rolename, description);
        try {
            MBeanUtils.createMBean(role);
            return findRole(rolename);
        } catch (Exception e) {
            IllegalArgumentException iae = new IllegalArgumentException(sm.getString("userMBean.createMBeanError.role", rolename));
            iae.initCause(e);
            throw iae;
        }
    }

    public String createUser(String username, String password, String fullName) {
        UserDatabase database = (UserDatabase) this.resource;
        User user = database.createUser(username, password, fullName);
        try {
            MBeanUtils.createMBean(user);
            return findUser(username);
        } catch (Exception e) {
            IllegalArgumentException iae = new IllegalArgumentException(sm.getString("userMBean.createMBeanError.user", username));
            iae.initCause(e);
            throw iae;
        }
    }

    public String findGroup(String groupname) {
        UserDatabase database = (UserDatabase) this.resource;
        Group group = database.findGroup(groupname);
        if (group == null) {
            return null;
        }
        try {
            ObjectName oname = MBeanUtils.createObjectName(this.managedGroup.getDomain(), group);
            return oname.toString();
        } catch (MalformedObjectNameException e) {
            IllegalArgumentException iae = new IllegalArgumentException(sm.getString("userMBean.createError.group", groupname));
            iae.initCause(e);
            throw iae;
        }
    }

    public String findRole(String rolename) {
        UserDatabase database = (UserDatabase) this.resource;
        Role role = database.findRole(rolename);
        if (role == null) {
            return null;
        }
        try {
            ObjectName oname = MBeanUtils.createObjectName(this.managedRole.getDomain(), role);
            return oname.toString();
        } catch (MalformedObjectNameException e) {
            IllegalArgumentException iae = new IllegalArgumentException(sm.getString("userMBean.createError.role", rolename));
            iae.initCause(e);
            throw iae;
        }
    }

    public String findUser(String username) {
        UserDatabase database = (UserDatabase) this.resource;
        User user = database.findUser(username);
        if (user == null) {
            return null;
        }
        try {
            ObjectName oname = MBeanUtils.createObjectName(this.managedUser.getDomain(), user);
            return oname.toString();
        } catch (MalformedObjectNameException e) {
            IllegalArgumentException iae = new IllegalArgumentException(sm.getString("userMBean.createError.user", username));
            iae.initCause(e);
            throw iae;
        }
    }

    public void removeGroup(String groupname) {
        UserDatabase database = (UserDatabase) this.resource;
        Group group = database.findGroup(groupname);
        if (group == null) {
            return;
        }
        try {
            MBeanUtils.destroyMBean(group);
            database.removeGroup(group);
        } catch (Exception e) {
            IllegalArgumentException iae = new IllegalArgumentException(sm.getString("userMBean.destroyError.group", groupname));
            iae.initCause(e);
            throw iae;
        }
    }

    public void removeRole(String rolename) {
        UserDatabase database = (UserDatabase) this.resource;
        Role role = database.findRole(rolename);
        if (role == null) {
            return;
        }
        try {
            MBeanUtils.destroyMBean(role);
            database.removeRole(role);
        } catch (Exception e) {
            IllegalArgumentException iae = new IllegalArgumentException(sm.getString("userMBean.destroyError.role", rolename));
            iae.initCause(e);
            throw iae;
        }
    }

    public void removeUser(String username) {
        UserDatabase database = (UserDatabase) this.resource;
        User user = database.findUser(username);
        if (user == null) {
            return;
        }
        try {
            MBeanUtils.destroyMBean(user);
            database.removeUser(user);
        } catch (Exception e) {
            IllegalArgumentException iae = new IllegalArgumentException(sm.getString("userMBean.destroyError.user", username));
            iae.initCause(e);
            throw iae;
        }
    }
}
