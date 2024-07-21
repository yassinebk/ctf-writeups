package com.google.gson.internal.bind;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.C$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.ReflectionAccessFilterHelper;
import com.google.gson.internal.reflect.ReflectionHelper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/internal/bind/ReflectiveTypeAdapterFactory.class */
public final class ReflectiveTypeAdapterFactory implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    private final FieldNamingStrategy fieldNamingPolicy;
    private final Excluder excluder;
    private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
    private final List<ReflectionAccessFilter> reflectionFilters;

    public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor, FieldNamingStrategy fieldNamingPolicy, Excluder excluder, JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory, List<ReflectionAccessFilter> reflectionFilters) {
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingPolicy = fieldNamingPolicy;
        this.excluder = excluder;
        this.jsonAdapterFactory = jsonAdapterFactory;
        this.reflectionFilters = reflectionFilters;
    }

    private boolean includeField(Field f, boolean serialize) {
        return (this.excluder.excludeClass(f.getType(), serialize) || this.excluder.excludeField(f, serialize)) ? false : true;
    }

    private List<String> getFieldNames(Field f) {
        SerializedName annotation = (SerializedName) f.getAnnotation(SerializedName.class);
        if (annotation == null) {
            String name = this.fieldNamingPolicy.translateName(f);
            return Collections.singletonList(name);
        }
        String serializedName = annotation.value();
        String[] alternates = annotation.alternate();
        if (alternates.length == 0) {
            return Collections.singletonList(serializedName);
        }
        List<String> fieldNames = new ArrayList<>(alternates.length + 1);
        fieldNames.add(serializedName);
        Collections.addAll(fieldNames, alternates);
        return fieldNames;
    }

    @Override // com.google.gson.TypeAdapterFactory
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> raw = type.getRawType();
        if (!Object.class.isAssignableFrom(raw)) {
            return null;
        }
        ReflectionAccessFilter.FilterResult filterResult = ReflectionAccessFilterHelper.getFilterResult(this.reflectionFilters, raw);
        if (filterResult == ReflectionAccessFilter.FilterResult.BLOCK_ALL) {
            throw new JsonIOException("ReflectionAccessFilter does not permit using reflection for " + raw + ". Register a TypeAdapter for this type or adjust the access filter.");
        }
        boolean blockInaccessible = filterResult == ReflectionAccessFilter.FilterResult.BLOCK_INACCESSIBLE;
        if (ReflectionHelper.isRecord(raw)) {
            TypeAdapter<T> adapter = new RecordAdapter<>(raw, getBoundFields(gson, type, raw, blockInaccessible, true), blockInaccessible);
            return adapter;
        }
        ObjectConstructor<T> constructor = this.constructorConstructor.get(type);
        return new FieldReflectionAdapter(constructor, getBoundFields(gson, type, raw, blockInaccessible, false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <M extends AccessibleObject & Member> void checkAccessible(Object object, M member) {
        if (!ReflectionAccessFilterHelper.canAccess(member, Modifier.isStatic(member.getModifiers()) ? null : object)) {
            String memberDescription = ReflectionHelper.getAccessibleObjectDescription(member, true);
            throw new JsonIOException(memberDescription + " is not accessible and ReflectionAccessFilter does not permit making it accessible. Register a TypeAdapter for the declaring type, adjust the access filter or increase the visibility of the element and its declaring type.");
        }
    }

    private BoundField createBoundField(final Gson context, Field field, final Method accessor, String name, final TypeToken<?> fieldType, boolean serialize, boolean deserialize, final boolean blockInaccessible) {
        final boolean isPrimitive = Primitives.isPrimitive(fieldType.getRawType());
        int modifiers = field.getModifiers();
        final boolean isStaticFinalField = Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers);
        JsonAdapter annotation = (JsonAdapter) field.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> mapped = null;
        if (annotation != null) {
            mapped = this.jsonAdapterFactory.getTypeAdapter(this.constructorConstructor, context, fieldType, annotation);
        }
        final boolean jsonAdapterPresent = mapped != null;
        if (mapped == null) {
            mapped = context.getAdapter(fieldType);
        }
        final TypeAdapter<?> typeAdapter = mapped;
        return new BoundField(name, field, serialize, deserialize) { // from class: com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.1
            @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField
            void write(JsonWriter writer, Object source) throws IOException, IllegalAccessException {
                Object fieldValue;
                if (this.serialized) {
                    if (blockInaccessible) {
                        if (accessor == null) {
                            ReflectiveTypeAdapterFactory.checkAccessible(source, this.field);
                        } else {
                            ReflectiveTypeAdapterFactory.checkAccessible(source, accessor);
                        }
                    }
                    if (accessor != null) {
                        try {
                            fieldValue = accessor.invoke(source, new Object[0]);
                        } catch (InvocationTargetException e) {
                            String accessorDescription = ReflectionHelper.getAccessibleObjectDescription(accessor, false);
                            throw new JsonIOException("Accessor " + accessorDescription + " threw exception", e.getCause());
                        }
                    } else {
                        fieldValue = this.field.get(source);
                    }
                    if (fieldValue == source) {
                        return;
                    }
                    writer.name(this.name);
                    TypeAdapter<Object> t = jsonAdapterPresent ? typeAdapter : new TypeAdapterRuntimeTypeWrapper<>(context, typeAdapter, fieldType.getType());
                    t.write(writer, fieldValue);
                }
            }

            @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField
            void readIntoArray(JsonReader reader, int index, Object[] target) throws IOException, JsonParseException {
                Object fieldValue = typeAdapter.read(reader);
                if (fieldValue == null && isPrimitive) {
                    throw new JsonParseException("null is not allowed as value for record component '" + this.fieldName + "' of primitive type; at path " + reader.getPath());
                }
                target[index] = fieldValue;
            }

            @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField
            void readIntoField(JsonReader reader, Object target) throws IOException, IllegalAccessException {
                Object fieldValue = typeAdapter.read(reader);
                if (fieldValue != null || !isPrimitive) {
                    if (blockInaccessible) {
                        ReflectiveTypeAdapterFactory.checkAccessible(target, this.field);
                    } else if (isStaticFinalField) {
                        String fieldDescription = ReflectionHelper.getAccessibleObjectDescription(this.field, false);
                        throw new JsonIOException("Cannot set value of 'static final' " + fieldDescription);
                    }
                    this.field.set(target, fieldValue);
                }
            }
        };
    }

    private Map<String, BoundField> getBoundFields(Gson context, TypeToken<?> type, Class<?> raw, boolean blockInaccessible, boolean isRecord) {
        Map<String, BoundField> result = new LinkedHashMap<>();
        if (raw.isInterface()) {
            return result;
        }
        while (raw != Object.class) {
            Field[] fields = raw.getDeclaredFields();
            if (raw != raw && fields.length > 0) {
                ReflectionAccessFilter.FilterResult filterResult = ReflectionAccessFilterHelper.getFilterResult(this.reflectionFilters, raw);
                if (filterResult == ReflectionAccessFilter.FilterResult.BLOCK_ALL) {
                    throw new JsonIOException("ReflectionAccessFilter does not permit using reflection for " + raw + " (supertype of " + raw + "). Register a TypeAdapter for this type or adjust the access filter.");
                }
                blockInaccessible = filterResult == ReflectionAccessFilter.FilterResult.BLOCK_INACCESSIBLE;
            }
            for (Field field : fields) {
                boolean serialize = includeField(field, true);
                boolean deserialize = includeField(field, false);
                if (serialize || deserialize) {
                    Method accessor = null;
                    if (isRecord) {
                        if (Modifier.isStatic(field.getModifiers())) {
                            deserialize = false;
                        } else {
                            accessor = ReflectionHelper.getAccessor(raw, field);
                            if (!blockInaccessible) {
                                ReflectionHelper.makeAccessible(accessor);
                            }
                            if (accessor.getAnnotation(SerializedName.class) != null && field.getAnnotation(SerializedName.class) == null) {
                                String methodDescription = ReflectionHelper.getAccessibleObjectDescription(accessor, false);
                                throw new JsonIOException("@SerializedName on " + methodDescription + " is not supported");
                            }
                        }
                    }
                    if (!blockInaccessible && accessor == null) {
                        ReflectionHelper.makeAccessible(field);
                    }
                    Type fieldType = C$Gson$Types.resolve(type.getType(), raw, field.getGenericType());
                    List<String> fieldNames = getFieldNames(field);
                    BoundField previous = null;
                    int size = fieldNames.size();
                    for (int i = 0; i < size; i++) {
                        String name = fieldNames.get(i);
                        if (i != 0) {
                            serialize = false;
                        }
                        BoundField boundField = createBoundField(context, field, accessor, name, TypeToken.get(fieldType), serialize, deserialize, blockInaccessible);
                        BoundField replaced = result.put(name, boundField);
                        if (previous == null) {
                            previous = replaced;
                        }
                    }
                    if (previous != null) {
                        throw new IllegalArgumentException("Class " + raw.getName() + " declares multiple JSON fields named '" + previous.name + "'; conflict is caused by fields " + ReflectionHelper.fieldToString(previous.field) + " and " + ReflectionHelper.fieldToString(field));
                    }
                }
            }
            type = TypeToken.get(C$Gson$Types.resolve(type.getType(), raw, raw.getGenericSuperclass()));
            raw = type.getRawType();
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/internal/bind/ReflectiveTypeAdapterFactory$BoundField.class */
    public static abstract class BoundField {
        final String name;
        final Field field;
        final String fieldName;
        final boolean serialized;
        final boolean deserialized;

        abstract void write(JsonWriter jsonWriter, Object obj) throws IOException, IllegalAccessException;

        abstract void readIntoArray(JsonReader jsonReader, int i, Object[] objArr) throws IOException, JsonParseException;

        abstract void readIntoField(JsonReader jsonReader, Object obj) throws IOException, IllegalAccessException;

        protected BoundField(String name, Field field, boolean serialized, boolean deserialized) {
            this.name = name;
            this.field = field;
            this.fieldName = field.getName();
            this.serialized = serialized;
            this.deserialized = deserialized;
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/internal/bind/ReflectiveTypeAdapterFactory$Adapter.class */
    public static abstract class Adapter<T, A> extends TypeAdapter<T> {
        final Map<String, BoundField> boundFields;

        abstract A createAccumulator();

        abstract void readField(A a, JsonReader jsonReader, BoundField boundField) throws IllegalAccessException, IOException;

        abstract T finalize(A a);

        Adapter(Map<String, BoundField> boundFields) {
            this.boundFields = boundFields;
        }

        @Override // com.google.gson.TypeAdapter
        public void write(JsonWriter out, T value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            try {
                for (BoundField boundField : this.boundFields.values()) {
                    boundField.write(out, value);
                }
                out.endObject();
            } catch (IllegalAccessException e) {
                throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e);
            }
        }

        @Override // com.google.gson.TypeAdapter
        public T read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            A accumulator = createAccumulator();
            try {
                in.beginObject();
                while (in.hasNext()) {
                    String name = in.nextName();
                    BoundField field = this.boundFields.get(name);
                    if (field == null || !field.deserialized) {
                        in.skipValue();
                    } else {
                        readField(accumulator, in, field);
                    }
                }
                in.endObject();
                return finalize(accumulator);
            } catch (IllegalAccessException e) {
                throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e);
            } catch (IllegalStateException e2) {
                throw new JsonSyntaxException(e2);
            }
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/internal/bind/ReflectiveTypeAdapterFactory$FieldReflectionAdapter.class */
    private static final class FieldReflectionAdapter<T> extends Adapter<T, T> {
        private final ObjectConstructor<T> constructor;

        FieldReflectionAdapter(ObjectConstructor<T> constructor, Map<String, BoundField> boundFields) {
            super(boundFields);
            this.constructor = constructor;
        }

        @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter
        T createAccumulator() {
            return this.constructor.construct();
        }

        @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter
        void readField(T accumulator, JsonReader in, BoundField field) throws IllegalAccessException, IOException {
            field.readIntoField(in, accumulator);
        }

        @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter
        T finalize(T accumulator) {
            return accumulator;
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/internal/bind/ReflectiveTypeAdapterFactory$RecordAdapter.class */
    private static final class RecordAdapter<T> extends Adapter<T, Object[]> {
        static final Map<Class<?>, Object> PRIMITIVE_DEFAULTS = primitiveDefaults();
        private final Constructor<T> constructor;
        private final Object[] constructorArgsDefaults;
        private final Map<String, Integer> componentIndices;

        RecordAdapter(Class<T> raw, Map<String, BoundField> boundFields, boolean blockInaccessible) {
            super(boundFields);
            this.componentIndices = new HashMap();
            this.constructor = ReflectionHelper.getCanonicalRecordConstructor(raw);
            if (blockInaccessible) {
                ReflectiveTypeAdapterFactory.checkAccessible(null, this.constructor);
            } else {
                ReflectionHelper.makeAccessible(this.constructor);
            }
            String[] componentNames = ReflectionHelper.getRecordComponentNames(raw);
            for (int i = 0; i < componentNames.length; i++) {
                this.componentIndices.put(componentNames[i], Integer.valueOf(i));
            }
            Class<?>[] parameterTypes = this.constructor.getParameterTypes();
            this.constructorArgsDefaults = new Object[parameterTypes.length];
            for (int i2 = 0; i2 < parameterTypes.length; i2++) {
                this.constructorArgsDefaults[i2] = PRIMITIVE_DEFAULTS.get(parameterTypes[i2]);
            }
        }

        private static Map<Class<?>, Object> primitiveDefaults() {
            Map<Class<?>, Object> zeroes = new HashMap<>();
            zeroes.put(Byte.TYPE, (byte) 0);
            zeroes.put(Short.TYPE, (short) 0);
            zeroes.put(Integer.TYPE, 0);
            zeroes.put(Long.TYPE, 0L);
            zeroes.put(Float.TYPE, Float.valueOf(0.0f));
            zeroes.put(Double.TYPE, Double.valueOf(0.0d));
            zeroes.put(Character.TYPE, (char) 0);
            zeroes.put(Boolean.TYPE, false);
            return zeroes;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter
        public Object[] createAccumulator() {
            return (Object[]) this.constructorArgsDefaults.clone();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter
        public void readField(Object[] accumulator, JsonReader in, BoundField field) throws IOException {
            Integer componentIndex = this.componentIndices.get(field.fieldName);
            if (componentIndex == null) {
                throw new IllegalStateException("Could not find the index in the constructor '" + ReflectionHelper.constructorToString(this.constructor) + "' for field with name '" + field.fieldName + "', unable to determine which argument in the constructor the field corresponds to. This is unexpected behavior, as we expect the RecordComponents to have the same names as the fields in the Java class, and that the order of the RecordComponents is the same as the order of the canonical constructor parameters.");
            }
            field.readIntoArray(in, componentIndex.intValue(), accumulator);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter
        public T finalize(Object[] accumulator) {
            try {
                return this.constructor.newInstance(accumulator);
            } catch (IllegalAccessException e) {
                throw ReflectionHelper.createExceptionForUnexpectedIllegalAccess(e);
            } catch (IllegalArgumentException | InstantiationException e2) {
                throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(this.constructor) + "' with args " + Arrays.toString(accumulator), e2);
            } catch (InvocationTargetException e3) {
                throw new RuntimeException("Failed to invoke constructor '" + ReflectionHelper.constructorToString(this.constructor) + "' with args " + Arrays.toString(accumulator), e3.getCause());
            }
        }
    }
}
