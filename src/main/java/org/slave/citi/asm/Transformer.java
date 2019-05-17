package org.slave.citi.asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Master on 5/16/19 at 3:58 PM
 *
 * @author Master
 */
public interface Transformer {

    /**
     * @param className The class' name. Always obfuscated
     * @param transformedClassName The class' name. Deobfuscated if mapping is available
     * @param original Original class data
     * @return
     */
    byte[] transform(final String className, final String transformedClassName, final byte[] original);

    static void writeClassFile(final File outDir, final String className, final byte[] classData) throws IOException {
        String newClassName = null;
        if (!className.contains(".class")) newClassName = className + ".class";
        Transformer.writeClassFile(new File(outDir, newClassName != null ? newClassName : className), classData);
    }

    static void writeClassFile(final File outFile, final byte[] classData) throws IOException {
        if (!outFile.getParentFile().exists()) outFile.getParentFile().mkdirs();

        FileOutputStream fileOutputStream = new FileOutputStream(outFile);

        fileOutputStream.write(classData);

        fileOutputStream.flush();
        fileOutputStream.close();
    }

}
