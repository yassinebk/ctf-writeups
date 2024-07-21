package com.google.gson.internal;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/gson-2.10.1.jar:com/google/gson/internal/UnsafeAllocator.class */
public abstract class UnsafeAllocator {
    public static final UnsafeAllocator INSTANCE = create();

    public abstract <T> T newInstance(Class<T> cls) throws Exception;

    /* JADX INFO: Access modifiers changed from: private */
    public static void assertInstantiable(Class<?> c) {
        String exceptionMessage = ConstructorConstructor.checkInstantiable(c);
        if (exceptionMessage != null) {
            throw new AssertionError("UnsafeAllocator is used for non-instantiable type: " + exceptionMessage);
        }
    }

    private static UnsafeAllocator create() {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            final Object unsafe = f.get(null);
            final Method allocateInstance = unsafeClass.getMethod("allocateInstance", Class.class);
            return new UnsafeAllocator() { // from class: com.google.gson.internal.UnsafeAllocator.1
                @Override // com.google.gson.internal.UnsafeAllocator
                public <T> T newInstance(Class<T> c) throws Exception {
                    UnsafeAllocator.assertInstantiable(c);
                    return (T) allocateInstance.invoke(unsafe, c);
                }
            };
        } catch (Exception e) {
            try {
                Method getConstructorId = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
                getConstructorId.setAccessible(true);
                final int constructorId = ((Integer) getConstructorId.invoke(null, Object.class)).intValue();
                final Method newInstance = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, Integer.TYPE);
                newInstance.setAccessible(true);
                return new UnsafeAllocator() { // from class: com.google.gson.internal.UnsafeAllocator.2
                    @Override // com.google.gson.internal.UnsafeAllocator
                    public <T> T newInstance(Class<T> c) throws Exception {
                        UnsafeAllocator.assertInstantiable(c);
                        return (T) newInstance.invoke(null, c, Integer.valueOf(constructorId));
                    }
                };
            } catch (Exception e2) {
                try {
                    final Method newInstance2 = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
                    newInstance2.setAccessible(true);
                    return new UnsafeAllocator() { // from class: com.google.gson.internal.UnsafeAllocator.3
                        @Override // com.google.gson.internal.UnsafeAllocator
                        public <T> T newInstance(Class<T> c) throws Exception {
                            UnsafeAllocator.assertInstantiable(c);
                            return (T) newInstance2.invoke(null, c, Object.class);
                        }
                    };
                } catch (Exception e3) {
                    return new UnsafeAllocator() { // from class: com.google.gson.internal.UnsafeAllocator.4
                        @Override // com.google.gson.internal.UnsafeAllocator
                        public <T> T newInstance(Class<T> c) {
                            throw new UnsupportedOperationException("Cannot allocate " + c + ". Usage of JDK sun.misc.Unsafe is enabled, but it could not be used. Make sure your runtime is configured correctly.");
                        }
                    };
                }
            }
        }
    }
}
