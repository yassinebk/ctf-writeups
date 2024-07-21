package javax.el;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/LambdaExpression.class */
public class LambdaExpression {
    private List<String> formalParameters;
    private ValueExpression expression;
    private ELContext context;
    private Map<String, Object> envirArgs = new HashMap();

    public LambdaExpression(List<String> formalParameters, ValueExpression expression) {
        this.formalParameters = new ArrayList();
        this.formalParameters = formalParameters;
        this.expression = expression;
    }

    public void setELContext(ELContext context) {
        this.context = context;
    }

    public Object invoke(ELContext elContext, Object... args) throws ELException {
        int i = 0;
        Map<String, Object> lambdaArgs = new HashMap<>();
        lambdaArgs.putAll(this.envirArgs);
        for (String fParam : this.formalParameters) {
            if (i >= args.length) {
                throw new ELException("Expected Argument " + fParam + " missing in Lambda Expression");
            }
            int i2 = i;
            i++;
            lambdaArgs.put(fParam, args[i2]);
        }
        elContext.enterLambdaScope(lambdaArgs);
        Object ret = this.expression.getValue(elContext);
        if (ret instanceof LambdaExpression) {
            ((LambdaExpression) ret).envirArgs.putAll(lambdaArgs);
        }
        elContext.exitLambdaScope();
        return ret;
    }

    public Object invoke(Object... args) {
        return invoke(this.context, args);
    }
}
