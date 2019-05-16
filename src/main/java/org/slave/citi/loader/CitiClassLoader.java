package org.slave.citi.loader;

import java.io.*;
import java.net.*;

public final class CitiClassLoader extends URLClassLoader {

    public CitiClassLoader(final ClassLoader parent) {
        super(new URL[0], parent);
    }

    public void addFile(final File file) {
        if (file == null) return;
        try {
            super.addURL(file.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
