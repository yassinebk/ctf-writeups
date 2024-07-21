package org.springframework.boot.diagnostics.analyzer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.util.ClassUtils;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/diagnostics/analyzer/NoSuchMethodFailureAnalyzer.class */
class NoSuchMethodFailureAnalyzer extends AbstractFailureAnalyzer<NoSuchMethodError> {
    NoSuchMethodFailureAnalyzer() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.boot.diagnostics.AbstractFailureAnalyzer
    public FailureAnalysis analyze(Throwable rootFailure, NoSuchMethodError cause) {
        NoSuchMethodDescriptor descriptor = getNoSuchMethodDescriptor(cause.getMessage());
        if (descriptor == null) {
            return null;
        }
        String description = getDescription(cause, descriptor);
        return new FailureAnalysis(description, "Correct the classpath of your application so that it contains a single, compatible version of " + descriptor.getClassName(), cause);
    }

    protected NoSuchMethodDescriptor getNoSuchMethodDescriptor(String cause) {
        List<URL> candidates;
        URL actual;
        String message = cleanMessage(cause);
        String className = extractClassName(message);
        if (className == null || (candidates = findCandidates(className)) == null || (actual = getActual(className)) == null) {
            return null;
        }
        return new NoSuchMethodDescriptor(message, className, candidates, actual);
    }

    private String cleanMessage(String message) {
        int loadedFromIndex = message.indexOf(" (loaded from");
        if (loadedFromIndex == -1) {
            return message;
        }
        return message.substring(0, loadedFromIndex);
    }

    private String extractClassName(String message) {
        String classAndMethodName;
        int methodNameIndex;
        if (message.startsWith("'") && message.endsWith("'")) {
            int splitIndex = message.indexOf(32);
            if (splitIndex == -1) {
                return null;
            }
            message = message.substring(splitIndex + 1);
        }
        int descriptorIndex = message.indexOf(40);
        if (descriptorIndex == -1 || (methodNameIndex = (classAndMethodName = message.substring(0, descriptorIndex)).lastIndexOf(46)) == -1) {
            return null;
        }
        String className = classAndMethodName.substring(0, methodNameIndex);
        return className.replace('/', '.');
    }

    private List<URL> findCandidates(String className) {
        try {
            return Collections.list(NoSuchMethodFailureAnalyzer.class.getClassLoader().getResources(ClassUtils.convertClassNameToResourcePath(className) + ClassUtils.CLASS_FILE_SUFFIX));
        } catch (Throwable th) {
            return null;
        }
    }

    private URL getActual(String className) {
        try {
            return Class.forName(className, false, getClass().getClassLoader()).getProtectionDomain().getCodeSource().getLocation();
        } catch (Throwable th) {
            return null;
        }
    }

    private String getDescription(NoSuchMethodError cause, NoSuchMethodDescriptor descriptor) {
        StringWriter description = new StringWriter();
        PrintWriter writer = new PrintWriter(description);
        writer.println("An attempt was made to call a method that does not exist. The attempt was made from the following location:");
        writer.println();
        writer.print("    ");
        writer.println(cause.getStackTrace()[0]);
        writer.println();
        writer.println("The following method did not exist:");
        writer.println();
        writer.print("    ");
        writer.println(descriptor.getErrorMessage());
        writer.println();
        writer.println("The method's class, " + descriptor.getClassName() + ", is available from the following locations:");
        writer.println();
        for (URL candidate : descriptor.getCandidateLocations()) {
            writer.print("    ");
            writer.println(candidate);
        }
        writer.println();
        writer.println("It was loaded from the following location:");
        writer.println();
        writer.print("    ");
        writer.println(descriptor.getActualLocation());
        return description.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/diagnostics/analyzer/NoSuchMethodFailureAnalyzer$NoSuchMethodDescriptor.class */
    public static class NoSuchMethodDescriptor {
        private final String errorMessage;
        private final String className;
        private final List<URL> candidateLocations;
        private final URL actualLocation;

        public NoSuchMethodDescriptor(String errorMessage, String className, List<URL> candidateLocations, URL actualLocation) {
            this.errorMessage = errorMessage;
            this.className = className;
            this.candidateLocations = candidateLocations;
            this.actualLocation = actualLocation;
        }

        public String getErrorMessage() {
            return this.errorMessage;
        }

        public String getClassName() {
            return this.className;
        }

        public List<URL> getCandidateLocations() {
            return this.candidateLocations;
        }

        public URL getActualLocation() {
            return this.actualLocation;
        }
    }
}
