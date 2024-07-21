package com.fasterxml.jackson.datatype.jsr310.deser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-datatype-jsr310-2.11.0.jar:com/fasterxml/jackson/datatype/jsr310/deser/MonthDayDeserializer.class */
public class MonthDayDeserializer extends JSR310DateTimeDeserializerBase<MonthDay> {
    private static final long serialVersionUID = 1;
    public static final MonthDayDeserializer INSTANCE = new MonthDayDeserializer(null);

    public MonthDayDeserializer(DateTimeFormatter formatter) {
        super(MonthDay.class, formatter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase
    /* renamed from: withDateFormat */
    public JSR310DateTimeDeserializerBase<MonthDay> withDateFormat2(DateTimeFormatter dtf) {
        return new MonthDayDeserializer(dtf);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase, com.fasterxml.jackson.datatype.jsr310.deser.JSR310DeserializerBase
    public MonthDayDeserializer withLeniency(Boolean leniency) {
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase
    /* renamed from: withShape */
    public JSR310DateTimeDeserializerBase<MonthDay> withShape2(JsonFormat.Shape shape) {
        return this;
    }

    @Override // com.fasterxml.jackson.databind.JsonDeserializer
    public MonthDay deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.hasToken(JsonToken.VALUE_STRING)) {
            String string = parser.getValueAsString().trim();
            try {
                if (this._formatter == null) {
                    return MonthDay.parse(string);
                }
                return MonthDay.parse(string, this._formatter);
            } catch (DateTimeException e) {
                return (MonthDay) _handleDateTimeException(context, e, string);
            }
        } else if (parser.isExpectedStartArrayToken()) {
            JsonToken t = parser.nextToken();
            if (t == JsonToken.END_ARRAY) {
                return null;
            }
            if ((t == JsonToken.VALUE_STRING || t == JsonToken.VALUE_EMBEDDED_OBJECT) && context.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
                MonthDay parsed = deserialize(parser, context);
                if (parser.nextToken() != JsonToken.END_ARRAY) {
                    handleMissingEndArrayForSingle(parser, context);
                }
                return parsed;
            }
            if (t != JsonToken.VALUE_NUMBER_INT) {
                _reportWrongToken(context, JsonToken.VALUE_NUMBER_INT, "month");
            }
            int month = parser.getIntValue();
            int day = parser.nextIntValue(-1);
            if (day == -1) {
                if (!parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
                    _reportWrongToken(context, JsonToken.VALUE_NUMBER_INT, "day");
                }
                day = parser.getIntValue();
            }
            if (parser.nextToken() != JsonToken.END_ARRAY) {
                throw context.wrongTokenException(parser, handledType(), JsonToken.END_ARRAY, "Expected array to end");
            }
            return MonthDay.of(month, day);
        } else if (parser.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
            return (MonthDay) parser.getEmbeddedObject();
        } else {
            return (MonthDay) _handleUnexpectedToken(context, parser, JsonToken.VALUE_STRING, JsonToken.START_ARRAY);
        }
    }
}
