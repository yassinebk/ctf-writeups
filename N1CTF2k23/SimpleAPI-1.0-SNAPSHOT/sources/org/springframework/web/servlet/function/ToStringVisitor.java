package org.springframework.web.servlet.function;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunctions;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/ToStringVisitor.class */
class ToStringVisitor implements RouterFunctions.Visitor, RequestPredicates.Visitor {
    private final StringBuilder builder = new StringBuilder();
    private int indent = 0;

    @Override // org.springframework.web.servlet.function.RouterFunctions.Visitor
    public void startNested(RequestPredicate predicate) {
        indent();
        predicate.accept(this);
        this.builder.append(" => {\n");
        this.indent++;
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Visitor
    public void endNested(RequestPredicate predicate) {
        this.indent--;
        indent();
        this.builder.append("}\n");
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Visitor
    public void route(RequestPredicate predicate, HandlerFunction<?> handlerFunction) {
        indent();
        predicate.accept(this);
        this.builder.append(" -> ");
        this.builder.append(handlerFunction).append('\n');
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Visitor
    public void resources(Function<ServerRequest, Optional<Resource>> lookupFunction) {
        indent();
        this.builder.append(lookupFunction).append('\n');
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Visitor
    public void unknown(RouterFunction<?> routerFunction) {
        indent();
        this.builder.append(routerFunction);
    }

    private void indent() {
        for (int i = 0; i < this.indent; i++) {
            this.builder.append(' ');
        }
    }

    @Override // org.springframework.web.servlet.function.RequestPredicates.Visitor
    public void method(Set<HttpMethod> methods) {
        if (methods.size() == 1) {
            this.builder.append(methods.iterator().next());
        } else {
            this.builder.append(methods);
        }
    }

    @Override // org.springframework.web.servlet.function.RequestPredicates.Visitor
    public void path(String pattern) {
        this.builder.append(pattern);
    }

    @Override // org.springframework.web.servlet.function.RequestPredicates.Visitor
    public void pathExtension(String extension) {
        this.builder.append(String.format("*.%s", extension));
    }

    @Override // org.springframework.web.servlet.function.RequestPredicates.Visitor
    public void header(String name, String value) {
        this.builder.append(String.format("%s: %s", name, value));
    }

    @Override // org.springframework.web.servlet.function.RequestPredicates.Visitor
    public void param(String name, String value) {
        this.builder.append(String.format("?%s == %s", name, value));
    }

    @Override // org.springframework.web.servlet.function.RequestPredicates.Visitor
    public void startAnd() {
        this.builder.append('(');
    }

    @Override // org.springframework.web.servlet.function.RequestPredicates.Visitor
    public void and() {
        this.builder.append(" && ");
    }

    @Override // org.springframework.web.servlet.function.RequestPredicates.Visitor
    public void endAnd() {
        this.builder.append(')');
    }

    @Override // org.springframework.web.servlet.function.RequestPredicates.Visitor
    public void startOr() {
        this.builder.append('(');
    }

    @Override // org.springframework.web.servlet.function.RequestPredicates.Visitor
    public void or() {
        this.builder.append(" || ");
    }

    @Override // org.springframework.web.servlet.function.RequestPredicates.Visitor
    public void endOr() {
        this.builder.append(')');
    }

    @Override // org.springframework.web.servlet.function.RequestPredicates.Visitor
    public void startNegate() {
        this.builder.append("!(");
    }

    @Override // org.springframework.web.servlet.function.RequestPredicates.Visitor
    public void endNegate() {
        this.builder.append(')');
    }

    @Override // org.springframework.web.servlet.function.RequestPredicates.Visitor
    public void unknown(RequestPredicate predicate) {
        this.builder.append(predicate);
    }

    public String toString() {
        String result = this.builder.toString();
        if (result.endsWith("\n")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
