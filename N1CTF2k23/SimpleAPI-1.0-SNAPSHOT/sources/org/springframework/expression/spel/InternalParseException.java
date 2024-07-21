package org.springframework.expression.spel;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/spel/InternalParseException.class */
public class InternalParseException extends RuntimeException {
    public InternalParseException(SpelParseException cause) {
        super(cause);
    }

    @Override // java.lang.Throwable
    public SpelParseException getCause() {
        return (SpelParseException) super.getCause();
    }
}
