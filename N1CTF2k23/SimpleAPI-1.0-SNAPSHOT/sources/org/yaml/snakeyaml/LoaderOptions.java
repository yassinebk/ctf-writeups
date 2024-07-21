package org.yaml.snakeyaml;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/LoaderOptions.class */
public class LoaderOptions {
    private boolean allowDuplicateKeys = true;
    private boolean wrappedToRootException = false;
    private int maxAliasesForCollections = 50;
    private boolean allowRecursiveKeys;

    public boolean isAllowDuplicateKeys() {
        return this.allowDuplicateKeys;
    }

    public void setAllowDuplicateKeys(boolean allowDuplicateKeys) {
        this.allowDuplicateKeys = allowDuplicateKeys;
    }

    public boolean isWrappedToRootException() {
        return this.wrappedToRootException;
    }

    public void setWrappedToRootException(boolean wrappedToRootException) {
        this.wrappedToRootException = wrappedToRootException;
    }

    public int getMaxAliasesForCollections() {
        return this.maxAliasesForCollections;
    }

    public void setMaxAliasesForCollections(int maxAliasesForCollections) {
        this.maxAliasesForCollections = maxAliasesForCollections;
    }

    public void setAllowRecursiveKeys(boolean allowRecursiveKeys) {
        this.allowRecursiveKeys = allowRecursiveKeys;
    }

    public boolean getAllowRecursiveKeys() {
        return this.allowRecursiveKeys;
    }
}
