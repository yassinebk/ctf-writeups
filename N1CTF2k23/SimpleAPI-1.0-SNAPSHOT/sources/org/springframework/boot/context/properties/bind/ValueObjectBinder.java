package org.springframework.boot.context.properties.bind;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import kotlin.reflect.KFunction;
import kotlin.reflect.KParameter;
import kotlin.reflect.jvm.ReflectJvmMapping;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.KotlinDetector;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.ResolvableType;
import org.springframework.core.convert.ConversionException;
import org.springframework.util.Assert;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/bind/ValueObjectBinder.class */
public class ValueObjectBinder implements DataObjectBinder {
    private final BindConstructorProvider constructorProvider;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ValueObjectBinder(BindConstructorProvider constructorProvider) {
        this.constructorProvider = constructorProvider;
    }

    @Override // org.springframework.boot.context.properties.bind.DataObjectBinder
    public <T> T bind(ConfigurationPropertyName name, Bindable<T> target, Binder.Context context, DataObjectPropertyBinder propertyBinder) {
        ValueObject<T> valueObject = ValueObject.get(target, this.constructorProvider, context);
        if (valueObject == null) {
            return null;
        }
        context.pushConstructorBoundTypes(target.getType().resolve());
        List<ConstructorParameter> parameters = valueObject.getConstructorParameters();
        List<Object> args = new ArrayList<>(parameters.size());
        boolean bound = false;
        for (ConstructorParameter parameter : parameters) {
            Object arg = parameter.bind(propertyBinder);
            bound = bound || arg != null;
            args.add(arg != null ? arg : getDefaultValue(context, parameter));
        }
        context.clearConfigurationProperty();
        context.popConstructorBoundTypes();
        if (bound) {
            return valueObject.instantiate(args);
        }
        return null;
    }

    @Override // org.springframework.boot.context.properties.bind.DataObjectBinder
    public <T> T create(Bindable<T> target, Binder.Context context) {
        ValueObject<T> valueObject = ValueObject.get(target, this.constructorProvider, context);
        if (valueObject == null) {
            return null;
        }
        List<ConstructorParameter> parameters = valueObject.getConstructorParameters();
        List<Object> args = new ArrayList<>(parameters.size());
        for (ConstructorParameter parameter : parameters) {
            args.add(getDefaultValue(context, parameter));
        }
        return valueObject.instantiate(args);
    }

