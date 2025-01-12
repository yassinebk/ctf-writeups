package com.fasterxml.jackson.core;

import java.io.IOException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-core-2.11.0.jar:com/fasterxml/jackson/core/JsonProcessingException.class */
public class JsonProcessingException extends IOException {
    static final long serialVersionUID = 123;
    protected JsonLocation _location;

    /* JADX INFO: Access modifiers changed from: protected */
    public JsonProcessingException(String msg, JsonLocation loc, Throwable rootCause) {
        super(msg);
        if (rootCause != null) {
            initCause(rootCause);
        }
        this._location = loc;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JsonProcessingException(String msg) {
        super(msg);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JsonProcessingException(String msg, JsonLocation loc) {
        this(msg, loc, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JsonProcessingException(String msg, Throwable rootCause) {
        this(msg, null, rootCause);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JsonProcessingException(Throwable rootCause) {
        this(null, null, rootCause);
    }

    public JsonLocation getLocation() {
        return this._location;
    }

    public void clearLocation() {
        this._location = null;
    }

    public String getOriginalMessage() {
        return super.getMessage();
    }

    public Object getProcessor() {
        return null;
    }

    protected String getMessageSuffix() {
        return null;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String msg = super.getMessage();
        if (msg == null) {
            msg = "N/A";
        }
        JsonLocation loc = getLocation();
        String suffix = getMessageSuffix();
        if (loc != null || suffix != null) {
            StringBuilder sb = new StringBuilder(100);
            sb.append(msg);
            if (suffix != null) {
                sb.append(suffix);
            }
            if (loc != null) {
                sb.append('\n');
                sb.append(" at ");
                sb.append(loc.toString());
            }
            msg = sb.toString();
        }
        return msg;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return getClass().getName() + ": " + getMessage();
    }
}
