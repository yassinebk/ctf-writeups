package com.fasterxml.jackson.datatype.jsr310.deser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.util.Locale;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-datatype-jsr310-2.11.0.jar:com/fasterxml/jackson/datatype/jsr310/deser/JSR310DateTimeDeserializerBase.class */
public abstract class JSR310DateTimeDeserializerBase<T> extends JSR310DeserializerBase<T> implements ContextualDeserializer {
    protected final DateTimeFormatter _formatter;
    protected final JsonFormat.Shape _shape;

    protected abstract JSR310DateTimeDeserializerBase<T> withDateFormat(DateTimeFormatter dateTimeFormatter);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.fasterxml.jackson.datatype.jsr310.deser.JSR310DeserializerBase
    public abstract JSR310DateTimeDeserializerBase<T> withLeniency(Boolean bool);

    protected abstract JSR310DateTimeDeserializerBase<T> withShape(JsonFormat.Shape shape);

    @Override // com.fasterxml.jackson.datatype.jsr310.deser.JSR310DeserializerBase, com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer, com.fasterxml.jackson.databind.deser.std.StdDeserializer, com.fasterxml.jackson.databind.JsonDeserializer
    public /* bridge */ /* synthetic */ Object deserializeWithType(JsonParser jsonParser, DeserializationContext deserializationContext, TypeDeserializer typeDeserializer) throws IOException {
        return super.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JSR310DateTimeDeserializerBase(Class<T> supportedType, DateTimeFormatter f) {
        super(supportedType);
        this._formatter = f;
        this._shape = null;
    }

    public JSR310DateTimeDeserializerBase(Class<T> supportedType, DateTimeFormatter f, Boolean leniency) {
        super(supportedType, leniency);
        this._formatter = f;
        this._shape = null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JSR310DateTimeDeserializerBase(JSR310DateTimeDeserializerBase<T> base, DateTimeFormatter f) {
        super(base);
        this._formatter = f;
        this._shape = base._shape;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JSR310DateTimeDeserializerBase(JSR310DateTimeDeserializerBase<T> base, Boolean leniency) {
        super(base, leniency);
        this._formatter = base._formatter;
        this._shape = base._shape;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JSR310DateTimeDeserializerBase(JSR310DateTimeDeserializerBase<T> base, JsonFormat.Shape shape) {
        super(base);
        this._formatter = base._formatter;
        this._shape = shape;
    }

    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        DateTimeFormatter df;
        Boolean leniency;
        JsonFormat.Value format = findFormatOverrides(ctxt, property, handledType());
        JSR310DateTimeDeserializerBase<T> jSR310DateTimeDeserializerBase = this;
        if (format != null) {
            if (format.hasLenient() && (leniency = format.getLenient()) != null) {
                jSR310DateTimeDeserializerBase = jSR310DateTimeDeserializerBase.withLeniency(leniency);
            }
            if (format.hasPattern()) {
                String pattern = format.getPattern();
                Locale locale = format.hasLocale() ? format.getLocale() : ctxt.getLocale();
                DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
                if (acceptCaseInsensitiveValues(ctxt, format)) {
                    builder.parseCaseInsensitive();
                }
                builder.appendPattern(pattern);
                if (locale == null) {
                    df = builder.toFormatter();
                } else {
                    df = builder.toFormatter(locale);
                }
                if (!jSR310DateTimeDeserializerBase.isLenient()) {
                    df = df.withResolverStyle(ResolverStyle.STRICT);
                }
                if (format.hasTimeZone()) {
                    df = df.withZone(format.getTimeZone().toZoneId());
                }
                jSR310DateTimeDeserializerBase = jSR310DateTimeDeserializerBase.withDateFormat(df);
            }
            JsonFormat.Shape shape = format.getShape();
            if (shape != null && shape != this._shape) {
                jSR310DateTimeDeserializerBase = jSR310DateTimeDeserializerBase.withShape(shape);
            }
        }
        return jSR310DateTimeDeserializerBase;
    }

    private boolean acceptCaseInsensitiveValues(DeserializationContext ctxt, JsonFormat.Value format) {
        Boolean enabled = format.getFeature(JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_VALUES);
        if (enabled == null) {
            enabled = Boolean.valueOf(ctxt.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_VALUES));
        }
        return enabled.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void _throwNoNumericTimestampNeedTimeZone(JsonParser p, DeserializationContext ctxt) throws IOException {
        ctxt.reportInputMismatch(handledType(), "raw timestamp (%d) not allowed for `%s`: need additional information such as an offset or time-zone (see class Javadocs)", p.getNumberValue(), handledType().getName());
    }
}
