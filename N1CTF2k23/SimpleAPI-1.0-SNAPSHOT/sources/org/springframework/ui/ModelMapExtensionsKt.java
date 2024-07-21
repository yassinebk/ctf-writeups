package org.springframework.ui;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ModelMapExtensions.kt */
@Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 2, d1 = {"��\u0018\n��\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n��\n\u0002\u0010\u000e\n��\n\u0002\u0010��\n��\u001a\u001d\u0010��\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0086\u0002¨\u0006\u0007"}, d2 = {"set", "", "Lorg/springframework/ui/ModelMap;", "attributeName", "", "attributeValue", "", "spring-context"})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/ui/ModelMapExtensionsKt.class */
public final class ModelMapExtensionsKt {
    public static final void set(@NotNull ModelMap $this$set, @NotNull String attributeName, @NotNull Object attributeValue) {
        Intrinsics.checkParameterIsNotNull($this$set, "$this$set");
        Intrinsics.checkParameterIsNotNull(attributeName, "attributeName");
        Intrinsics.checkParameterIsNotNull(attributeValue, "attributeValue");
        $this$set.addAttribute(attributeName, attributeValue);
    }
}
