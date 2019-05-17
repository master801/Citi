package org.slave.citi;

import org.slave.citi.asm.transformers.TransformerTowns;
import org.slave.citi.asm.transformers.TransformerTownsMain;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Master801 on 3/18/2016 at 6:12 AM.
 *
 * @author Master801
 */
public final class Agent {

    private static final String CITI_AGENT_VERSION = "v0.0.1-Apple";

    public static void premain(final String agentArguments, final Instrumentation instrumentation) {
        System.out.println("Citi Agent version: " + CITI_AGENT_VERSION);
        if (!Agent.loadLibraries()) {
            System.out.println("Could not load library files... not starting Citi...");
            return;
        }
        instrumentation.addTransformer(
                (loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> new TransformerTowns().transform(className, className, classfileBuffer)
        );
        instrumentation.addTransformer(
                (loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> new TransformerTownsMain().transform(className, className, classfileBuffer)
        );
    }

    private static boolean loadLibraries() {
        if (Citi.DIRECTORY_CITI_LIBS.exists()) {
            File[] files = Citi.DIRECTORY_CITI_LIBS.listFiles((dir, name) -> name.trim().toLowerCase().endsWith(".jar"));
            if (files != null) {
                Method addURL = null;
                if (Agent.class.getClassLoader() instanceof URLClassLoader) {
                    try {
                        addURL = URLClassLoader.class.getDeclaredMethod(
                                "addURL",

                                URL.class
                        );
                        addURL.setAccessible(true);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
                if (addURL == null) {
                    System.out.println("Could not load Citi's library files?!");
                    return false;
                }

                for (File file : files) {
                    try {
                        addURL.invoke(
                                Agent.class.getClassLoader(),

                                file.toURI().toURL()
                        );
                    } catch (IllegalAccessException | InvocationTargetException | MalformedURLException e) {
                        System.out.println("Caught exception while adding library file! Exception: " + e.toString());
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
