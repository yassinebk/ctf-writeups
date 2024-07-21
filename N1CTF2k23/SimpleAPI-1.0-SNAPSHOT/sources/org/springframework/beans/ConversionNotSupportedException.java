package org.springframework.beans;

import java.beans.PropertyChangeEvent;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/ConversionNotSupportedException.class */
public class ConversionNotSupportedException extends TypeMismatchException {
    public ConversionNotSupportedException(PropertyChangeEvent propertyChangeEvent, @Nullable Class<?> requiredType, @Nullable Throwable cause) {
        super(propertyChangeEvent, requiredType, cause);
    }

    public ConversionNotSupportedException(@Nullable Object value, @Nullable Class<?> requiredType, @Nullable Throwable cause) {
        super(value, requiredType, cause);
    }
}
