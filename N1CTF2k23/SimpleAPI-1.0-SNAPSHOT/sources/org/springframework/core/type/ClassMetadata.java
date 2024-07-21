package org.springframework.core.type;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/type/ClassMetadata.class */
public interface ClassMetadata {
    String getClassName();

    boolean isInterface();

    boolean isAnnotation();

    boolean isAbstract();

    boolean isFinal();

    boolean isIndependent();

    @Nullable
    String getEnclosingClassName();

    @Nullable
    String getSuperClassName();

    String[] getInterfaceNames();

    String[] getMemberClassNames();

    default boolean isConcrete() {
        return (isInterface() || isAbstract()) ? false : true;
    }

    default boolean hasEnclosingClass() {
        return getEnclosingClassName() != null;
    }

    default boolean hasSuperClass() {
        return getSuperClassName() != null;
    }
}
