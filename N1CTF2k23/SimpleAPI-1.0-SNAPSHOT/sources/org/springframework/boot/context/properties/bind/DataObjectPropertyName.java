package org.springframework.boot.context.properties.bind;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/context/properties/bind/DataObjectPropertyName.class */
public abstract class DataObjectPropertyName {
    private DataObjectPropertyName() {
    }

    public static String toDashedForm(String name) {
        StringBuilder result = new StringBuilder();
        String replaced = name.replace('_', '-');
        for (int i = 0; i < replaced.length(); i++) {
            char ch2 = replaced.charAt(i);
            if (Character.isUpperCase(ch2) && result.length() > 0 && result.charAt(result.length() - 1) != '-') {
                result.append('-');
            }
            result.append(Character.toLowerCase(ch2));
        }
        return result.toString();
    }
}
