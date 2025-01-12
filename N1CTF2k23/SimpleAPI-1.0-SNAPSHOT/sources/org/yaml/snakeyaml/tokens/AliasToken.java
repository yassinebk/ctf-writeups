package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.tokens.Token;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/tokens/AliasToken.class */
public final class AliasToken extends Token {
    private final String value;

    public AliasToken(String value, Mark startMark, Mark endMark) {
        super(startMark, endMark);
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override // org.yaml.snakeyaml.tokens.Token
    public Token.ID getTokenId() {
        return Token.ID.Alias;
    }
}
