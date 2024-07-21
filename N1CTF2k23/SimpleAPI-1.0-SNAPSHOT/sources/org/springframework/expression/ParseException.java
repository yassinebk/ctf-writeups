package org.springframework.expression;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/ParseException.class */
public class ParseException extends ExpressionException {
    public ParseException(@Nullable String expressionString, int position, String message) {
        super(expressionString, position, message);
    }

    public ParseException(int position, String message, Throwable cause) {
        super(position, message, cause);
    }

    public ParseException(int position, String message) {
        super(position, message);
    }
}
