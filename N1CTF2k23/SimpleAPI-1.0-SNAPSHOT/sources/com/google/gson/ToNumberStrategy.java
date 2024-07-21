package com.google.gson;

import com.google.gson.stream.JsonReader;
import java.io.IOException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/ToNumberStrategy.class */
public interface ToNumberStrategy {
    Number readNumber(JsonReader jsonReader) throws IOException;
}
