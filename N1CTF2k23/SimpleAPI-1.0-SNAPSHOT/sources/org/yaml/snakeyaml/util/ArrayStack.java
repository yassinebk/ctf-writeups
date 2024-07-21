package org.yaml.snakeyaml.util;

import java.util.ArrayList;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/util/ArrayStack.class */
public class ArrayStack<T> {
    private ArrayList<T> stack;

    public ArrayStack(int initSize) {
        this.stack = new ArrayList<>(initSize);
    }

    public void push(T obj) {
        this.stack.add(obj);
    }

    public T pop() {
        return this.stack.remove(this.stack.size() - 1);
    }

    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    public void clear() {
        this.stack.clear();
    }
}