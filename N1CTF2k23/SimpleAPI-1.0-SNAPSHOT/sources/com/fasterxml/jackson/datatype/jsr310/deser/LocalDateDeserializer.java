package com.fasterxml.jackson.datatype.jsr310.deser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-datatype-jsr310-2.11.0.jar:com/fasterxml/jackson/datatype/jsr310/deser/LocalDateDeserializer.class */
public class LocalDateDeserializer extends JSR310DateTimeDeserializerBase<LocalDate> {
    private static final long serialVersionUID = 1;
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    public static final LocalDateDeserializer INSTANCE = new LocalDateDeserializer();

    protected LocalDateDeserializer() {
        this(DEFAULT_FORMATTER);
    }

    public LocalDateDeserializer(DateTimeFormatter dtf) {
        super(LocalDate.class, dtf);
    }

    public LocalDateDeserializer(LocalDateDeserializer base, DateTimeFormatter dtf) {
        super(base, dtf);
    }

    protected LocalDateDeserializer(LocalDateDeserializer base, Boolean leniency) {
        super(base, leniency);
    }

    protected LocalDateDeserializer(LocalDateDeserializer base, JsonFormat.Shape shape) {
        super(base, shape);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase
    /* renamed from: withDateFormat */
    public JSR310DateTimeDeserializerBase<LocalDate> withDateFormat2(DateTimeFormatter dtf) {
        return new LocalDateDeserializer(this, dtf);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase, com.fasterxml.jackson.datatype.jsr310.deser.JSR310DeserializerBase
    public LocalDateDeserializer withLeniency(Boolean leniency) {
        return new LocalDateDeserializer(this, leniency);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.fasterxml.jackson.datatype.jsr310.deser.JSR310DateTimeDeserializerBase
    /* renamed from: withShape */
    public JSR310DateTimeDeserializerBase<LocalDate> withShape2(JsonFormat.Shape shape) {
        return new LocalDateDeserializer(this, shape);
    }

    @Override // com.fasterxml.jackson.databind.JsonDeserializer
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.hasToken(JsonToken.VALUE_STRING)) {
            String string = parser.getText().trim();
            if (string.length() == 0) {
                if (!isLenient()) {
                    return _failForNotLenient(parser, context, JsonToken.VALUE_STRING);
                }
                return null;
            }
            DateTimeFormatter format = this._formatter;
            try {
                if (format == DEFAULT_FORMATTER && string.length() > 10 && string.charAt(10) == 'T') {
                    if (string.endsWith("Z")) {
                        return LocalDateTime.ofInstant(Instant.parse(string), ZoneOffset.UTC).toLocalDate();
                    }
                    return LocalDate.parse(string, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                }
                return LocalDate.parse(string, format);
            } catch (DateTimeException e) {
                return (LocalDate) _handleDateTimeException(context, e, string);
            }
        }
        if (parser.isExpectedStartArrayToken()) {
            JsonToken t = parser.nextToken();
            if (t == JsonToken.END_ARRAY) {
                return null;
            }
            if (context.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS) && (t == JsonToken.VALUE_STRING || t == JsonToken.VALUE_EMBEDDED_OBJECT)) {
                LocalDate parsed = deserialize(parser, context);
                if (parser.nextToken() != JsonToken.END_ARRAY) {
                    handleMissingEndArrayForSingle(parser, context);
                }
                return parsed;
            } else if (t == JsonToken.VALUE_NUMBER_INT) {
                int year = parser.getIntValue();
                int month = parser.nextIntValue(-1);
                int day = parser.nextIntValue(-1);
                if (parser.nextToken() != JsonToken.END_ARRAY) {
                    throw context.wrongTokenException(parser, handledType(), JsonToken.END_ARRAY, "Expected array to end");
                }
                return LocalDate.of(year, month, day);
            } else {
                context.reportInputMismatch(handledType(), "Unexpected token (%s) within Array, expected VALUE_NUMBER_INT", t);
            }
        }
        if (parser.hasToken(JsonToken.VALUE_EMBEDDED_OBJECT)) {
            return (LocalDate) parser.getEmbeddedObject();
        }
        if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
            if (this._shape == JsonFormat.Shape.NUMBER_INT || isLenient()) {
                return LocalDate.ofEpochDay(parser.getLongValue());
            }
            return _failForNotLenient(parser, context, JsonToken.VALUE_STRING);
        }
        return (LocalDate) _handleUnexpectedToken(context, parser, "Expected array or string.", new Object[0]);
    }
}
