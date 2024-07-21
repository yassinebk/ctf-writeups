package org.springframework.boot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/ExitCodeGenerators.class */
class ExitCodeGenerators implements Iterable<ExitCodeGenerator> {
    private List<ExitCodeGenerator> generators = new ArrayList();

    void addAll(Throwable exception, ExitCodeExceptionMapper... mappers) {
        Assert.notNull(exception, "Exception must not be null");
        Assert.notNull(mappers, "Mappers must not be null");
        addAll(exception, Arrays.asList(mappers));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addAll(Throwable exception, Iterable<? extends ExitCodeExceptionMapper> mappers) {
        Assert.notNull(exception, "Exception must not be null");
        Assert.notNull(mappers, "Mappers must not be null");
        for (ExitCodeExceptionMapper mapper : mappers) {
            add(exception, mapper);
        }
    }

    void add(Throwable exception, ExitCodeExceptionMapper mapper) {
        Assert.notNull(exception, "Exception must not be null");
        Assert.notNull(mapper, "Mapper must not be null");
        add(new MappedExitCodeGenerator(exception, mapper));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addAll(ExitCodeGenerator... generators) {
        Assert.notNull(generators, "Generators must not be null");
        addAll(Arrays.asList(generators));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addAll(Iterable<? extends ExitCodeGenerator> generators) {
        Assert.notNull(generators, "Generators must not be null");
        for (ExitCodeGenerator generator : generators) {
            add(generator);
        }
    }

    void add(ExitCodeGenerator generator) {
        Assert.notNull(generator, "Generator must not be null");
        this.generators.add(generator);
    }

    @Override // java.lang.Iterable
    public Iterator<ExitCodeGenerator> iterator() {
        return this.generators.iterator();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getExitCode() {
        int exitCode = 0;
        for (ExitCodeGenerator generator : this.generators) {
            try {
                int value = generator.getExitCode();
                if ((value > 0 && value > exitCode) || (value < 0 && value < exitCode)) {
                    exitCode = value;
                }
            } catch (Exception ex) {
                exitCode = exitCode != 0 ? exitCode : 1;
                ex.printStackTrace();
            }
        }
        return exitCode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/ExitCodeGenerators$MappedExitCodeGenerator.class */
    public static class MappedExitCodeGenerator implements ExitCodeGenerator {
        private final Throwable exception;
        private final ExitCodeExceptionMapper mapper;

        MappedExitCodeGenerator(Throwable exception, ExitCodeExceptionMapper mapper) {
            this.exception = exception;
            this.mapper = mapper;
        }

        @Override // org.springframework.boot.ExitCodeGenerator
        public int getExitCode() {
            return this.mapper.getExitCode(this.exception);
        }
    }
}
