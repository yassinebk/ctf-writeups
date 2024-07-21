package org.springframework.web.servlet.mvc.annotation;

import java.lang.reflect.Method;
import org.springframework.lang.Nullable;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.ModelAndView;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/mvc/annotation/ModelAndViewResolver.class */
public interface ModelAndViewResolver {
    public static final ModelAndView UNRESOLVED = new ModelAndView();

    ModelAndView resolveModelAndView(Method method, Class<?> cls, @Nullable Object obj, ExtendedModelMap extendedModelMap, NativeWebRequest nativeWebRequest);
}
