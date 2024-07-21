package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.tokens.Token;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/tokens/ValueToken.class */
public final class ValueToken extends Token {
    public ValueToken(Mark startMark, Mark endMark) {
        super(startMark, endMark);
    }

    @Override // org.yaml.snakeyaml.tokens.Token
    public Token.ID getTokenId() {
        return Token.ID.Value;
    }
}
