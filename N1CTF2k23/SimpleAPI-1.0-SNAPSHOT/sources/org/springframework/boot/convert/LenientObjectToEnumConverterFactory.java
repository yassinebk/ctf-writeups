package org.springframework.boot.convert;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/convert/LenientObjectToEnumConverterFactory.class */
public abstract class LenientObjectToEnumConverterFactory<T> implements ConverterFactory<T, Enum<?>> {
    private static Map<String, List<String>> ALIASES;

    static {
        MultiValueMap<String, String> aliases = new LinkedMultiValueMap<>();
        aliases.add("true", CustomBooleanEditor.VALUE_ON);
        aliases.add("false", CustomBooleanEditor.VALUE_OFF);
        ALIASES = Collections.unmodifiableMap(aliases);
    }

    @Override // org.springframework.core.convert.converter.ConverterFactory
    public <E extends Enum<?>> Converter<T, E> getConverter(Class<E> targetType) {
        Class<E> cls;
        Class<E> cls2 = targetType;
        while (true) {
            cls = cls2;
            if (cls == null || cls.isEnum()) {
                break;
            }
            cls2 = cls.getSuperclass();
        }
        Assert.notNull(cls, () -> {
            return "The target type " + targetType.getName() + " does not refer to an enum";
        });
        return new LenientToEnumConverter(cls);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/convert/LenientObjectToEnumConverterFactory$LenientToEnumConverter.class */
    private class LenientToEnumConverter<E extends Enum> implements Converter<T, E> {
        private final Class<E> enumType;

        @Override // org.springframework.core.convert.converter.Converter
        public /* bridge */ /* synthetic */ Object convert(Object source) {
            return convert((LenientToEnumConverter<E>) source);
        }

        LenientToEnumConverter(Class<E> enumType) {
            this.enumType = enumType;
        }

        @Override // org.springframework.core.convert.converter.Converter
        public E convert(T source) {
            String value = source.toString().trim();
            if (value.isEmpty()) {
                return null;
            }
            try {
                return (E) Enum.valueOf(this.enumType, value);
            } catch (Exception e) {
                return findEnum(value);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:5:0x002e  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private E findEnum(java.lang.String r6) {
            /*
                r5 = this;
                r0 = r5
                r1 = r6
                java.lang.String r0 = r0.getCanonicalName(r1)
                r7 = r0
                java.util.Map r0 = org.springframework.boot.convert.LenientObjectToEnumConverterFactory.access$000()
                r1 = r7
                java.util.List r2 = java.util.Collections.emptyList()
                java.lang.Object r0 = r0.getOrDefault(r1, r2)
                java.util.List r0 = (java.util.List) r0
                r8 = r0
                r0 = r5
                java.lang.Class<E extends java.lang.Enum> r0 = r0.enumType
                java.util.EnumSet r0 = java.util.EnumSet.allOf(r0)
                java.util.Iterator r0 = r0.iterator()
                r9 = r0
            L24:
                r0 = r9
                boolean r0 = r0.hasNext()
                if (r0 == 0) goto L5f
                r0 = r9
                java.lang.Object r0 = r0.next()
                java.lang.Enum r0 = (java.lang.Enum) r0
                r10 = r0
                r0 = r5
                r1 = r10
                java.lang.String r1 = r1.name()
                java.lang.String r0 = r0.getCanonicalName(r1)
                r11 = r0
                r0 = r7
                r1 = r11
                boolean r0 = r0.equals(r1)
                if (r0 != 0) goto L59
                r0 = r8
                r1 = r11
                boolean r0 = r0.contains(r1)
                if (r0 == 0) goto L5c
            L59:
                r0 = r10
                return r0
            L5c:
                goto L24
            L5f:
                java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
                r1 = r0
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r3 = r2
                r3.<init>()
                java.lang.String r3 = "No enum constant "
                java.lang.StringBuilder r2 = r2.append(r3)
                r3 = r5
                java.lang.Class<E extends java.lang.Enum> r3 = r3.enumType
                java.lang.String r3 = r3.getCanonicalName()
                java.lang.StringBuilder r2 = r2.append(r3)
                java.lang.String r3 = "."
                java.lang.StringBuilder r2 = r2.append(r3)
                r3 = r6
                java.lang.StringBuilder r2 = r2.append(r3)
                java.lang.String r2 = r2.toString()
                r1.<init>(r2)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.springframework.boot.convert.LenientObjectToEnumConverterFactory.LenientToEnumConverter.findEnum(java.lang.String):java.lang.Enum");
        }

        private String getCanonicalName(String name) {
            StringBuilder canonicalName = new StringBuilder(name.length());
            name.chars().filter(Character::isLetterOrDigit).map(Character::toLowerCase).forEach(c -> {
                canonicalName.append((char) c);
            });
            return canonicalName.toString();
        }
    }
}
