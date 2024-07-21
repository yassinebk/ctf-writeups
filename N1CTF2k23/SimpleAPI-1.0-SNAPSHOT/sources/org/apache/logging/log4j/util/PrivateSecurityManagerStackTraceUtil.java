package org.apache.logging.log4j.util;

import java.util.Stack;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/util/PrivateSecurityManagerStackTraceUtil.class */
final class PrivateSecurityManagerStackTraceUtil {
    private static final PrivateSecurityManager SECURITY_MANAGER;

    static {
        PrivateSecurityManager psm;
        try {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPermission(new RuntimePermission("createSecurityManager"));
            }
            psm = new PrivateSecurityManager();
        } catch (SecurityException e) {
            psm = null;
        }
        SECURITY_MANAGER = psm;
    }

    private PrivateSecurityManagerStackTraceUtil() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isEnabled() {
        return SECURITY_MANAGER != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Stack<Class<?>> getCurrentStackTrace() {
        Class<?>[] array = SECURITY_MANAGER.getClassContext();
        Stack<Class<?>> classes = new Stack<>();
        classes.ensureCapacity(array.length);
        for (Class<?> clazz : array) {
            classes.push(clazz);
        }
        return classes;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/util/PrivateSecurityManagerStackTraceUtil$PrivateSecurityManager.class */
    private static final class PrivateSecurityManager extends SecurityManager {
        private PrivateSecurityManager() {
        }

        @Override // java.lang.SecurityManager
        protected Class<?>[] getClassContext() {
            return super.getClassContext();
        }
    }
}
