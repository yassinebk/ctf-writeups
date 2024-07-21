package com.fasterxml.jackson.databind.jsonFormatVisitors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-databind-2.11.0.jar:com/fasterxml/jackson/databind/jsonFormatVisitors/JsonFormatTypes.class */
public enum JsonFormatTypes {
    STRING,
    NUMBER,
    INTEGER,
    BOOLEAN,
    OBJECT,
    ARRAY,
    NULL,
    ANY;
    
    private static final Map<String, JsonFormatTypes> _byLCName = new HashMap();

    static {
        JsonFormatTypes[] values;
        for (JsonFormatTypes t : values()) {
            _byLCName.put(t.name().toLowerCase(), t);
        }
    }

    @JsonValue
    public String value() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static JsonFormatTypes forValue(String s) {
        return _byLCName.get(s);
    }
}
