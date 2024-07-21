package org.springframework.http;

import java.time.Duration;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/ResponseCookie.class */
public final class ResponseCookie extends HttpCookie {
    private final Duration maxAge;
    @Nullable
    private final String domain;
    @Nullable
    private final String path;
    private final boolean secure;
    private final boolean httpOnly;
    @Nullable
    private final String sameSite;

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/ResponseCookie$ResponseCookieBuilder.class */
    public interface ResponseCookieBuilder {
        ResponseCookieBuilder maxAge(Duration duration);

        ResponseCookieBuilder maxAge(long j);

        ResponseCookieBuilder path(String str);

        ResponseCookieBuilder domain(String str);

        ResponseCookieBuilder secure(boolean z);

        ResponseCookieBuilder httpOnly(boolean z);

        ResponseCookieBuilder sameSite(@Nullable String str);

        ResponseCookie build();
    }

    private ResponseCookie(String name, String value, Duration maxAge, @Nullable String domain, @Nullable String path, boolean secure, boolean httpOnly, @Nullable String sameSite) {
        super(name, value);
        Assert.notNull(maxAge, "Max age must not be null");
        this.maxAge = maxAge;
        this.domain = domain;
        this.path = path;
        this.secure = secure;
        this.httpOnly = httpOnly;
        this.sameSite = sameSite;
        Rfc6265Utils.validateCookieName(name);
        Rfc6265Utils.validateCookieValue(value);
        Rfc6265Utils.validateDomain(domain);
        Rfc6265Utils.validatePath(path);
    }

    public Duration getMaxAge() {
        return this.maxAge;
    }

    @Nullable
    public String getDomain() {
        return this.domain;
    }

    @Nullable
    public String getPath() {
        return this.path;
    }

    public boolean isSecure() {
        return this.secure;
    }

    public boolean isHttpOnly() {
        return this.httpOnly;
    }

    @Nullable
    public String getSameSite() {
        return this.sameSite;
    }

