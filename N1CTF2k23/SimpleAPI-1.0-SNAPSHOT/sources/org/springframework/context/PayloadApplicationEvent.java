package org.springframework.context;

import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/context/PayloadApplicationEvent.class */
public class PayloadApplicationEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {
    private final T payload;

    public PayloadApplicationEvent(Object source, T payload) {
        super(source);
        Assert.notNull(payload, "Payload must not be null");
        this.payload = payload;
    }

    @Override // org.springframework.core.ResolvableTypeProvider
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getPayload()));
    }

    public T getPayload() {
        return this.payload;
    }
}
