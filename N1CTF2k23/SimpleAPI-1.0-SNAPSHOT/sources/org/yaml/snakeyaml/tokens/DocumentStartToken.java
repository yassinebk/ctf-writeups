package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.tokens.Token;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/tokens/DocumentStartToken.class */
public final class DocumentStartToken extends Token {
    public DocumentStartToken(Mark startMark, Mark endMark) {
        super(startMark, endMark);
    }

    @Override // org.yaml.snakeyaml.tokens.Token
    public Token.ID getTokenId() {
        return Token.ID.DocumentStart;
    }
}