    @Override // org.springframework.http.HttpCookie
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ResponseCookie)) {
            return false;
        }
        ResponseCookie otherCookie = (ResponseCookie) other;
        return getName().equalsIgnoreCase(otherCookie.getName()) && ObjectUtils.nullSafeEquals(this.path, otherCookie.getPath()) && ObjectUtils.nullSafeEquals(this.domain, otherCookie.getDomain());
    }

    @Override // org.springframework.http.HttpCookie
    public int hashCode() {
        int result = super.hashCode();
        return (31 * ((31 * result) + ObjectUtils.nullSafeHashCode(this.domain))) + ObjectUtils.nullSafeHashCode(this.path);
    }

    @Override // org.springframework.http.HttpCookie
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append('=').append(getValue());
        if (StringUtils.hasText(getPath())) {
            sb.append("; Path=").append(getPath());
        }
        if (StringUtils.hasText(this.domain)) {
            sb.append("; Domain=").append(this.domain);
        }
        if (!this.maxAge.isNegative()) {
            sb.append("; Max-Age=").append(this.maxAge.getSeconds());
            sb.append("; Expires=");
            long millis = this.maxAge.getSeconds() > 0 ? System.currentTimeMillis() + this.maxAge.toMillis() : 0L;
            sb.append(HttpHeaders.formatDate(millis));
        }
        if (this.secure) {
            sb.append("; Secure");
        }
        if (this.httpOnly) {
            sb.append("; HttpOnly");
        }
        if (StringUtils.hasText(this.sameSite)) {
            sb.append("; SameSite=").append(this.sameSite);
        }
        return sb.toString();
    }

    public static ResponseCookieBuilder from(String name, String value) {
        return from(name, value, false);
    }

    public static ResponseCookieBuilder fromClientResponse(String name, String value) {
        return from(name, value, true);
    }

    private static ResponseCookieBuilder from(final String name, final String value, final boolean lenient) {
        return new ResponseCookieBuilder() { // from class: org.springframework.http.ResponseCookie.1
            private Duration maxAge = Duration.ofSeconds(-1);
            @Nullable
            private String domain;
            @Nullable
            private String path;
            private boolean secure;
            private boolean httpOnly;
            @Nullable
            private String sameSite;

            @Override // org.springframework.http.ResponseCookie.ResponseCookieBuilder
            public ResponseCookieBuilder maxAge(Duration maxAge) {
                this.maxAge = maxAge;
                return this;
            }

            @Override // org.springframework.http.ResponseCookie.ResponseCookieBuilder
            public ResponseCookieBuilder maxAge(long maxAgeSeconds) {
                this.maxAge = maxAgeSeconds >= 0 ? Duration.ofSeconds(maxAgeSeconds) : Duration.ofSeconds(-1L);
                return this;
            }

            @Override // org.springframework.http.ResponseCookie.ResponseCookieBuilder
            public ResponseCookieBuilder domain(String domain) {
                this.domain = initDomain(domain);
                return this;
            }

            @Nullable
            private String initDomain(String domain) {
                if (lenient && !StringUtils.isEmpty(domain)) {
                    String s = domain.trim();
                    if (s.startsWith("\"") && s.endsWith("\"") && s.substring(1, s.length() - 1).trim().isEmpty()) {
                        return null;
                    }
                }
                return domain;
            }

            @Override // org.springframework.http.ResponseCookie.ResponseCookieBuilder
            public ResponseCookieBuilder path(String path) {
                this.path = path;
                return this;
            }

            @Override // org.springframework.http.ResponseCookie.ResponseCookieBuilder
            public ResponseCookieBuilder secure(boolean secure) {
                this.secure = secure;
                return this;
            }

            @Override // org.springframework.http.ResponseCookie.ResponseCookieBuilder
            public ResponseCookieBuilder httpOnly(boolean httpOnly) {
                this.httpOnly = httpOnly;
                return this;
            }

            @Override // org.springframework.http.ResponseCookie.ResponseCookieBuilder
            public ResponseCookieBuilder sameSite(@Nullable String sameSite) {
                this.sameSite = sameSite;
                return this;
            }

            @Override // org.springframework.http.ResponseCookie.ResponseCookieBuilder
            public ResponseCookie build() {
                return new ResponseCookie(name, value, this.maxAge, this.domain, this.path, this.secure, this.httpOnly, this.sameSite);
            }
        };
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/ResponseCookie$Rfc6265Utils.class */
    private static class Rfc6265Utils {
        private static final String SEPARATOR_CHARS = "()<>@,;:\\\"/[]?={} ";
        private static final String DOMAIN_CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-";

        private Rfc6265Utils() {
        }

        public static void validateCookieName(String name) {
            for (int i = 0; i < name.length(); i++) {
                char c = name.charAt(i);
                if (c <= 31 || c == 127) {
                    throw new IllegalArgumentException(name + ": RFC2616 token cannot have control chars");
                }
                if (SEPARATOR_CHARS.indexOf(c) >= 0) {
                    throw new IllegalArgumentException(name + ": RFC2616 token cannot have separator chars such as '" + c + "'");
                }
                if (c >= 128) {
                    throw new IllegalArgumentException(name + ": RFC2616 token can only have US-ASCII: 0x" + Integer.toHexString(c));
                }
            }
        }

        public static void validateCookieValue(@Nullable String value) {
            if (value == null) {
                return;
            }
            int start = 0;
            int end = value.length();
            if (end > 1 && value.charAt(0) == '\"' && value.charAt(end - 1) == '\"') {
                start = 1;
                end--;
            }
            char[] chars = value.toCharArray();
            for (int i = start; i < end; i++) {
                char c = chars[i];
                if (c < '!' || c == '\"' || c == ',' || c == ';' || c == '\\' || c == 127) {
                    throw new IllegalArgumentException("RFC2616 cookie value cannot have '" + c + "'");
                }
                if (c >= 128) {
                    throw new IllegalArgumentException("RFC2616 cookie value can only have US-ASCII chars: 0x" + Integer.toHexString(c));
                }
            }
        }

        public static void validateDomain(@Nullable String domain) {
            if (!StringUtils.hasLength(domain)) {
                return;
            }
            int char1 = domain.charAt(0);
            int charN = domain.charAt(domain.length() - 1);
            if (char1 == 45 || charN == 46 || charN == 45) {
                throw new IllegalArgumentException("Invalid first/last char in cookie domain: " + domain);
            }
            int c = -1;
            for (int i = 0; i < domain.length(); i++) {
                int p = c;
                c = domain.charAt(i);
                if (DOMAIN_CHARS.indexOf(c) == -1 || ((p == 46 && (c == 46 || c == 45)) || (p == 45 && c == 46))) {
                    throw new IllegalArgumentException(domain + ": invalid cookie domain char '" + c + "'");
                }
            }
        }

        public static void validatePath(@Nullable String path) {
            if (path == null) {
                return;
            }
            for (int i = 0; i < path.length(); i++) {
                char c = path.charAt(i);
                if (c < ' ' || c > '~' || c == ';') {
                    throw new IllegalArgumentException(path + ": Invalid cookie path char '" + c + "'");
                }
            }
        }
    }
}
