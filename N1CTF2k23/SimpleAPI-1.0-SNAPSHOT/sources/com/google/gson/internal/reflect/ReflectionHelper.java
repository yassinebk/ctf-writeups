package com.google.gson.internal.reflect;

import com.google.gson.JsonIOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/internal/reflect/ReflectionHelper.class */
public class ReflectionHelper {
    private static final RecordHelper RECORD_HELPER;

    static {
        RecordHelper instance;
        try {
            instance = new RecordSupportedHelper();
        } catch (NoSuchMethodException e) {
            instance = new RecordNotSupportedHelper();
        }
        RECORD_HELPER = instance;
    }

    private ReflectionHelper() {
    }

    public static void makeAccessible(AccessibleObject object) throws JsonIOException {
        try {
            object.setAccessible(true);
        } catch (Exception exception) {
            String description = getAccessibleObjectDescription(object, false);
            throw new JsonIOException("Failed making " + description + " accessible; either increase its visibility or write a custom TypeAdapter for its declaring type.", exception);
        }
    }

    public static String getAccessibleObjectDescription(AccessibleObject object, boolean uppercaseFirstLetter) {
        String description;
        if (object instanceof Field) {
            description = "field '" + fieldToString((Field) object) + "'";
        } else if (object instanceof Method) {
            Method method = (Method) object;
            StringBuilder methodSignatureBuilder = new StringBuilder(method.getName());
            appendExecutableParameters(method, methodSignatureBuilder);
            String methodSignature = methodSignatureBuilder.toString();
            description = "method '" + method.getDeclaringClass().getName() + "#" + methodSignature + "'";
        } else if (object instanceof Constructor) {
            description = "constructor '" + constructorToString((Constructor) object) + "'";
        } else {
            description = "<unknown AccessibleObject> " + object.toString();
        }
        if (uppercaseFirstLetter && Character.isLowerCase(description.charAt(0))) {
            description = Character.toUpperCase(description.charAt(0)) + description.substring(1);
        }
        return description;
    }

    public static String fieldToString(Field field) {
        return field.getDeclaringClass().getName() + "#" + field.getName();
    }

    public static String constructorToString(Constructor<?> constructor) {
        StringBuilder stringBuilder = new StringBuilder(constructor.getDeclaringClass().getName());
        appendExecutableParameters(constructor, stringBuilder);
        return stringBuilder.toString();
    }

