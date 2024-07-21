package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/internal/bind/JsonTreeWriter.class */
public final class JsonTreeWriter extends JsonWriter {
    private static final Writer UNWRITABLE_WRITER = new Writer() { // from class: com.google.gson.internal.bind.JsonTreeWriter.1
        @Override // java.io.Writer
        public void write(char[] buffer, int offset, int counter) {
            throw new AssertionError();
        }

        @Override // java.io.Writer, java.io.Flushable
        public void flush() {
            throw new AssertionError();
        }

        @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            throw new AssertionError();
        }
    };
    private static final JsonPrimitive SENTINEL_CLOSED = new JsonPrimitive("closed");
    private final List<JsonElement> stack;
    private String pendingName;
    private JsonElement product;

    public JsonTreeWriter() {
        super(UNWRITABLE_WRITER);
        this.stack = new ArrayList();
        this.product = JsonNull.INSTANCE;
    }

    public JsonElement get() {
        if (!this.stack.isEmpty()) {
            throw new IllegalStateException("Expected one JSON element but was " + this.stack);
        }
        return this.product;
    }

    private JsonElement peek() {
        return this.stack.get(this.stack.size() - 1);
    }

    private void put(JsonElement value) {
        if (this.pendingName != null) {
            if (!value.isJsonNull() || getSerializeNulls()) {
                JsonObject object = (JsonObject) peek();
                object.add(this.pendingName, value);
            }
            this.pendingName = null;
        } else if (this.stack.isEmpty()) {
            this.product = value;
        } else {
            JsonElement element = peek();
            if (element instanceof JsonArray) {
                ((JsonArray) element).add(value);
                return;
            }
            throw new IllegalStateException();
        }
    }

    @Override // com.google.gson.stream.JsonWriter
    public JsonWriter beginArray() throws IOException {
        JsonArray array = new JsonArray();
        put(array);
        this.stack.add(array);
        return this;
    }

    @Override // com.google.gson.stream.JsonWriter
    public JsonWriter endArray() throws IOException {
        if (this.stack.isEmpty() || this.pendingName != null) {
            throw new IllegalStateException();
        }
        JsonElement element = peek();
        if (element instanceof JsonArray) {
            this.stack.remove(this.stack.size() - 1);
            return this;
        }
        throw new IllegalStateException();
    }

    @Override // com.google.gson.stream.JsonWriter
    public JsonWriter beginObject() throws IOException {
        JsonObject object = new JsonObject();
        put(object);
        this.stack.add(object);
        return this;
    }

    @Override // com.google.gson.stream.JsonWriter
    public JsonWriter endObject() throws IOException {
        if (this.stack.isEmpty() || this.pendingName != null) {
            throw new IllegalStateException();
        }
        JsonElement element = peek();
        if (element instanceof JsonObject) {
            this.stack.remove(this.stack.size() - 1);
            return this;
        }
        throw new IllegalStateException();
    }

    @Override // com.google.gson.stream.JsonWriter
    public JsonWriter name(String name) throws IOException {
        Objects.requireNonNull(name, "name == null");
        if (this.stack.isEmpty() || this.pendingName != null) {
            throw new IllegalStateException();
        }
        JsonElement element = peek();
        if (element instanceof JsonObject) {
            this.pendingName = name;
            return this;
        }
        throw new IllegalStateException();
    }

    @Override // com.google.gson.stream.JsonWriter
    public JsonWriter value(String value) throws IOException {
        if (value == null) {
            return nullValue();
        }
        put(new JsonPrimitive(value));
        return this;
    }

    @Override // com.google.gson.stream.JsonWriter
    public JsonWriter jsonValue(String value) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.gson.stream.JsonWriter
    public JsonWriter nullValue() throws IOException {
        put(JsonNull.INSTANCE);
        return this;
    }

    @Override // com.google.gson.stream.JsonWriter
    public JsonWriter value(boolean value) throws IOException {
        put(new JsonPrimitive(Boolean.valueOf(value)));
        return this;
    }

    @Override // com.google.gson.stream.JsonWriter
    public JsonWriter value(Boolean value) throws IOException {
        if (value == null) {
            return nullValue();
        }
        put(new JsonPrimitive(value));
        return this;
    }

    @Override // com.google.gson.stream.JsonWriter
    public JsonWriter value(float value) throws IOException {
        if (!isLenient() && (Float.isNaN(value) || Float.isInfinite(value))) {
            throw new IllegalArgumentException("JSON forbids NaN and infinities: " + value);
        }
        put(new JsonPrimitive(Float.valueOf(value)));
        return this;
    }

    @Override // com.google.gson.stream.JsonWriter
    public JsonWriter value(double value) throws IOException {
        if (!isLenient() && (Double.isNaN(value) || Double.isInfinite(value))) {
            throw new IllegalArgumentException("JSON forbids NaN and infinities: " + value);
        }
        put(new JsonPrimitive(Double.valueOf(value)));
        return this;
    }

    @Override // com.google.gson.stream.JsonWriter
    public JsonWriter value(long value) throws IOException {
        put(new JsonPrimitive(Long.valueOf(value)));
        return this;
    }

    @Override // com.google.gson.stream.JsonWriter
    public JsonWriter value(Number value) throws IOException {
        if (value == null) {
            return nullValue();
        }
        if (!isLenient()) {
            double d = value.doubleValue();
            if (Double.isNaN(d) || Double.isInfinite(d)) {
                throw new IllegalArgumentException("JSON forbids NaN and infinities: " + value);
            }
        }
        put(new JsonPrimitive(value));
        return this;
    }

    @Override // com.google.gson.stream.JsonWriter, java.io.Flushable
    public void flush() throws IOException {
    }

    @Override // com.google.gson.stream.JsonWriter, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.stack.isEmpty()) {
            throw new IOException("Incomplete document");
        }
        this.stack.add(SENTINEL_CLOSED);
    }
}
