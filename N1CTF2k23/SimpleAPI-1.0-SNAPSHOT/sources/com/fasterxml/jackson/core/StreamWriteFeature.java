package com.fasterxml.jackson.core;

import com.fasterxml.jackson.core.JsonGenerator;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-core-2.11.0.jar:com/fasterxml/jackson/core/StreamWriteFeature.class */
public enum StreamWriteFeature {
    AUTO_CLOSE_TARGET(JsonGenerator.Feature.AUTO_CLOSE_TARGET),
    AUTO_CLOSE_CONTENT(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT),
    FLUSH_PASSED_TO_STREAM(JsonGenerator.Feature.FLUSH_PASSED_TO_STREAM),
    WRITE_BIGDECIMAL_AS_PLAIN(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN),
    STRICT_DUPLICATE_DETECTION(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION),
    IGNORE_UNKNOWN(JsonGenerator.Feature.IGNORE_UNKNOWN);
    
    private final boolean _defaultState;
    private final int _mask;
    private final JsonGenerator.Feature _mappedFeature;

    StreamWriteFeature(JsonGenerator.Feature mappedTo) {
        this._mappedFeature = mappedTo;
        this._mask = mappedTo.getMask();
        this._defaultState = mappedTo.enabledByDefault();
    }

    public static int collectDefaults() {
        StreamWriteFeature[] values;
        int flags = 0;
        for (StreamWriteFeature f : values()) {
            if (f.enabledByDefault()) {
                flags |= f.getMask();
            }
        }
        return flags;
    }

    public boolean enabledByDefault() {
        return this._defaultState;
    }

    public boolean enabledIn(int flags) {
        return (flags & this._mask) != 0;
    }

    public int getMask() {
        return this._mask;
    }

    public JsonGenerator.Feature mappedFeature() {
        return this._mappedFeature;
    }
}