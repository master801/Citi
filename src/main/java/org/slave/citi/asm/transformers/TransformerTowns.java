package org.slave.citi.asm.transformers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.slave.citi.Citi;
import org.slave.citi.asm.Transformer;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 *     Replaces Towns' current running classloader with ours
 * </p>
 *
 * Created by Master on 5/17/19 at 6:49 AM
 *
 * @author Master
 */
public final class TransformerTowns implements Transformer {

    @Override
    public byte[] transform(final String className, final String transformedClassName, final byte[] original) {
        if (className.equals("xaos/Towns")) {
            ClassReader classReader = new ClassReader(original);

            ClassNode classNode = new ClassNode();
            classReader.accept(
                    new ClassVisitor(Opcodes.ASM5, classNode) {

                        @Override
                        public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
                            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

                            //Replace classloader
                            if (access == (Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC) && name.equals("main") && desc.equals("([Ljava/lang/String;)V")) {
                                return new MethodVisitor(super.api, mv) {

                                    @Override
                                    public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
                                        super.visitTryCatchBlock(start, end, handler, type);

                                        if (type.equals("java/lang/Exception")) {
                                            super.visitLabel(new Label());
                                            super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/slave/citi/loader/CitiClassLoader", "replaceCurrentClassLoader", "()V", false);
                                            super.visitLabel(new Label());
                                        }
                                    }

                                };
                            }
                            return mv;
                        }
                    },
                    0
            );

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);

            byte[] newClassData = classWriter.toByteArray();
            if (Citi.DEBUG) {
                try {
                    Transformer.writeClassFile(new File("asm/classes"), className, original);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return newClassData;
        }
        return original;
    }

}
