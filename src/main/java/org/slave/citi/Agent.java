package org.slave.citi;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.logging.Logger;

/**
 * Created by Master801 on 3/18/2016 at 6:12 AM.
 *
 * @author Master801
 */
public final class Agent {

    public static void premain(final String agentArguments, final Instrumentation instrumentation) {
        Logger.getLogger("Citi").info("!!HELLO WORLD!!");

        instrumentation.addTransformer(
                new ClassFileTransformer() {

                    @Override
                    public byte[] transform(final ClassLoader loader, final String className, final Class<?> classBeingRedefined, final ProtectionDomain protectionDomain, final byte[] classfileBuffer) throws IllegalClassFormatException {
                        return new byte[0];
                    }

                }
        );
    }

}
