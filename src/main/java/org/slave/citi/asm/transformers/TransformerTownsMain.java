package org.slave.citi.asm.transformers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.slave.citi.Citi;
import org.slave.citi.api.asm.Transformer;

import java.io.File;
import java.io.IOException;

/**
 * Created by Master on 5/17/19 at 8:02 AM
 *
 * @author Master
 */
public final class TransformerTownsMain implements Transformer {

    @Override
    public byte[] transform(final String className, final String transformedClassName, final byte[] original) {
        if (className.equals("xaos/main/a")) {
            ClassReader classReader = new ClassReader(original);

            ClassNode classNode = new ClassNode();
            classReader.accept(
                    new ClassVisitor(Opcodes.ASM5, classNode) {

                        @Override
                        public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
                            MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

                            //Load mods and fire stages
                            if (access == Opcodes.ACC_PUBLIC && name.equals("<init>") && desc.equals("()V")) {
                                return new MethodVisitor(super.api, mv) {

                                    @Override
                                    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
                                        super.visitFieldInsn(opcode, owner, name, desc);

                                        if (opcode == Opcodes.PUTSTATIC && owner.equals(classNode.name) && name.equals("d") && desc.equals("I")) {
                                            //Fire preInitialization stage for mods
                                            super.visitLabel(new Label());
                                            super.visitFieldInsn(Opcodes.GETSTATIC, "org/slave/citi/loader/CitiLoader", "INSTANCE", "Lorg/slave/citi/loader/CitiLoader;");
                                            super.visitTypeInsn(Opcodes.NEW, "org/slave/citi/api/stage/StagePreInitialization");
                                            super.visitInsn(Opcodes.DUP);
                                            super.visitVarInsn(Opcodes.ALOAD, 0);
                                            super.visitMethodInsn(Opcodes.INVOKESPECIAL, "org/slave/citi/api/stage/StagePreInitialization", "<init>", "(Lxaos/main/a;)V", false);
                                            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/slave/citi/loader/CitiLoader", "fireStage", "(Lorg/slave/citi/api/stage/Stage;)V", false);
                                        }
                                    }

                                    @Override
                                    public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                                        super.visitMethodInsn(opcode, owner, name, desc, itf);

                                        if (opcode == Opcodes.INVOKESTATIC && owner.equals("xaos/actions/a") && name.equals("a") && desc.equals("(II)V")) {
                                            //Load mods
                                            super.visitLabel(new Label());
                                            super.visitFieldInsn(Opcodes.GETSTATIC, "org/slave/citi/loader/CitiLoader", "INSTANCE", "Lorg/slave/citi/loader/CitiLoader;");
                                            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/slave/citi/loader/CitiLoader", "loadMods", "()V", false);

                                            //Fire initialization stage for mods
                                            super.visitLabel(new Label());
                                            super.visitFieldInsn(Opcodes.GETSTATIC, "org/slave/citi/loader/CitiLoader", "INSTANCE", "Lorg/slave/citi/loader/CitiLoader;");
                                            super.visitTypeInsn(Opcodes.NEW, "org/slave/citi/api/stage/StageInitialization");
                                            super.visitInsn(Opcodes.DUP);
                                            super.visitVarInsn(Opcodes.ALOAD, 0);
                                            super.visitMethodInsn(Opcodes.INVOKESPECIAL, "org/slave/citi/api/stage/StageInitialization", "<init>", "(Lxaos/main/a;)V", false);
                                            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/slave/citi/loader/CitiLoader", "fireStage", "(Lorg/slave/citi/api/stage/Stage;)V", false);
                                        } else if (opcode == Opcodes.INVOKEVIRTUAL && owner.equals("xaos/panels/c") && name.equals("b") && desc.equals("(Z)V")) {
                                            //Fire postInitialization stage for mods
                                            super.visitLabel(new Label());
                                            super.visitFieldInsn(Opcodes.GETSTATIC, "org/slave/citi/loader/CitiLoader", "INSTANCE", "Lorg/slave/citi/loader/CitiLoader;");
                                            super.visitTypeInsn(Opcodes.NEW, "org/slave/citi/api/stage/StagePostInitialization");
                                            super.visitInsn(Opcodes.DUP);
                                            super.visitVarInsn(Opcodes.ALOAD, 0);
                                            super.visitMethodInsn(Opcodes.INVOKESPECIAL, "org/slave/citi/api/stage/StagePostInitialization", "<init>", "(Lxaos/main/a;)V", false);
                                            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/slave/citi/loader/CitiLoader", "fireStage", "(Lorg/slave/citi/api/stage/Stage;)V", false);
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
