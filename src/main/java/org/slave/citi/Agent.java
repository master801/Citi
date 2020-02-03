package org.slave.citi;

import org.slave.citi.asm.transformers.TransformerPanelMainMenu;
import org.slave.citi.asm.transformers.TransformerTowns;
import org.slave.citi.asm.transformers.TransformerTownsMain;
import org.slave.citi.deobfuscator.runtime.RuntimeDeobfuscation;
import org.slave.citi.loader.asm.CitiASMLoader;
import org.slave.lib.api.asm.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;

/**
 * Created by Master801 on 3/18/2016 at 6:12 AM.
 *
 * @author Master801
 */
public final class Agent {

    private static final String CITI_AGENT_VERSION = "v0.0.1-Apple";

    private static final Class<? extends Transformer>[] CLASSES_TRANSFORMER = new Class[] {
            TransformerTowns.class,
            TransformerTownsMain.class,

            TransformerPanelMainMenu.class
    };

    public static final Logger LOGGER_CITI_AGENT = LoggerFactory.getLogger("Citi-Agent");

    public static void premain(final String agentArguments, final Instrumentation instrumentation) {
        Agent.LOGGER_CITI_AGENT.info("Citi Agent version: {}", CITI_AGENT_VERSION);
        Citi.init();
        if (!Agent.loadLibraries()) {
            System.out.println("Could not load library files... not starting Citi...");
            return;
        }

        //Runtime deobfuscation
        RuntimeDeobfuscation runtimeDeobfuscation = new RuntimeDeobfuscation();
        runtimeDeobfuscation.deobfuscate();//TODO

        //Transformers
        for(Class<? extends Transformer> classTransformer : CLASSES_TRANSFORMER) {
            try {
                Constructor<? extends Transformer> constructor = classTransformer.getConstructor();
                Transformer transformer = constructor.newInstance();

                instrumentation.addTransformer(
                        (loader, className, classBeingRedefined, protectionDomain, classfileBuffer) -> transformer.transform(classfileBuffer, className, className),
                        true
                );
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                Agent.LOGGER_CITI_AGENT.info("Caught exception while adding transformer!");
                Agent.LOGGER_CITI_AGENT.info("Exception: " + e.toString());
            }
        }

        try {
            CitiASMLoader.INSTANCE.loadTransformers(instrumentation);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            CitiASMLoader.LOGGER_CITI_ASM.log(Level.SEVERE, "Failed to load transformer due to caught exception!", e);
        }
        CitiASMLoader.INSTANCE.sortTransformers();
    }

    private static boolean loadLibraries() {
        if (Citi.getDirectoryCitiLibs().exists()) {
            File[] files = Citi.getDirectoryCitiLibs().listFiles((dir, name) -> name.trim().toLowerCase().endsWith(".jar"));
            if (files != null && files.length > 0) {
                Method addURL = null;
                if (Agent.class.getClassLoader() instanceof URLClassLoader) {
                    try {
                        addURL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
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
                        addURL.invoke(Agent.class.getClassLoader(), file.toURI().toURL());
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
