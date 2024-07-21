package ch.qos.logback.core.pattern.color;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/pattern/color/YellowCompositeConverter.class */
public class YellowCompositeConverter<E> extends ForegroundCompositeConverterBase<E> {
    @Override // ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase
    protected String getForegroundColorCode(E event) {
        return ANSIConstants.YELLOW_FG;
    }
}
