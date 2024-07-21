package org.springframework.format;

import java.lang.annotation.Annotation;
import org.springframework.core.convert.converter.ConverterRegistry;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/format/FormatterRegistry.class */
public interface FormatterRegistry extends ConverterRegistry {
    void addPrinter(Printer<?> printer);

    void addParser(Parser<?> parser);

    void addFormatter(Formatter<?> formatter);

    void addFormatterForFieldType(Class<?> cls, Formatter<?> formatter);

    void addFormatterForFieldType(Class<?> cls, Printer<?> printer, Parser<?> parser);

    void addFormatterForFieldAnnotation(AnnotationFormatterFactory<? extends Annotation> annotationFormatterFactory);
}
