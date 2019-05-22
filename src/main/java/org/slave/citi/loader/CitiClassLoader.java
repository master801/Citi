package org.slave.citi.loader;

import lombok.AccessLevel;
import lombok.Getter;
import org.slave.citi.Citi;

import java.io.*;
import java.net.*;
import java.util.logging.Level;

public final class CitiClassLoader extends URLClassLoader {

    @Getter(value = AccessLevel.PUBLIC)
    private static CitiClassLoader citiClassLoader;

    public CitiClassLoader(final ClassLoader parent) {
        super(new URL[0], parent);
    }

    public void addFile(final File file) {
        if (file == null) return;
        try {
            super.addURL(file.toURI().toURL());
        } catch (MalformedURLException e) {
            Citi.LOGGER_CITI.log(Level.WARNING, "Failed to add file to classloader due to caught exception!", e);
        }
    }

    public static void replaceCurrentClassLoader() {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        CitiClassLoader citiClassLoader = new CitiClassLoader(currentClassLoader);
        Thread.currentThread().setContextClassLoader(citiClassLoader);

        CitiClassLoader.citiClassLoader = citiClassLoader;
    }

}
