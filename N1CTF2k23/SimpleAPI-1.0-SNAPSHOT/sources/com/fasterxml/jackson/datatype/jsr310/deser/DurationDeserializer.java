package com.fasterxml.jackson.datatype.jsr310.deser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.DecimalUtils;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.Duration;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-datatype-jsr310-2.11.0.jar:com/fasterxml/jackson/datatype/jsr310/deser/DurationDeserializer.class */
public class DurationDeserializer extends JSR310DeserializerBase<Duration> implements ContextualDeserializer {
    private static final long serialVersionUID = 1;
    public static final DurationDeserializer INSTANCE = new DurationDeserializer();

    @Override // com.fasterxml.jackson.datatype.jsr310.deser.JSR310DeserializerBase, com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer, com.fasterxml.jackson.databind.deser.std.StdDeserializer, com.fasterxml.jackson.databind.JsonDeserializer
    public /* bridge */ /* synthetic */ Object deserializeWithType(JsonParser jsonParser, DeserializationContext deserializationContext, TypeDeserializer typeDeserializer) throws IOException {
        return super.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
    }

    private DurationDeserializer() {
        super(Duration.class);
    }

    protected DurationDeserializer(DurationDeserializer base, Boolean leniency) {
        super(base, leniency);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.fasterxml.jackson.datatype.jsr310.deser.JSR310DeserializerBase
    /* renamed from: withLeniency */
    public JSR310DeserializerBase<Duration> withLeniency2(Boolean leniency) {
        return new DurationDeserializer(this, leniency);
    }

    @Override // com.fasterxml.jackson.databind.JsonDeserializer
    public Duration deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        switch (parser.getCurrentTokenId()) {
            case 3:
                return _deserializeFromArray(parser, context);
            case 4:
            case 5:
            case 9:
            case 10:
            case 11:
            default:
                return (Duration) _handleUnexpectedToken(context, parser, JsonToken.VALUE_STRING, JsonToken.VALUE_NUMBER_INT, JsonToken.VALUE_NUMBER_FLOAT);
            case 6:
                String string = parser.getText().trim();
                if (string.length() == 0) {
                    if (!isLenient()) {
                        return _failForNotLenient(parser, context, JsonToken.VALUE_STRING);
                    }
                    return null;
                }
                try {
                    return Duration.parse(string);
                } catch (DateTimeException e) {
                    return (Duration) _handleDateTimeException(context, e, string);
                }
            case 7:
                if (context.isEnabled(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)) {
                    return Duration.ofSeconds(parser.getLongValue());
                }
                return Duration.ofMillis(parser.getLongValue());
            case 8:
                BigDecimal value = parser.getDecimalValue();
                return (Duration) DecimalUtils.extractSecondsAndNanos(value, (v0, v1) -> {
                    return Duration.ofSeconds(v0, v1);
                });
            case 12:
                return (Duration) parser.getEmbeddedObject();
        }
    }

    @Override // com.fasterxml.jackson.databind.deser.ContextualDeserializer
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        Boolean leniency;
        JsonFormat.Value format = findFormatOverrides(ctxt, property, handledType());
        JSR310DeserializerBase<Duration> deser = this;
        if (format != null && format.hasLenient() && (leniency = format.getLenient()) != null) {
            deser = deser.withLeniency2(leniency);
        }
        return deser;
    }
}
