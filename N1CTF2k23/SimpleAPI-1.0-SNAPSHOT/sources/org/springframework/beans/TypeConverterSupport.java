package org.springframework.beans;

import java.lang.reflect.Field;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-beans-5.2.6.RELEASE.jar:org/springframework/beans/TypeConverterSupport.class */
public abstract class TypeConverterSupport extends PropertyEditorRegistrySupport implements TypeConverter {
    @Nullable
    TypeConverterDelegate typeConverterDelegate;

    @Override // org.springframework.beans.TypeConverter
    @Nullable
    public <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType) throws TypeMismatchException {
        return (T) convertIfNecessary(value, requiredType, TypeDescriptor.valueOf(requiredType));
    }

    @Override // org.springframework.beans.TypeConverter
    @Nullable
    public <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType, @Nullable MethodParameter methodParam) throws TypeMismatchException {
        return (T) convertIfNecessary(value, requiredType, methodParam != null ? new TypeDescriptor(methodParam) : TypeDescriptor.valueOf(requiredType));
    }

    @Override // org.springframework.beans.TypeConverter
    @Nullable
    public <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType, @Nullable Field field) throws TypeMismatchException {
        return (T) convertIfNecessary(value, requiredType, field != null ? new TypeDescriptor(field) : TypeDescriptor.valueOf(requiredType));
    }

    @Override // org.springframework.beans.TypeConverter
    @Nullable
    public <T> T convertIfNecessary(@Nullable Object value, @Nullable Class<T> requiredType, @Nullable TypeDescriptor typeDescriptor) throws TypeMismatchException {
        Assert.state(this.typeConverterDelegate != null, "No TypeConverterDelegate");
        try {
            return (T) this.typeConverterDelegate.convertIfNecessary(null, null, value, requiredType, typeDescriptor);
        } catch (IllegalArgumentException | ConversionException ex) {
            throw new TypeMismatchException(value, (Class<?>) requiredType, (Throwable) ex);
        } catch (IllegalStateException | ConverterNotFoundException ex2) {
            throw new ConversionNotSupportedException(value, (Class<?>) requiredType, (Throwable) ex2);
        }
    }
}
