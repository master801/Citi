package org.slave.citi.loader;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.objectweb.asm.Type;
import org.slave.citi.api.Mod;
import org.slave.citi.api.asm.ASM;
import org.slave.lib.resources.ASMAnnotation;
import org.slave.lib.resources.ASMTable;
import org.slave.lib.resources.ASMTable.TableClass;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

/**
 * Created by Master on 5/16/19 at 3:48 PM
 *
 * @author Master
 */
@RequiredArgsConstructor
public final class CitiModCandidate {

    @Getter
    private final File file;

    @Getter(AccessLevel.PROTECTED)
    private ImmutableList<TableClass> modTableList;

    @Getter(AccessLevel.PROTECTED)
    private ImmutableList<TableClass> asmTableList;

    public void explore() throws IOException {
        ZipFile zipFile = new ZipFile(file);
        ASMTable asmTable = new ASMTable();
        asmTable.load(zipFile);
        zipFile.close();

        List<TableClass> modTableClasses = new ArrayList<>();
        List<TableClass> asmTableClasses = new ArrayList<>();
        for(TableClass tableClass : asmTable.getTableClasses()) {
            if (tableClass.getAnnotations() != null) {
                for(ASMAnnotation asmAnnotation : tableClass.getAnnotations()) {
                    if (asmAnnotation.getDesc().equals(Type.getDescriptor(Mod.class))) {
                        modTableClasses.add(tableClass);
                    } else if (asmAnnotation.getDesc().equals(Type.getDescriptor(ASM.class))) {
                        asmTableClasses.add(tableClass);
                    }
                }
            }
        }

        this.modTableList = ImmutableList.copyOf(modTableClasses);
        this.asmTableList = ImmutableList.copyOf(asmTableClasses);
    }

}
