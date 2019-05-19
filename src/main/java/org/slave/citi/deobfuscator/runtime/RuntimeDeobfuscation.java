package org.slave.citi.deobfuscator.runtime;

import org.slave.citi.Citi;
import org.slave.citi.deobfuscator.Mapping;

import java.io.File;
import java.io.IOException;

/**
 * Created by Master on 5/17/19 at 6:37 AM
 *
 * @author Master
 */
public final class RuntimeDeobfuscation {

    public void deobfuscate() {
        //TODO
        Mapping mapping = new Mapping();
        try {
            mapping.loadFromPath(new File(Citi.DIRECTORY_CITI, Mapping.MAPPING_FILE_NAME));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
