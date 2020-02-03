package org.slave.citi.loader;

import lombok.AccessLevel;
import lombok.Getter;
import org.slave.citi.Citi;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

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
            Citi.LOGGER_CITI.error("Failed to add file to classloader due to caught exception!", e);
        }
    }

    public static void replaceCurrentClassLoader() {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        CitiClassLoader citiClassLoader = new CitiClassLoader(currentClassLoader);
        Thread.currentThread().setContextClassLoader(citiClassLoader);

        CitiClassLoader.citiClassLoader = citiClassLoader;
    }

}
