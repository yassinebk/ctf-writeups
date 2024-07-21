package javax.el;

import java.util.EventObject;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ELContextEvent.class */
public class ELContextEvent extends EventObject {
    private static final long serialVersionUID = 1255131906285426769L;

    public ELContextEvent(ELContext source) {
        super(source);
    }

    public ELContext getELContext() {
        return (ELContext) getSource();
    }
}
