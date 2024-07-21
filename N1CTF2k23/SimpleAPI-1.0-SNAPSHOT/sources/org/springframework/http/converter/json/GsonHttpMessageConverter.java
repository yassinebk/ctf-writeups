package org.springframework.http.converter.json;

import com.google.gson.Gson;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/converter/json/GsonHttpMessageConverter.class */
public class GsonHttpMessageConverter extends AbstractJsonHttpMessageConverter {
    private Gson gson;

    public GsonHttpMessageConverter() {
        this.gson = new Gson();
    }

    public GsonHttpMessageConverter(Gson gson) {
        Assert.notNull(gson, "A Gson instance is required");
        this.gson = gson;
    }

    public void setGson(Gson gson) {
        Assert.notNull(gson, "A Gson instance is required");
        this.gson = gson;
    }

    public Gson getGson() {
        return this.gson;
    }

    @Override // org.springframework.http.converter.json.AbstractJsonHttpMessageConverter
    protected Object readInternal(Type resolvedType, Reader reader) throws Exception {
        return getGson().fromJson(reader, resolvedType);
    }

    @Override // org.springframework.http.converter.json.AbstractJsonHttpMessageConverter
    protected void writeInternal(Object o, @Nullable Type type, Writer writer) throws Exception {
        if (type instanceof ParameterizedType) {
            getGson().toJson(o, type, writer);
        } else {
            getGson().toJson(o, writer);
        }
    }
}
