package ch.qos.logback.classic.spi;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-classic-1.2.3.jar:ch/qos/logback/classic/spi/IThrowableProxy.class */
public interface IThrowableProxy {
    String getMessage();

    String getClassName();

    StackTraceElementProxy[] getStackTraceElementProxyArray();

    int getCommonFrames();

    IThrowableProxy getCause();

    IThrowableProxy[] getSuppressed();
}
