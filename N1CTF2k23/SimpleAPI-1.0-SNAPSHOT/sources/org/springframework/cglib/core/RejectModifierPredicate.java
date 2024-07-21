package org.springframework.cglib.core;

import java.lang.reflect.Member;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/core/RejectModifierPredicate.class */
public class RejectModifierPredicate implements Predicate {
    private int rejectMask;

    public RejectModifierPredicate(int rejectMask) {
        this.rejectMask = rejectMask;
    }

    @Override // org.springframework.cglib.core.Predicate
    public boolean evaluate(Object arg) {
        return (((Member) arg).getModifiers() & this.rejectMask) == 0;
    }
}
