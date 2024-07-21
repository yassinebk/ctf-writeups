package com.sun.el.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/util/MessageFactory.class */
public final class MessageFactory {
    protected static final ResourceBundle bundle = ResourceBundle.getBundle("com.sun.el.Messages");

    public static String get(String key) {
        return bundle.getString(key);
    }

    public static String get(String key, Object obj0) {
        return getArray(key, new Object[]{obj0});
    }

    public static String get(String key, Object obj0, Object obj1) {
        return getArray(key, new Object[]{obj0, obj1});
    }

    public static String get(String key, Object obj0, Object obj1, Object obj2) {
        return getArray(key, new Object[]{obj0, obj1, obj2});
    }

    public static String get(String key, Object obj0, Object obj1, Object obj2, Object obj3) {
        return getArray(key, new Object[]{obj0, obj1, obj2, obj3});
    }

    public static String get(String key, Object obj0, Object obj1, Object obj2, Object obj3, Object obj4) {
        return getArray(key, new Object[]{obj0, obj1, obj2, obj3, obj4});
    }

    public static String getArray(String key, Object[] objA) {
        return MessageFormat.format(bundle.getString(key), objA);
    }
}
