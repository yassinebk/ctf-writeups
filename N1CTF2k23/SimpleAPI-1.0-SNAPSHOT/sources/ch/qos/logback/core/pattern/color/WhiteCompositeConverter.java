package ch.qos.logback.core.pattern.color;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/pattern/color/WhiteCompositeConverter.class */
public class WhiteCompositeConverter<E> extends ForegroundCompositeConverterBase<E> {
    @Override // ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase
    protected String getForegroundColorCode(E event) {
        return ANSIConstants.WHITE_FG;
    }
}
