package com.sun.el.stream;

import com.sun.el.lang.ELArithmetic;
import com.sun.el.lang.ELSupport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import javax.el.ELException;
import javax.el.LambdaExpression;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/stream/Stream.class */
public class Stream {
    private Iterator<Object> source;
    private Stream upstream;
    private Operator op;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Stream(Iterator<Object> source) {
        this.source = source;
    }

    Stream(Stream upstream, Operator op) {
        this.upstream = upstream;
        this.op = op;
    }

    public Iterator<Object> iterator() {
        if (this.source != null) {
            return this.source;
        }
        return this.op.iterator(this.upstream.iterator());
    }

    public Stream filter(final LambdaExpression predicate) {
        return new Stream(this, new Operator() { // from class: com.sun.el.stream.Stream.1
            @Override // com.sun.el.stream.Operator
            public Iterator<Object> iterator(Iterator<Object> upstream) {
                return new Iterator2(upstream) { // from class: com.sun.el.stream.Stream.1.1
                    {
                        Stream stream = Stream.this;
                    }

                    @Override // com.sun.el.stream.Stream.Iterator2
                    public void doItem(Object item) {
                        if (((Boolean) predicate.invoke(item)).booleanValue()) {
                            yield(item);
                        }
                    }
                };
            }
        });
    }

    public Stream map(final LambdaExpression mapper) {
        return new Stream(this, new Operator() { // from class: com.sun.el.stream.Stream.2
            @Override // com.sun.el.stream.Operator
            public Iterator<Object> iterator(Iterator<Object> up) {
                return new Iterator1(up) { // from class: com.sun.el.stream.Stream.2.1
                    {
                        Stream stream = Stream.this;
                    }

                    @Override // java.util.Iterator
                    public Object next() {
                        return mapper.invoke(this.iter.next());
                    }
                };
            }
        });
    }

    public Stream peek(final LambdaExpression comsumer) {
        return new Stream(this, new Operator() { // from class: com.sun.el.stream.Stream.3
            @Override // com.sun.el.stream.Operator
            public Iterator<Object> iterator(Iterator<Object> up) {
                return new Iterator2(up) { // from class: com.sun.el.stream.Stream.3.1
                    {
                        Stream stream = Stream.this;
                    }

                    @Override // com.sun.el.stream.Stream.Iterator2
                    void doItem(Object item) {
                        comsumer.invoke(item);
                        yield(item);
                    }
                };
            }
        });
    }

