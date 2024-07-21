package org.springframework.boot.autoconfigure.web.reactive;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.util.StringUtils;
@ConfigurationProperties(prefix = "spring.webflux")
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/reactive/WebFluxProperties.class */
public class WebFluxProperties {
    private String basePath;
    private final Format format = new Format();
    private String staticPathPattern = "/**";

    public String getBasePath() {
        return this.basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = cleanBasePath(basePath);
    }

    private String cleanBasePath(String basePath) {
        String candidate = StringUtils.trimWhitespace(basePath);
        if (StringUtils.hasText(candidate)) {
            if (!candidate.startsWith("/")) {
                candidate = "/" + candidate;
            }
            if (candidate.endsWith("/")) {
                candidate = candidate.substring(0, candidate.length() - 1);
            }
        }
        return candidate;
    }

    @DeprecatedConfigurationProperty(replacement = "spring.webflux.format.date")
    @Deprecated
    public String getDateFormat() {
        return this.format.getDate();
    }

    @Deprecated
    public void setDateFormat(String dateFormat) {
        this.format.setDate(dateFormat);
    }

    public Format getFormat() {
        return this.format;
    }

    public String getStaticPathPattern() {
        return this.staticPathPattern;
    }

    public void setStaticPathPattern(String staticPathPattern) {
        this.staticPathPattern = staticPathPattern;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/reactive/WebFluxProperties$Format.class */
    public static class Format {
        private String date;
        private String time;
        private String dateTime;

        public String getDate() {
            return this.date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return this.time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDateTime() {
            return this.dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }
    }
}
