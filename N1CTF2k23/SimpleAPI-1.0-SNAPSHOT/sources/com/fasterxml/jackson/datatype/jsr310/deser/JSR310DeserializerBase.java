package com.fasterxml.jackson.datatype.jsr310.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.IOException;
import java.time.DateTimeException;
import java.util.Arrays;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jackson-datatype-jsr310-2.11.0.jar:com/fasterxml/jackson/datatype/jsr310/deser/JSR310DeserializerBase.class */
abstract class JSR310DeserializerBase<T> extends StdScalarDeserializer<T> {
    private static final long serialVersionUID = 1;
    protected final boolean _isLenient;

    protected abstract JSR310DeserializerBase<T> withLeniency(Boolean bool);

    /* JADX INFO: Access modifiers changed from: protected */
    public JSR310DeserializerBase(Class<T> supportedType) {
        super((Class<?>) supportedType);
        this._isLenient = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JSR310DeserializerBase(Class<T> supportedType, Boolean leniency) {
        super((Class<?>) supportedType);
        this._isLenient = !Boolean.FALSE.equals(leniency);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JSR310DeserializerBase(JSR310DeserializerBase<T> base) {
        super(base);
        this._isLenient = base._isLenient;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JSR310DeserializerBase(JSR310DeserializerBase<T> base, Boolean leniency) {
        super(base);
        this._isLenient = !Boolean.FALSE.equals(leniency);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isLenient() {
        return this._isLenient;
    }

    @Override // com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer, com.fasterxml.jackson.databind.deser.std.StdDeserializer, com.fasterxml.jackson.databind.JsonDeserializer
    public Object deserializeWithType(JsonParser parser, DeserializationContext context, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromAny(parser, context);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <BOGUS> BOGUS _reportWrongToken(DeserializationContext context, JsonToken exp, String unit) throws IOException {
        context.reportWrongTokenException(this, exp, "Expected %s for '%s' of %s value", exp.name(), unit, handledType().getName());
        return null;
    }

    protected <BOGUS> BOGUS _reportWrongToken(JsonParser parser, DeserializationContext context, JsonToken... expTypes) throws IOException {
        return (BOGUS) context.reportInputMismatch(handledType(), "Unexpected token (%s), expected one of %s for %s value", parser.getCurrentToken(), Arrays.asList(expTypes).toString(), handledType().getName());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <R> R _handleDateTimeException(DeserializationContext context, DateTimeException e0, String value) throws JsonMappingException {
        try {
            return (R) context.handleWeirdStringValue(handledType(), value, "Failed to deserialize %s: (%s) %s", handledType().getName(), e0.getClass().getName(), e0.getMessage());
        } catch (JsonMappingException e) {
            e.initCause(e0);
            throw e;
        } catch (IOException e2) {
            if (null == e2.getCause()) {
                e2.initCause(e0);
            }
            throw JsonMappingException.fromUnexpectedIOE(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <R> R _handleUnexpectedToken(DeserializationContext context, JsonParser parser, String message, Object... args) throws JsonMappingException {
        try {
            return (R) context.handleUnexpectedToken(handledType(), parser.getCurrentToken(), parser, message, args);
        } catch (JsonMappingException e) {
            throw e;
        } catch (IOException e2) {
            throw JsonMappingException.fromUnexpectedIOE(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <R> R _handleUnexpectedToken(DeserializationContext context, JsonParser parser, JsonToken... expTypes) throws JsonMappingException {
        return (R) _handleUnexpectedToken(context, parser, "Unexpected token (%s), expected one of %s for %s value", parser.currentToken(), Arrays.asList(expTypes), handledType().getName());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public T _failForNotLenient(JsonParser p, DeserializationContext ctxt, JsonToken expToken) throws IOException {
        return (T) ctxt.handleUnexpectedToken(handledType(), expToken, p, "Cannot deserialize instance of %s out of %s token: not allowed because 'strict' mode set for property or type (enable 'lenient' handling to allow)", ClassUtil.nameOf(handledType()), p.currentToken());
    }

    protected DateTimeException _peelDTE(DateTimeException e) {
        while (true) {
            Throwable t = e.getCause();
            if (t == null || !(t instanceof DateTimeException)) {
                break;
            }
            e = (DateTimeException) t;
        }
        return e;
    }
}
