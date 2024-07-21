package com.sun.el.parser;

import java.util.ArrayList;
import java.util.List;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/AstLambdaParameters.class */
public class AstLambdaParameters extends SimpleNode {
    public AstLambdaParameters(int id) {
        super(id);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<String> getParameters() {
        Node[] nodeArr;
        List<String> parameters = new ArrayList<>();
        if (this.children != null) {
            for (Node child : this.children) {
                parameters.add(child.getImage());
            }
        }
        return parameters;
    }
}
