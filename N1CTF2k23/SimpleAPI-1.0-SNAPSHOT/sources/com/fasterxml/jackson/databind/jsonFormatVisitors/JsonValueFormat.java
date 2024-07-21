package com.fasterxml.jackson.databind.jsonFormatVisitors;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.web.servlet.tags.form.AbstractHtmlElementTag;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-databind-2.11.0.jar:com/fasterxml/jackson/databind/jsonFormatVisitors/JsonValueFormat.class */
public enum JsonValueFormat {
    COLOR("color"),
    DATE("date"),
    DATE_TIME("date-time"),
    EMAIL("email"),
    HOST_NAME("host-name"),
    IP_ADDRESS("ip-address"),
    IPV6("ipv6"),
    PHONE("phone"),
    REGEX("regex"),
    STYLE(AbstractHtmlElementTag.STYLE_ATTRIBUTE),
    TIME("time"),
    URI("uri"),
    UTC_MILLISEC("utc-millisec"),
    UUID("uuid");
    
    private final String _desc;

    JsonValueFormat(String desc) {
        this._desc = desc;
    }

    @Override // java.lang.Enum
    @JsonValue
    public String toString() {
        return this._desc;
    }
}
