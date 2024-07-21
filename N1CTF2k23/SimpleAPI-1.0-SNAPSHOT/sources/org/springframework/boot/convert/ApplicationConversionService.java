package org.springframework.boot.convert;

import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.Parser;
import org.springframework.format.Printer;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.StringValueResolver;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/convert/ApplicationConversionService.class */
public class ApplicationConversionService extends FormattingConversionService {
    private static volatile ApplicationConversionService sharedInstance;

    public ApplicationConversionService() {
        this(null);
    }

    public ApplicationConversionService(StringValueResolver embeddedValueResolver) {
        if (embeddedValueResolver != null) {
            setEmbeddedValueResolver(embeddedValueResolver);
        }
        configure(this);
    }

    public static ConversionService getSharedInstance() {
        ApplicationConversionService sharedInstance2 = sharedInstance;
        if (sharedInstance2 == null) {
            synchronized (ApplicationConversionService.class) {
                sharedInstance2 = sharedInstance;
                if (sharedInstance2 == null) {
                    sharedInstance2 = new ApplicationConversionService();
                    sharedInstance = sharedInstance2;
                }
            }
        }
        return sharedInstance2;
    }

    public static void configure(FormatterRegistry registry) {
        DefaultConversionService.addDefaultConverters(registry);
        DefaultFormattingConversionService.addDefaultFormatters(registry);
        addApplicationFormatters(registry);
        addApplicationConverters(registry);
    }

    public static void addApplicationConverters(ConverterRegistry registry) {
        addDelimitedStringConverters(registry);
        registry.addConverter(new StringToDurationConverter());
        registry.addConverter(new DurationToStringConverter());
        registry.addConverter(new NumberToDurationConverter());
        registry.addConverter(new DurationToNumberConverter());
        registry.addConverter(new StringToPeriodConverter());
        registry.addConverter(new PeriodToStringConverter());
        registry.addConverter(new NumberToPeriodConverter());
        registry.addConverter(new StringToDataSizeConverter());
        registry.addConverter(new NumberToDataSizeConverter());
        registry.addConverter(new StringToFileConverter());
        registry.addConverter(new InputStreamSourceToByteArrayConverter());
        registry.addConverterFactory(new LenientStringToEnumConverterFactory());
        registry.addConverterFactory(new LenientBooleanToEnumConverterFactory());
    }

    public static void addDelimitedStringConverters(ConverterRegistry registry) {
        ConversionService service = (ConversionService) registry;
        registry.addConverter(new ArrayToDelimitedStringConverter(service));
        registry.addConverter(new CollectionToDelimitedStringConverter(service));
        registry.addConverter(new DelimitedStringToArrayConverter(service));
        registry.addConverter(new DelimitedStringToCollectionConverter(service));
    }

    public static void addApplicationFormatters(FormatterRegistry registry) {
        registry.addFormatter(new CharArrayFormatter());
        registry.addFormatter(new InetAddressFormatter());
        registry.addFormatter(new IsoOffsetFormatter());
    }

    public static void addBeans(FormatterRegistry registry, ListableBeanFactory beanFactory) {
        Set<Object> beans = new LinkedHashSet<>();
        beans.addAll(beanFactory.getBeansOfType(GenericConverter.class).values());
        beans.addAll(beanFactory.getBeansOfType(Converter.class).values());
        beans.addAll(beanFactory.getBeansOfType(Printer.class).values());
        beans.addAll(beanFactory.getBeansOfType(Parser.class).values());
        for (Object bean : beans) {
            if (bean instanceof GenericConverter) {
                registry.addConverter((GenericConverter) bean);
            } else if (bean instanceof Converter) {
                registry.addConverter((Converter) bean);
            } else if (bean instanceof Formatter) {
                registry.addFormatter((Formatter) bean);
            } else if (bean instanceof Printer) {
                registry.addPrinter((Printer) bean);
            } else if (bean instanceof Parser) {
                registry.addParser((Parser) bean);
            }
        }
    }
}
