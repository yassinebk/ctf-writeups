package org.springframework.expression;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-expression-5.2.6.RELEASE.jar:org/springframework/expression/ExpressionParser.class */
public interface ExpressionParser {
    Expression parseExpression(String str) throws ParseException;

    Expression parseExpression(String str, ParserContext parserContext) throws ParseException;
}
