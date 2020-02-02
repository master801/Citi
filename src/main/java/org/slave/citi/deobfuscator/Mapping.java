package org.slave.citi.deobfuscator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slave.citi.Citi;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.resources.Obfuscation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Master on 5/17/19 at 9:57 AM
 *
 * @author Master
 */
public final class Mapping {

    public static final String MAPPING_FILE_VERSION = "14e";
    public static final String MAPPING_FILE_NAME = String.format("Towns_%s.mapping", MAPPING_FILE_VERSION);

    @Getter
    private ImmutableList<MappingEntryClass> mappingEntryList;

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

        Map<Type, List<MappingEntry>> cache = Maps.newEnumMap(Type.class);
        Arrays.stream(Type.values()).forEach(type -> cache.put(type, Lists.newArrayList()));

        List<MappingEntryClass> mappingEntryClassList = new ArrayList<>();
        for(String line : lines) {
            String newLine;
            if (line.startsWith("\t")) {
                newLine = line.substring(line.lastIndexOf('\t') + 1);
            } else {
                newLine = line;
            }

            String[] parts = newLine.split(" ");
            if (newLine.startsWith(Type.CLASS.id)) {
                if (cache.get(Type.CLASS).size() == 1) {
                    MappingEntryClass mappingEntryClass = (MappingEntryClass)cache.get(Type.CLASS).get(0);
                    for (MappingEntry e : cache.get(Type.FIELD)) mappingEntryClass.fields.add((MappingEntryClass.MappingEntryField) e);
                    for (MappingEntry e : cache.get(Type.METHOD)) mappingEntryClass.methods.add((MappingEntryClass.MappingEntryMethod) e);
                    mappingEntryClassList.add(mappingEntryClass);
                    Arrays.stream(Type.values()).forEach(type -> cache.get(type).clear());
                }
                cache.get(Type.CLASS).add(parseMappingEntry(Type.CLASS, parts));
            } else if (line.startsWith("\t")) {
                Type type = Type.from(parts[0]);
                if (type == null) {
                    Citi.LOGGER_CITI.severe("");
                    continue;
                }
                MappingEntry mappingEntry = parseMappingEntry(type, parts);
                if (mappingEntry == null) {
                    Citi.LOGGER_CITI.severe("");
                    continue;
                }
                if (type == Type.ARG) {
                    MappingEntryClass.MappingEntryMethod mappingEntryMethod = (MappingEntryClass.MappingEntryMethod)cache.get(Type.METHOD).get(cache.get(Type.METHOD).size() - 1);
                    mappingEntryMethod.args.add((MappingEntryClass.MappingEntryMethod.MappingEntryMethodArg)mappingEntry);
                } else {
                    cache.get(type).add(mappingEntry);
                }
            }
        }

        MappingEntryClass mappingEntryClass = (MappingEntryClass)cache.get(Type.CLASS).get(0);
        for (MappingEntry e : cache.get(Type.FIELD)) mappingEntryClass.fields.add((MappingEntryClass.MappingEntryField) e);
        for (MappingEntry e : cache.get(Type.METHOD)) mappingEntryClass.methods.add((MappingEntryClass.MappingEntryMethod) e);
        mappingEntryClassList.add(mappingEntryClass);//Last cached class
        Arrays.stream(Type.values()).forEach(type -> cache.get(type).clear());

        mappingEntryList = ImmutableList.<MappingEntryClass>builder().addAll(mappingEntryClassList).build();
    }

    private MappingEntry parseMappingEntry(final Type type, final String[] parts) {
        if (type == null || ArrayHelper.isNullOrEmpty(parts)) return null;
        switch(type) {
            case CLASS:
                if (parts.length == 2 || parts.length == 3) {
                    EnumMap<Obfuscation, String> map = Maps.newEnumMap(Obfuscation.class);
                    map.put(Obfuscation.OBFUSCATED, parts[1]);
                    map.put(Obfuscation.DEOBFUSCATED, parts.length == 3 ? parts[2] : null);
                    return new MappingEntryClass(map);
                }
            case FIELD:
                if (parts.length == 4) {
                    EnumMap<Obfuscation, String> map = Maps.newEnumMap(Obfuscation.class);
                    map.put(Obfuscation.OBFUSCATED, parts[1]);
                    map.put(Obfuscation.DEOBFUSCATED, parts[2]);
                    return new MappingEntryClass.MappingEntryField(map, parts[3]);
                }
                break;
            case METHOD:
                if (parts.length == 3 || parts.length == 4) {
                    EnumMap<Obfuscation, String> map = Maps.newEnumMap(Obfuscation.class);
                    map.put(Obfuscation.OBFUSCATED, parts[1]);
                    map.put(Obfuscation.DEOBFUSCATED, parts.length == 4 ? parts[2] : null);
                    return new MappingEntryClass.MappingEntryMethod(map, parts.length == 4 ? parts[3] : parts[2]);
                }
                break;
            case ARG:
                if (parts.length == 3) {
                    return new MappingEntryClass.MappingEntryMethod.MappingEntryMethodArg(Integer.parseInt(parts[1]), parts[2]);
                }
                break;
        }
        return null;
    }

    interface MappingEntry {
    }

    @RequiredArgsConstructor
    static final class MappingEntryClass implements MappingEntry {

        @Getter
        private final EnumMap<Obfuscation, String> names;

        @Getter
        private final List<MappingEntryField> fields = new ArrayList<>();

        @Getter
        private final List<MappingEntryMethod> methods = new ArrayList<>();

        @RequiredArgsConstructor
        static final class MappingEntryField implements MappingEntry {

            @Getter
            private final EnumMap<Obfuscation, String> names;

            @Getter
            private final String desc;

        }

        @RequiredArgsConstructor
        static final class MappingEntryMethod implements MappingEntry {

            @Getter
            private final EnumMap<Obfuscation, String> names;

            @Getter
            private final String desc;

            @Getter
            private final List<MappingEntryMethodArg> args = new ArrayList<>();

            @RequiredArgsConstructor
            static final class MappingEntryMethodArg implements MappingEntry {

                @Getter
                private final int varIndex;

                @Getter
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
