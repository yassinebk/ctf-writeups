package org.springframework.core.type.classreading;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
@Deprecated
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/type/classreading/AnnotationAttributesReadingVisitor.class */
final class AnnotationAttributesReadingVisitor extends RecursiveAnnotationAttributesVisitor {
    private final MultiValueMap<String, AnnotationAttributes> attributesMap;
    private final Map<String, Set<String>> metaAnnotationMap;

    public AnnotationAttributesReadingVisitor(String annotationType, MultiValueMap<String, AnnotationAttributes> attributesMap, Map<String, Set<String>> metaAnnotationMap, @Nullable ClassLoader classLoader) {
        super(annotationType, new AnnotationAttributes(annotationType, classLoader), classLoader);
        this.attributesMap = attributesMap;
        this.metaAnnotationMap = metaAnnotationMap;
    }

    @Override // org.springframework.core.type.classreading.RecursiveAnnotationAttributesVisitor, org.springframework.asm.AnnotationVisitor
    public void visitEnd() {
        super.visitEnd();
        Class<? extends Annotation> annotationClass = this.attributes.annotationType();
        if (annotationClass != null) {
            List<AnnotationAttributes> attributeList = (List) this.attributesMap.get(this.annotationType);
            if (attributeList == null) {
                this.attributesMap.add(this.annotationType, this.attributes);
            } else {
                attributeList.add(0, this.attributes);
            }
            if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotationClass.getName())) {
                try {
                    Annotation[] metaAnnotations = annotationClass.getAnnotations();
                    if (!ObjectUtils.isEmpty((Object[]) metaAnnotations)) {
                        Set<Annotation> visited = new LinkedHashSet<>();
                        for (Annotation metaAnnotation : metaAnnotations) {
                            recursivelyCollectMetaAnnotations(visited, metaAnnotation);
                        }
                        if (!visited.isEmpty()) {
                            Set<String> metaAnnotationTypeNames = new LinkedHashSet<>(visited.size());
                            for (Annotation ann : visited) {
                                metaAnnotationTypeNames.add(ann.annotationType().getName());
                            }
                            this.metaAnnotationMap.put(annotationClass.getName(), metaAnnotationTypeNames);
                        }
                    }
                } catch (Throwable ex) {
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug("Failed to introspect meta-annotations on " + annotationClass + ": " + ex);
                    }
                }
            }
        }
    }

    private void recursivelyCollectMetaAnnotations(Set<Annotation> visited, Annotation annotation) {
        Annotation[] annotations;
        Class<? extends Annotation> annotationType = annotation.annotationType();
        String annotationName = annotationType.getName();
        if (!AnnotationUtils.isInJavaLangAnnotationPackage(annotationName) && visited.add(annotation)) {
            try {
                if (Modifier.isPublic(annotationType.getModifiers())) {
                    this.attributesMap.add(annotationName, AnnotationUtils.getAnnotationAttributes(annotation, false, true));
                }
                for (Annotation metaMetaAnnotation : annotationType.getAnnotations()) {
                    recursivelyCollectMetaAnnotations(visited, metaMetaAnnotation);
                }
            } catch (Throwable ex) {
                if (this.logger.isDebugEnabled()) {
                    this.logger.debug("Failed to introspect meta-annotations on " + annotation + ": " + ex);
                }
            }
        }
    }
}