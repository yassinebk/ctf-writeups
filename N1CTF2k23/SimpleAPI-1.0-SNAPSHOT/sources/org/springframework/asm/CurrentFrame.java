package org.springframework.asm;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/asm/CurrentFrame.class */
final class CurrentFrame extends Frame {
    /* JADX INFO: Access modifiers changed from: package-private */
    public CurrentFrame(Label owner) {
        super(owner);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // org.springframework.asm.Frame
    public void execute(int opcode, int arg, Symbol symbolArg, SymbolTable symbolTable) {
        super.execute(opcode, arg, symbolArg, symbolTable);
        Frame successor = new Frame(null);
        merge(symbolTable, successor, 0);
        copyFrom(successor);
    }
}
