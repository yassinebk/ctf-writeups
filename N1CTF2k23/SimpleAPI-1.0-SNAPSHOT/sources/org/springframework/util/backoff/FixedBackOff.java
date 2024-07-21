package org.springframework.util.backoff;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/backoff/FixedBackOff.class */
public class FixedBackOff implements BackOff {
    public static final long DEFAULT_INTERVAL = 5000;
    public static final long UNLIMITED_ATTEMPTS = Long.MAX_VALUE;
    private long interval;
    private long maxAttempts;

    public FixedBackOff() {
        this.interval = 5000L;
        this.maxAttempts = Long.MAX_VALUE;
    }

    public FixedBackOff(long interval, long maxAttempts) {
        this.interval = 5000L;
        this.maxAttempts = Long.MAX_VALUE;
        this.interval = interval;
        this.maxAttempts = maxAttempts;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public long getInterval() {
        return this.interval;
    }

    public void setMaxAttempts(long maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public long getMaxAttempts() {
        return this.maxAttempts;
    }

    @Override // org.springframework.util.backoff.BackOff
    public BackOffExecution start() {
        return new FixedBackOffExecution();
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/backoff/FixedBackOff$FixedBackOffExecution.class */
    private class FixedBackOffExecution implements BackOffExecution {
        private long currentAttempts;

        private FixedBackOffExecution() {
            this.currentAttempts = 0L;
        }

        @Override // org.springframework.util.backoff.BackOffExecution
        public long nextBackOff() {
            this.currentAttempts++;
            if (this.currentAttempts <= FixedBackOff.this.getMaxAttempts()) {
                return FixedBackOff.this.getInterval();
            }
            return -1L;
        }

        public String toString() {
            String attemptValue = FixedBackOff.this.maxAttempts == Long.MAX_VALUE ? "unlimited" : String.valueOf(FixedBackOff.this.maxAttempts);
            return "FixedBackOff{interval=" + FixedBackOff.this.interval + ", currentAttempts=" + this.currentAttempts + ", maxAttempts=" + attemptValue + '}';
        }
    }
}
