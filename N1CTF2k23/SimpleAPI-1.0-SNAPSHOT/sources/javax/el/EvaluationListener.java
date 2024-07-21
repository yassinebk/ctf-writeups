package javax.el;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/EvaluationListener.class */
public abstract class EvaluationListener {
    public void beforeEvaluation(ELContext context, String expression) {
    }

    public void afterEvaluation(ELContext context, String expression) {
    }

    public void propertyResolved(ELContext context, Object base, Object property) {
    }
}
