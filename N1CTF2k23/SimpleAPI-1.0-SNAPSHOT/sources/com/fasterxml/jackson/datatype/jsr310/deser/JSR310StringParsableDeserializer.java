package com.fasterxml.jackson.datatype.jsr310.deser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-datatype-jsr310-2.11.0.jar:com/fasterxml/jackson/datatype/jsr310/deser/JSR310StringParsableDeserializer.class */
public class JSR310StringParsableDeserializer extends JSR310DeserializerBase<Object> implements ContextualDeserializer {
    private static final long serialVersionUID = 1;
    protected static final int TYPE_PERIOD = 1;
    protected static final int TYPE_ZONE_ID = 2;
    protected static final int TYPE_ZONE_OFFSET = 3;
    public static final JsonDeserializer<Period> PERIOD = createDeserializer(Period.class, 1);
    public static final JsonDeserializer<ZoneId> ZONE_ID = createDeserializer(ZoneId.class, 2);
    public static final JsonDeserializer<ZoneOffset> ZONE_OFFSET = createDeserializer(ZoneOffset.class, 3);
    protected final int _typeSelector;

    protected JSR310StringParsableDeserializer(Class<?> supportedType, int typeSelector) {
        super(supportedType);
        this._typeSelector = typeSelector;
    }

    protected JSR310StringParsableDeserializer(JSR310StringParsableDeserializer base, Boolean leniency) {
        super(base, leniency);
        this._typeSelector = base._typeSelector;
    }

    protected static <T> JsonDeserializer<T> createDeserializer(Class<T> type, int typeId) {
        return new JSR310StringParsableDeserializer((Class<?>) type, typeId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.fasterxml.jackson.datatype.jsr310.deser.JSR310DeserializerBase
    /* renamed from: withLeniency */
    public JSR310DeserializerBase<Object> withLeniency2(Boolean leniency) {
        if (this._isLenient == (!Boolean.FALSE.equals(leniency))) {
            return this;
        }
        return new JSR310StringParsableDeserializer(this, leniency);
    }

    @Override // com.fasterxml.jackson.databind.JsonDeserializer
    public Object deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String string = p.getValueAsString();
        if (string != null) {
            String string2 = string.trim();
            if (string2.length() == 0) {
                if (!isLenient()) {
                    return _failForNotLenient(p, context, JsonToken.VALUE_STRING);
                }
                return null;
            }
            try {
                switch (this._typeSelector) {
                    case 1:
                        return Period.parse(string2);
                    case 2:
                        return ZoneId.of(string2);
                    case 3:
                        return ZoneOffset.of(string2);
                }
            } catch (DateTimeException e) {
                return _handleDateTimeException(context, e, string2);
            }
        }
        if (p.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
            return p.getEmbeddedObject();
        }
        if (p.hasToken(JsonToken.START_ARRAY)) {
            return _deserializeFromArray(p, context);
        }
        throw context.wrongTokenException(p, handledType(), JsonToken.VALUE_STRING, (String) null);
    }

    @Override // com.fasterxml.jackson.datatype.jsr310.deser.JSR310DeserializerBase, com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer, com.fasterxml.jackson.databind.deser.std.StdDeserializer, com.fasterxml.jackson.databind.JsonDeserializer
    public Object deserializeWithType(JsonParser parser, DeserializationContext context, TypeDeserializer deserializer) throws IOException {
        JsonToken t = parser.getCurrentToken();
        if (t != null && t.isScalarValue()) {
            return deserialize(parser, context);
        }
        return deserializer.deserializeTypedFromAny(parser, context);
    }

    @Override // com.fasterxml.jackson.databind.deser.ContextualDeserializer
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        Boolean leniency;
        JsonFormat.Value format = findFormatOverrides(ctxt, property, handledType());
        JSR310DeserializerBase<Object> jSR310DeserializerBase = this;
        if (format != null && format.hasLenient() && (leniency = format.getLenient()) != null) {
            jSR310DeserializerBase = withLeniency2(leniency);
        }
        return jSR310DeserializerBase;
    }
}
