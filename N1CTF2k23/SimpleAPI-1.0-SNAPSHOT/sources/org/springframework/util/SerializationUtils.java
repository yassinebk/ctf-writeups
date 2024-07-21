package org.springframework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/SerializationUtils.class */
public abstract class SerializationUtils {
    @Nullable
    public static byte[] serialize(@Nullable Object object) {
        if (object == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.flush();
            if (oos != null) {
                if (0 != 0) {
                    oos.close();
                } else {
                    oos.close();
                }
            }
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to serialize object of type: " + object.getClass(), ex);
        }
    }

    @Nullable
    public static Object deserialize(@Nullable byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            Object readObject = ois.readObject();
            if (ois != null) {
                if (0 != 0) {
                    ois.close();
                } else {
                    ois.close();
                }
            }
            return readObject;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Failed to deserialize object", ex);
        } catch (ClassNotFoundException ex2) {
            throw new IllegalStateException("Failed to deserialize object type", ex2);
        }
    }
}
