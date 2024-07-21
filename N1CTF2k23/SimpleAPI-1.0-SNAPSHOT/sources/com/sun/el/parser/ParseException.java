package com.sun.el.parser;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/parser/ParseException.class */
public class ParseException extends Exception {
    protected boolean specialConstructor;
    public Token currentToken;
    public int[][] expectedTokenSequences;
    public String[] tokenImage;
    protected String eol;

    public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal) {
        super("");
        this.eol = System.getProperty("line.separator", "\n");
        this.specialConstructor = true;
        this.currentToken = currentTokenVal;
        this.expectedTokenSequences = expectedTokenSequencesVal;
        this.tokenImage = tokenImageVal;
    }

    public ParseException() {
        this.eol = System.getProperty("line.separator", "\n");
        this.specialConstructor = false;
    }

    public ParseException(String message) {
        super(message);
        this.eol = System.getProperty("line.separator", "\n");
        this.specialConstructor = false;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        String retval;
        if (!this.specialConstructor) {
            return super.getMessage();
        }
        String expected = "";
        int maxSize = 0;
        for (int i = 0; i < this.expectedTokenSequences.length; i++) {
            if (maxSize < this.expectedTokenSequences[i].length) {
                maxSize = this.expectedTokenSequences[i].length;
            }
            for (int j = 0; j < this.expectedTokenSequences[i].length; j++) {
                expected = expected + this.tokenImage[this.expectedTokenSequences[i][j]] + " ";
            }
            if (this.expectedTokenSequences[i][this.expectedTokenSequences[i].length - 1] != 0) {
                expected = expected + "...";
            }
            expected = expected + this.eol + "    ";
        }
        String retval2 = "Encountered \"";
        Token tok = this.currentToken.next;
        int i2 = 0;
        while (true) {
            if (i2 >= maxSize) {
                break;
            }
            if (i2 != 0) {
                retval2 = retval2 + " ";
            }
            if (tok.kind == 0) {
                retval2 = retval2 + this.tokenImage[0];
                break;
            }
            retval2 = retval2 + add_escapes(tok.image);
            tok = tok.next;
            i2++;
        }
        String retval3 = (retval2 + "\" at line " + this.currentToken.next.beginLine + ", column " + this.currentToken.next.beginColumn) + "." + this.eol;
        if (this.expectedTokenSequences.length == 1) {
            retval = retval3 + "Was expecting:" + this.eol + "    ";
        } else {
            retval = retval3 + "Was expecting one of:" + this.eol + "    ";
        }
        return retval + expected;
    }

    protected String add_escapes(String str) {
        StringBuffer retval = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
                case 0:
                    break;
                case '\b':
                    retval.append("\\b");
                    break;
                case '\t':
                    retval.append("\\t");
                    break;
                case '\n':
                    retval.append("\\n");
                    break;
                case '\f':
                    retval.append("\\f");
                    break;
                case '\r':
                    retval.append("\\r");
                    break;
                case '\"':
                    retval.append("\\\"");
                    break;
                case '\'':
                    retval.append("\\'");
                    break;
                case '\\':
                    retval.append("\\\\");
                    break;
                default:
                    char ch2 = str.charAt(i);
                    if (ch2 < ' ' || ch2 > '~') {
                        String s = "0000" + Integer.toString(ch2, 16);
                        retval.append("\\u" + s.substring(s.length() - 4, s.length()));
                        break;
                    } else {
                        retval.append(ch2);
                        break;
                    }
                    break;
            }
        }
        return retval.toString();
    }
}