    private static void appendExecutableParameters(AccessibleObject executable, StringBuilder stringBuilder) {
        Class<?>[] parameterTypes;
        stringBuilder.append('(');
        if (executable instanceof Method) {
            parameterTypes = ((Method) executable).getParameterTypes();
        } else {
            parameterTypes = ((Constructor) executable).getParameterTypes();
        }
        Class<?>[] parameters = parameterTypes;
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(parameters[i].getSimpleName());
        }
        stringBuilder.append(')');
    }

    public static String tryMakeAccessible(Constructor<?> constructor) {
        try {
            constructor.setAccessible(true);
            return null;
        } catch (Exception exception) {
            return "Failed making constructor '" + constructorToString(constructor) + "' accessible; either increase its visibility or write a custom InstanceCreator or TypeAdapter for its declaring type: " + exception.getMessage();
        }
    }

    public static boolean isRecord(Class<?> raw) {
        return RECORD_HELPER.isRecord(raw);
    }

    public static String[] getRecordComponentNames(Class<?> raw) {
        return RECORD_HELPER.getRecordComponentNames(raw);
    }

    public static Method getAccessor(Class<?> raw, Field field) {
        return RECORD_HELPER.getAccessor(raw, field);
    }

    public static <T> Constructor<T> getCanonicalRecordConstructor(Class<T> raw) {
        return RECORD_HELPER.getCanonicalRecordConstructor(raw);
    }

    public static RuntimeException createExceptionForUnexpectedIllegalAccess(IllegalAccessException exception) {
        throw new RuntimeException("Unexpected IllegalAccessException occurred (Gson 2.10.1). Certain ReflectionAccessFilter features require Java >= 9 to work correctly. If you are not using ReflectionAccessFilter, report this to the Gson maintainers.", exception);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static RuntimeException createExceptionForRecordReflectionException(ReflectiveOperationException exception) {
        throw new RuntimeException("Unexpected ReflectiveOperationException occurred (Gson 2.10.1). To support Java records, reflection is utilized to read out information about records. All these invocations happens after it is established that records exist in the JVM. This exception is unexpected behavior.", exception);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/internal/reflect/ReflectionHelper$RecordHelper.class */
    private static abstract class RecordHelper {
        abstract boolean isRecord(Class<?> cls);

        abstract String[] getRecordComponentNames(Class<?> cls);

        abstract <T> Constructor<T> getCanonicalRecordConstructor(Class<T> cls);

        public abstract Method getAccessor(Class<?> cls, Field field);

        private RecordHelper() {
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/internal/reflect/ReflectionHelper$RecordSupportedHelper.class */
    private static class RecordSupportedHelper extends RecordHelper {
        private final Method isRecord;
        private final Method getRecordComponents;
        private final Method getName;
        private final Method getType;

        private RecordSupportedHelper() throws NoSuchMethodException {
            super();
            this.isRecord = Class.class.getMethod("isRecord", new Class[0]);
            this.getRecordComponents = Class.class.getMethod("getRecordComponents", new Class[0]);
            Class<?> classRecordComponent = this.getRecordComponents.getReturnType().getComponentType();
            this.getName = classRecordComponent.getMethod("getName", new Class[0]);
            this.getType = classRecordComponent.getMethod("getType", new Class[0]);
        }

        @Override // com.google.gson.internal.reflect.ReflectionHelper.RecordHelper
        boolean isRecord(Class<?> raw) {
            try {
                return ((Boolean) this.isRecord.invoke(raw, new Object[0])).booleanValue();
            } catch (ReflectiveOperationException e) {
                throw ReflectionHelper.createExceptionForRecordReflectionException(e);
            }
        }

        @Override // com.google.gson.internal.reflect.ReflectionHelper.RecordHelper
        String[] getRecordComponentNames(Class<?> raw) {
            try {
                Object[] recordComponents = (Object[]) this.getRecordComponents.invoke(raw, new Object[0]);
                String[] componentNames = new String[recordComponents.length];
                for (int i = 0; i < recordComponents.length; i++) {
                    componentNames[i] = (String) this.getName.invoke(recordComponents[i], new Object[0]);
                }
                return componentNames;
            } catch (ReflectiveOperationException e) {
                throw ReflectionHelper.createExceptionForRecordReflectionException(e);
            }
        }

        @Override // com.google.gson.internal.reflect.ReflectionHelper.RecordHelper
        public <T> Constructor<T> getCanonicalRecordConstructor(Class<T> raw) {
            try {
                Object[] recordComponents = (Object[]) this.getRecordComponents.invoke(raw, new Object[0]);
                Class<?>[] recordComponentTypes = new Class[recordComponents.length];
                for (int i = 0; i < recordComponents.length; i++) {
                    recordComponentTypes[i] = (Class) this.getType.invoke(recordComponents[i], new Object[0]);
                }
                return raw.getDeclaredConstructor(recordComponentTypes);
            } catch (ReflectiveOperationException e) {
                throw ReflectionHelper.createExceptionForRecordReflectionException(e);
            }
        }

        @Override // com.google.gson.internal.reflect.ReflectionHelper.RecordHelper
        public Method getAccessor(Class<?> raw, Field field) {
            try {
                return raw.getMethod(field.getName(), new Class[0]);
            } catch (ReflectiveOperationException e) {
                throw ReflectionHelper.createExceptionForRecordReflectionException(e);
            }
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/internal/reflect/ReflectionHelper$RecordNotSupportedHelper.class */
    private static class RecordNotSupportedHelper extends RecordHelper {
        private RecordNotSupportedHelper() {
            super();
        }

        @Override // com.google.gson.internal.reflect.ReflectionHelper.RecordHelper
        boolean isRecord(Class<?> clazz) {
            return false;
        }

        @Override // com.google.gson.internal.reflect.ReflectionHelper.RecordHelper
        String[] getRecordComponentNames(Class<?> clazz) {
            throw new UnsupportedOperationException("Records are not supported on this JVM, this method should not be called");
        }

        @Override // com.google.gson.internal.reflect.ReflectionHelper.RecordHelper
        <T> Constructor<T> getCanonicalRecordConstructor(Class<T> raw) {
            throw new UnsupportedOperationException("Records are not supported on this JVM, this method should not be called");
        }

        @Override // com.google.gson.internal.reflect.ReflectionHelper.RecordHelper
        public Method getAccessor(Class<?> raw, Field field) {
            throw new UnsupportedOperationException("Records are not supported on this JVM, this method should not be called");
        }
    }
}
