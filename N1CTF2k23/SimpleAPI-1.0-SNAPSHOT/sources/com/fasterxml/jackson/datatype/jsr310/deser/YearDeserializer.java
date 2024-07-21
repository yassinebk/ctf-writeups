package com.fasterxml.jackson.datatype.jsr310.deser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.Year;
import java.time.format.DateTimeFormatter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-datatype-jsr310-2.11.0.jar:com/fasterxml/jackson/datatype/jsr310/deser/YearDeserializer.class */
public class YearDeserializer extends JSR310DateTimeDeserializerBase<Year> {
    private static final long serialVersionUID = 1;
    public static final YearDeserializer INSTANCE = new YearDeserializer();

    private YearDeserializer() {
        this(null);
    }

    public YearDeserializer(DateTimeFormatter formatter) {
        super(Year.class, formatter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase
    /* renamed from: withDateFormat */
    public JSR310DateTimeDeserializerBase<Year> withDateFormat2(DateTimeFormatter dtf) {
        return new YearDeserializer(dtf);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase, com.fasterxml.jackson.datatype.jsr310.deser.JSR310DeserializerBase
    public YearDeserializer withLeniency(Boolean leniency) {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase
    /* renamed from: withShape */
    public JSR310DateTimeDeserializerBase<Year> withShape2(JsonFormat.Shape shape) {
        return this;
    }

    @Override // com.fasterxml.jackson.databind.JsonDeserializer
    public Year deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonToken t = parser.getCurrentToken();
        if (t == JsonToken.VALUE_STRING) {
            String string = parser.getValueAsString().trim();
            try {
                if (this._formatter == null) {
                    return Year.parse(string);
                }
                return Year.parse(string, this._formatter);
            } catch (DateTimeException e) {
                return (Year) _handleDateTimeException(context, e, string);
            }
        } else if (t == JsonToken.VALUE_NUMBER_INT) {
            return Year.of(parser.getIntValue());
        } else {
            if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
                return (Year) parser.getEmbeddedObject();
            }
            if (parser.hasToken(JsonToken.START_ARRAY)) {
                return _deserializeFromArray(parser, context);
            }
            return (Year) _handleUnexpectedToken(context, parser, JsonToken.VALUE_STRING, JsonToken.VALUE_NUMBER_INT);
        }
    }
}