    public Stream limit(final long n) {
        if (n < 0) {
            throw new IllegalArgumentException("limit must be non-negative");
        }
        return new Stream(this, new Operator() { // from class: com.sun.el.stream.Stream.4
            @Override // com.sun.el.stream.Operator
            public Iterator<Object> iterator(final Iterator<Object> up) {
                return new Iterator0() { // from class: com.sun.el.stream.Stream.4.1
                    long limit;

                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super();
                        this.limit = n;
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        if (this.limit > 0) {
                            return up.hasNext();
                        }
                        return false;
                    }

                    @Override // java.util.Iterator
                    public Object next() {
                        this.limit--;
                        return up.next();
                    }
                };
            }
        });
    }

    public Stream substream(final long startIndex) {
        if (startIndex < 0) {
            throw new IllegalArgumentException("substream index must be non-negative");
        }
        return new Stream(this, new Operator() { // from class: com.sun.el.stream.Stream.5
            long skip;

            {
                this.skip = startIndex;
            }

            @Override // com.sun.el.stream.Operator
            public Iterator<Object> iterator(Iterator<Object> up) {
                while (this.skip > 0 && up.hasNext()) {
                    up.next();
                    this.skip--;
                }
                return up;
            }
        });
    }

    public Stream substream(long startIndex, long endIndex) {
        return substream(startIndex).limit(endIndex - startIndex);
    }

    public Stream distinct() {
        return new Stream(this, new Operator() { // from class: com.sun.el.stream.Stream.6
            @Override // com.sun.el.stream.Operator
            public Iterator<Object> iterator(Iterator<Object> up) {
                return new Iterator2(up) { // from class: com.sun.el.stream.Stream.6.1
                    private Set<Object> set;

                    {
                        Stream stream = Stream.this;
                        this.set = new HashSet();
                    }

                    @Override // com.sun.el.stream.Stream.Iterator2
                    public void doItem(Object item) {
                        if (this.set.add(item)) {
                            yield(item);
                        }
                    }
                };
            }
        });
    }

    public Stream sorted() {
        return new Stream(this, new Operator() { // from class: com.sun.el.stream.Stream.7
            private PriorityQueue<Object> queue = null;

            @Override // com.sun.el.stream.Operator
            public Iterator<Object> iterator(Iterator<Object> up) {
                if (this.queue == null) {
                    this.queue = new PriorityQueue<>(16, new Comparator<Object>() { // from class: com.sun.el.stream.Stream.7.1
                        @Override // java.util.Comparator
                        public int compare(Object o1, Object o2) {
                            return ((Comparable) o1).compareTo(o2);
                        }
                    });
                    while (up.hasNext()) {
                        this.queue.add(up.next());
                    }
                }
                return new Iterator0() { // from class: com.sun.el.stream.Stream.7.2
                    {
                        Stream stream = Stream.this;
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return !AnonymousClass7.this.queue.isEmpty();
                    }

                    @Override // java.util.Iterator
                    public Object next() {
                        return AnonymousClass7.this.queue.remove();
                    }
                };
            }
        });
    }

    public Stream sorted(final LambdaExpression comparator) {
        return new Stream(this, new Operator() { // from class: com.sun.el.stream.Stream.8
            private PriorityQueue<Object> queue = null;

            @Override // com.sun.el.stream.Operator
            public Iterator<Object> iterator(Iterator<Object> up) {
                if (this.queue == null) {
                    this.queue = new PriorityQueue<>(16, new Comparator<Object>() { // from class: com.sun.el.stream.Stream.8.1
                        @Override // java.util.Comparator
                        public int compare(Object o1, Object o2) {
                            return ((Integer) ELSupport.coerceToType(comparator.invoke(o1, o2), Integer.class)).intValue();
                        }
                    });
                    while (up.hasNext()) {
                        this.queue.add(up.next());
                    }
                }
                return new Iterator0() { // from class: com.sun.el.stream.Stream.8.2
                    {
                        Stream stream = Stream.this;
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return !AnonymousClass8.this.queue.isEmpty();
                    }

                    @Override // java.util.Iterator
                    public Object next() {
                        return AnonymousClass8.this.queue.remove();
                    }
                };
            }
        });
    }

    public Stream flatMap(final LambdaExpression mapper) {
        return new Stream(this, new Operator() { // from class: com.sun.el.stream.Stream.9
            @Override // com.sun.el.stream.Operator
            public Iterator<Object> iterator(final Iterator<Object> upstream) {
                return new Iterator0() { // from class: com.sun.el.stream.Stream.9.1
                    Iterator<Object> iter;

                    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                    {
                        super();
                        this.iter = null;
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        while (true) {
                            if (this.iter == null) {
                                if (!upstream.hasNext()) {
                                    return false;
                                }
                                Object mapped = mapper.invoke(upstream.next());
                                if (!(mapped instanceof Stream)) {
                                    throw new ELException("Expecting a Stream from flatMap's mapper function.");
                                }
                                this.iter = ((Stream) mapped).iterator();
                            } else if (this.iter.hasNext()) {
                                return true;
                            } else {
                                this.iter = null;
                            }
                        }
                    }

                    @Override // java.util.Iterator
                    public Object next() {
                        if (this.iter == null) {
                            return null;
                        }
                        return this.iter.next();
                    }
                };
            }
        });
    }

    public Object reduce(Object base, LambdaExpression op) {
        Iterator<Object> iter = iterator();
        while (iter.hasNext()) {
            base = op.invoke(base, iter.next());
        }
        return base;
    }

    public Optional reduce(LambdaExpression op) {
        Iterator<Object> iter = iterator();
        if (iter.hasNext()) {
            Object next = iter.next();
            while (true) {
                Object base = next;
                if (iter.hasNext()) {
                    next = op.invoke(base, iter.next());
                } else {
                    return new Optional(base);
                }
            }
        } else {
            return new Optional();
        }
    }

    public void forEach(LambdaExpression comsumer) {
        Iterator<Object> iter = iterator();
        while (iter.hasNext()) {
            comsumer.invoke(iter.next());
        }
    }

    public boolean anyMatch(LambdaExpression predicate) {
        Iterator<Object> iter = iterator();
        while (iter.hasNext()) {
            if (((Boolean) predicate.invoke(iter.next())).booleanValue()) {
                return true;
            }
        }
        return false;
    }

    public boolean allMatch(LambdaExpression predicate) {
        Iterator<Object> iter = iterator();
        while (iter.hasNext()) {
            if (!((Boolean) predicate.invoke(iter.next())).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public boolean noneMatch(LambdaExpression predicate) {
        Iterator<Object> iter = iterator();
        while (iter.hasNext()) {
            if (((Boolean) predicate.invoke(iter.next())).booleanValue()) {
                return false;
            }
        }
        return true;
    }

    public Object[] toArray() {
        Iterator<Object> iter = iterator();
        ArrayList<Object> al = new ArrayList<>();
        while (iter.hasNext()) {
            al.add(iter.next());
        }
        return al.toArray();
    }

    public Object toList() {
        Iterator<Object> iter = iterator();
        ArrayList<Object> al = new ArrayList<>();
        while (iter.hasNext()) {
            al.add(iter.next());
        }
        return al;
    }

    public Optional findFirst() {
        Iterator<Object> iter = iterator();
        if (iter.hasNext()) {
            return new Optional(iter.next());
        }
        return new Optional();
    }

    public Object sum() {
        Number sum = 0L;
        Iterator<Object> iter = iterator();
        while (iter.hasNext()) {
            sum = ELArithmetic.add(sum, iter.next());
        }
        return sum;
    }

    public Object count() {
        long count = 0;
        Iterator<Object> iter = iterator();
        while (iter.hasNext()) {
            count++;
            iter.next();
        }
        return Long.valueOf(count);
    }

    public Optional min() {
        Object min = null;
        Iterator<Object> iter = iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            if (min == null || ELSupport.compare(min, item) > 0) {
                min = item;
            }
        }
        if (min == null) {
            return new Optional();
        }
        return new Optional(min);
    }

    public Optional max() {
        Object max = null;
        Iterator<Object> iter = iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            if (max == null || ELSupport.compare(max, item) < 0) {
                max = item;
            }
        }
        if (max == null) {
            return new Optional();
        }
        return new Optional(max);
    }

    public Optional min(LambdaExpression comparator) {
        Object min = null;
        Iterator<Object> iter = iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            if (min == null || ELSupport.compare(comparator.invoke(item, min), 0L) < 0) {
                min = item;
            }
        }
        if (min == null) {
            return new Optional();
        }
        return new Optional(min);
    }

    public Optional max(LambdaExpression comparator) {
        Object max = null;
        Iterator<Object> iter = iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            if (max == null || ELSupport.compare(comparator.invoke(max, item), 0L) < 0) {
                max = item;
            }
        }
        if (max == null) {
            return new Optional();
        }
        return new Optional(max);
    }

    public Optional average() {
        Number sum = 0L;
        long count = 0;
        Iterator<Object> iter = iterator();
        while (iter.hasNext()) {
            count++;
            sum = ELArithmetic.add(sum, iter.next());
        }
        if (count == 0) {
            return new Optional();
        }
        return new Optional(ELArithmetic.divide((Object) sum, (Object) Long.valueOf(count)));
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/stream/Stream$Iterator0.class */
    abstract class Iterator0 implements Iterator<Object> {
        Iterator0() {
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/stream/Stream$Iterator1.class */
    abstract class Iterator1 extends Iterator0 {
        Iterator iter;

        Iterator1(Iterator iter) {
            super();
            this.iter = iter;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.iter.hasNext();
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:com/sun/el/stream/Stream$Iterator2.class */
    abstract class Iterator2 extends Iterator1 {
        private Object current;
        private boolean yielded;

        abstract void doItem(Object obj);

        Iterator2(Iterator upstream) {
            super(upstream);
        }

        @Override // java.util.Iterator
        public Object next() {
            this.yielded = false;
            return this.current;
        }

        @Override // com.sun.el.stream.Stream.Iterator1, java.util.Iterator
        public boolean hasNext() {
            while (!this.yielded && this.iter.hasNext()) {
                doItem(this.iter.next());
            }
            return this.yielded;
        }

        void yield(Object current) {
            this.current = current;
            this.yielded = true;
        }
    }
}
