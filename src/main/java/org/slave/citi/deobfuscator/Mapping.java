package org.slave.citi.deobfuscator;

import lombok.RequiredArgsConstructor;
import org.slave.citi.Citi;
import org.slave.citi.deobfuscator.Mapping.MappingEntryClass.MappingEntryField;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 5/17/19 at 9:57 AM
 *
 * @author Master
 */
public final class Mapping {

    public static final String MAPPING_FILE_VERSION = "14e";
    public static final String MAPPING_FILE_NAME = String.format("Towns_%s.mapping", MAPPING_FILE_VERSION);

    public void loadFromPath(final File filePath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        try {
            parse(bufferedReader);
        } catch(IOException e) {
            Citi.LOGGER_CITI.info("Caught exception while parsing mapping file!");
            Citi.LOGGER_CITI.info("Exception: " + e.toString());
        }

        bufferedReader.close();
        inputStreamReader.close();
        fileInputStream.close();
    }

    private void parse(final BufferedReader bufferedReader) throws IOException {
        List<String> lines = new ArrayList<>();

        String iteratingLine;
        while((iteratingLine = bufferedReader.readLine()) != null) lines.add(iteratingLine);

        List<MappingEntryClass> mappingEntryClassList = new ArrayList<>();
        int toNext = -1;
        for(int i = 0; i < lines.size(); ++i) {
            String line = lines.get(i);

            int indent = 0;
            if (line.startsWith("\t")) indent = line.lastIndexOf('\t');

            boolean isChild = indent > 1;

            String[] parts = line.split(" ");
            Type type = Type.from(isChild ? parts[0].substring(indent + 1) : parts[0]);

            String[] names = null;
            if (type == Type.CLASS) {
                names = new String[2];
                System.arraycopy(parts, 1, names, 0, 2);

                int next = i + 1;
                MappingEntryClass mappingEntryClass = new MappingEntryClass(names);

                String nextLine;
                while((nextLine = lines.get(next)) != null) {
                    next += 1;

                    String[] nextParts = nextLine.split(" ");

                    String nextTypeName = nextParts[0];
                    if (nextTypeName.startsWith("\t")) nextTypeName = nextTypeName.substring(nextTypeName.lastIndexOf('\t') + 1);
                    Type nextType = Type.from(nextTypeName);

                    if (nextType == Type.CLASS) {//Ignore class
                        break;
                    }

                    if (nextType == Type.FIELD) {
                        mappingEntryClass.fields.add(
                                new MappingEntryField(null, null)
                        );
                    } else if (nextType == Type.METHOD) {
                    }

                    Citi.LOGGER_CITI.info("");
                }
            }
            Citi.LOGGER_CITI.info("");
        }
    }

    @RequiredArgsConstructor
    static final class MappingEntryClass {

        private final String[] names;

        private final List<MappingEntryField> fields = new ArrayList<>();
        private final List<MappingEntryMethod> methods = new ArrayList<>();

        @RequiredArgsConstructor
        static final class MappingEntryField {

            private final String[] names;
            private final String desc;

        }

        @RequiredArgsConstructor
        static final class MappingEntryMethod {

            private final String[] names;
            private final String desc;
            private final List<MappingEntryMethodArg> args = new ArrayList<>();

            @RequiredArgsConstructor
            static final class MappingEntryMethodArg {

                private final int varIndex;
                private final String name;

            }

        }

    }

    @RequiredArgsConstructor
    private enum Type {

        CLASS("CLASS", true, false),

        FIELD("FIELD", false, true),

        METHOD("METHOD", true, true),

        ARG("ARG", false, true);

        private final String id;
        private final boolean isParent, isChild;

        private static Type from(final String id) {
            for(Type type : Type.values()) {
                if (type.id.equals(id)) return type;
            }
            return null;
        }

    }

}
