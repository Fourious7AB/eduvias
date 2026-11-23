/*
 * Decompiled with CFR 0.152.
 */
package org.springframework.boot.loader.archive;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.jar.Manifest;

public interface Archive
extends Iterable<Entry>,
AutoCloseable {
    public URL getUrl() throws MalformedURLException;

    public Manifest getManifest() throws IOException;

    public Iterator<Archive> getNestedArchives(EntryFilter var1, EntryFilter var2) throws IOException;

    default public boolean isExploded() {
        return false;
    }

    @Override
    default public void close() throws Exception {
    }

    @FunctionalInterface
    public static interface EntryFilter {
        public boolean matches(Entry var1);
    }

    public static interface Entry {
        public boolean isDirectory();

        public String getName();
    }
}

