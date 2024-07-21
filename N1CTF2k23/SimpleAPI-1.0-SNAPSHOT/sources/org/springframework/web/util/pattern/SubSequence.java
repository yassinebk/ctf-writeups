package org.springframework.web.util.pattern;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/util/pattern/SubSequence.class */
class SubSequence implements CharSequence {
    private final char[] chars;
    private final int start;
    private final int end;

    SubSequence(char[] chars, int start, int end) {
        this.chars = chars;
        this.start = start;
        this.end = end;
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.end - this.start;
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        return this.chars[this.start + index];
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return new SubSequence(this.chars, this.start + start, this.start + end);
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return new String(this.chars, this.start, this.end - this.start);
    }
}
