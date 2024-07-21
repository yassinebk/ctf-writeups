package com.sun.el.stream;

import java.util.NoSuchElementException;
import javax.el.LambdaExpression;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/stream/Optional.class */
public class Optional {
    private static final Optional EMPTY = new Optional();
    private final Object value;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Optional(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        this.value = value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Optional() {
        this.value = null;
    }

    public boolean isPresent() {
        return this.value != null;
    }

    public void ifPresent(LambdaExpression lambda) {
        if (this.value != null) {
            lambda.invoke(this.value);
        }
    }

    public Object get() {
        if (this.value == null) {
            throw new NoSuchElementException("No value present");
        }
        return this.value;
    }

    public Object orElse(Object other) {
        return this.value != null ? this.value : other;
    }

    public Object orElseGet(LambdaExpression other) {
        return this.value != null ? this.value : other.invoke(new Object[0]);
    }
}
