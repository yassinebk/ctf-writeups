package org.apache.tomcat.util.http;

import org.apache.tomcat.util.res.StringManager;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/http/SameSiteCookies.class */
public enum SameSiteCookies {
    UNSET("Unset"),
    NONE("None"),
    LAX("Lax"),
    STRICT("Strict");
    
    private static final StringManager sm = StringManager.getManager(SameSiteCookies.class);
    private final String value;

    SameSiteCookies(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static SameSiteCookies fromString(String value) {
        SameSiteCookies[] values;
        for (SameSiteCookies sameSiteCookies : values()) {
            if (sameSiteCookies.getValue().equalsIgnoreCase(value)) {
                return sameSiteCookies;
            }
        }
        throw new IllegalStateException(sm.getString("cookies.invalidSameSiteCookies", value));
    }
}