    private <T> T getDefaultValue(Binder.Context context, ConstructorParameter parameter) {
        ResolvableType type = parameter.getType();
        Annotation[] annotations = parameter.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof DefaultValue) {
                String[] defaultValue = ((DefaultValue) annotation).value();
                if (defaultValue.length == 0) {
                    return (T) getNewInstanceIfPossible(context, type);
                }
                return (T) convertDefaultValue(context.getConverter(), defaultValue, type, annotations);
            }
        }
        return null;
    }

    private <T> T convertDefaultValue(BindConverter converter, String[] defaultValue, ResolvableType type, Annotation[] annotations) {
        try {
            return (T) converter.convert(defaultValue, type, annotations);
        } catch (ConversionException ex) {
            if (defaultValue.length == 1) {
                return (T) converter.convert(defaultValue[0], type, annotations);
            }
            throw ex;
        }
    }

    private <T> T getNewInstanceIfPossible(Binder.Context context, ResolvableType type) {
        Class<?> resolve = type.resolve();
        Assert.state(resolve == null || isEmptyDefaultValueAllowed(resolve), () -> {
            return "Parameter of type " + type + " must have a non-empty default value.";
        });
        T instance = (T) create(Bindable.of(type), context);
        if (instance != null) {
            return instance;
        }
        if (resolve != null) {
            return (T) BeanUtils.instantiateClass(resolve);
        }
        return null;
    }

    private boolean isEmptyDefaultValueAllowed(Class<?> type) {
        if (type.isPrimitive() || type.isEnum() || isAggregate(type) || type.getName().startsWith("java.lang")) {
            return false;
        }
        return true;
    }

    private boolean isAggregate(Class<?> type) {
        return type.isArray() || Map.class.isAssignableFrom(type) || Collection.class.isAssignableFrom(type);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/bind/ValueObjectBinder$ValueObject.class */
    public static abstract class ValueObject<T> {
        private final Constructor<T> constructor;

        abstract List<ConstructorParameter> getConstructorParameters();

        protected ValueObject(Constructor<T> constructor) {
            this.constructor = constructor;
        }

        T instantiate(List<Object> args) {
            return (T) BeanUtils.instantiateClass(this.constructor, args.toArray());
        }

        static <T> ValueObject<T> get(Bindable<T> bindable, BindConstructorProvider constructorProvider, Binder.Context context) {
            Constructor<?> bindConstructor;
            Class<?> resolve = bindable.getType().resolve();
            if (resolve == null || resolve.isEnum() || Modifier.isAbstract(resolve.getModifiers()) || (bindConstructor = constructorProvider.getBindConstructor(bindable, context.isNestedConstructorBinding())) == null) {
                return null;
            }
            if (KotlinDetector.isKotlinType(resolve)) {
                return KotlinValueObject.get(bindConstructor, bindable.getType());
            }
            return DefaultValueObject.get(bindConstructor, bindable.getType());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/bind/ValueObjectBinder$KotlinValueObject.class */
    public static final class KotlinValueObject<T> extends ValueObject<T> {
        private final List<ConstructorParameter> constructorParameters;

        private KotlinValueObject(Constructor<T> primaryConstructor, KFunction<T> kotlinConstructor, ResolvableType type) {
            super(primaryConstructor);
            this.constructorParameters = parseConstructorParameters(kotlinConstructor, type);
        }

        private List<ConstructorParameter> parseConstructorParameters(KFunction<T> kotlinConstructor, ResolvableType type) {
            List<KParameter> parameters = kotlinConstructor.getParameters();
            List<ConstructorParameter> result = new ArrayList<>(parameters.size());
            for (KParameter parameter : parameters) {
                String name = parameter.getName();
                ResolvableType parameterType = ResolvableType.forType(ReflectJvmMapping.getJavaType(parameter.getType()), type);
                Annotation[] annotations = (Annotation[]) parameter.getAnnotations().toArray(new Annotation[0]);
                result.add(new ConstructorParameter(name, parameterType, annotations));
            }
            return Collections.unmodifiableList(result);
        }

        @Override // org.springframework.boot.context.properties.bind.ValueObjectBinder.ValueObject
        List<ConstructorParameter> getConstructorParameters() {
            return this.constructorParameters;
        }

        static <T> ValueObject<T> get(Constructor<T> bindConstructor, ResolvableType type) {
            KFunction<T> kotlinConstructor = ReflectJvmMapping.getKotlinFunction(bindConstructor);
            if (kotlinConstructor != null) {
                return new KotlinValueObject(bindConstructor, kotlinConstructor, type);
            }
            return DefaultValueObject.get(bindConstructor, type);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/bind/ValueObjectBinder$DefaultValueObject.class */
    public static final class DefaultValueObject<T> extends ValueObject<T> {
        private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
        private final List<ConstructorParameter> constructorParameters;

        private DefaultValueObject(Constructor<T> constructor, ResolvableType type) {
            super(constructor);
            this.constructorParameters = parseConstructorParameters(constructor, type);
        }

        private static List<ConstructorParameter> parseConstructorParameters(Constructor<?> constructor, ResolvableType type) {
            String[] names = PARAMETER_NAME_DISCOVERER.getParameterNames(constructor);
            Assert.state(names != null, () -> {
                return "Failed to extract parameter names for " + constructor;
            });
            Parameter[] parameters = constructor.getParameters();
            List<ConstructorParameter> result = new ArrayList<>(parameters.length);
            for (int i = 0; i < parameters.length; i++) {
                String name = names[i];
                ResolvableType parameterType = ResolvableType.forMethodParameter(new MethodParameter(constructor, i), type);
                Annotation[] annotations = parameters[i].getDeclaredAnnotations();
                result.add(new ConstructorParameter(name, parameterType, annotations));
            }
            return Collections.unmodifiableList(result);
        }

        @Override // org.springframework.boot.context.properties.bind.ValueObjectBinder.ValueObject
        List<ConstructorParameter> getConstructorParameters() {
            return this.constructorParameters;
        }

        static <T> ValueObject<T> get(Constructor<?> bindConstructor, ResolvableType type) {
            return new DefaultValueObject(bindConstructor, type);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/bind/ValueObjectBinder$ConstructorParameter.class */
    public static class ConstructorParameter {
        private final String name;
        private final ResolvableType type;
        private final Annotation[] annotations;

        ConstructorParameter(String name, ResolvableType type, Annotation[] annotations) {
            this.name = DataObjectPropertyName.toDashedForm(name);
            this.type = type;
            this.annotations = annotations;
        }

        Object bind(DataObjectPropertyBinder propertyBinder) {
            return propertyBinder.bindProperty(this.name, Bindable.of(this.type).withAnnotations(this.annotations));
        }

        Annotation[] getAnnotations() {
            return this.annotations;
        }

        ResolvableType getType() {
            return this.type;
        }
    }
}
