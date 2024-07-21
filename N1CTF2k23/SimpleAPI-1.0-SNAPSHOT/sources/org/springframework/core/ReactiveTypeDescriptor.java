package org.springframework.core;

import java.util.function.Supplier;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/ReactiveTypeDescriptor.class */
public final class ReactiveTypeDescriptor {
    private final Class<?> reactiveType;
    private final boolean multiValue;
    private final boolean noValue;
    @Nullable
    private final Supplier<?> emptyValueSupplier;

    private ReactiveTypeDescriptor(Class<?> reactiveType, boolean multiValue, boolean noValue, @Nullable Supplier<?> emptySupplier) {
        Assert.notNull(reactiveType, "'reactiveType' must not be null");
        this.reactiveType = reactiveType;
        this.multiValue = multiValue;
        this.noValue = noValue;
        this.emptyValueSupplier = emptySupplier;
    }

    public Class<?> getReactiveType() {
        return this.reactiveType;
    }

    public boolean isMultiValue() {
        return this.multiValue;
    }

    public boolean isNoValue() {
        return this.noValue;
    }

    public boolean supportsEmpty() {
        return this.emptyValueSupplier != null;
    }

    public Object getEmptyValue() {
        Assert.state(this.emptyValueSupplier != null, "Empty values not supported");
        return this.emptyValueSupplier.get();
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        return this.reactiveType.equals(((ReactiveTypeDescriptor) other).reactiveType);
    }

    public int hashCode() {
        return this.reactiveType.hashCode();
    }

    public static ReactiveTypeDescriptor multiValue(Class<?> type, Supplier<?> emptySupplier) {
        return new ReactiveTypeDescriptor(type, true, false, emptySupplier);
    }

    public static ReactiveTypeDescriptor singleOptionalValue(Class<?> type, Supplier<?> emptySupplier) {
        return new ReactiveTypeDescriptor(type, false, false, emptySupplier);
    }

    public static ReactiveTypeDescriptor singleRequiredValue(Class<?> type) {
        return new ReactiveTypeDescriptor(type, false, false, null);
    }

    public static ReactiveTypeDescriptor noValue(Class<?> type, Supplier<?> emptySupplier) {
        return new ReactiveTypeDescriptor(type, false, true, emptySupplier);
    }
}
