package com.hal.agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class HALTransformer
        implements ClassFileTransformer {

    @Override
    public byte[] transform(
            Module module,
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer) {

        if (className == null) {
            return null;
        }

        // Only instrument our test application
        if (!className.startsWith("com/hal/test/")) {
            return null;
        }

        try {

            ClassReader reader =
                    new ClassReader(classfileBuffer);

            ClassWriter writer =
                    new ClassWriter(
                            reader,
                            ClassWriter.COMPUTE_FRAMES
                    );

            ClassVisitor classVisitor =
                    new ClassVisitor(
                            Opcodes.ASM9,
                            writer) {

                        @Override
                        public MethodVisitor visitMethod(
                                int access,
                                String name,
                                String descriptor,
                                String signature,
                                String[] exceptions) {

                            MethodVisitor mv =
                                    super.visitMethod(
                                            access,
                                            name,
                                            descriptor,
                                            signature,
                                            exceptions
                                    );

                            return new AdviceAdapter(
                                    Opcodes.ASM9,
                                    mv,
                                    access,
                                    name,
                                    descriptor
                            ) {

                                @Override
                                protected void onMethodExit(
                                        int opcode) {

                                    if (opcode != ATHROW) {
                                        return;
                                    }

                                    // Stack currently contains:
                                    // Throwable

                                    dup();

                                    push(
                                            className.replace(
                                                    '/',
                                                    '.'
                                            )
                                    );

                                    push(name);

                                    invokeStatic(
                                            Type.getType(
                                                    HALExceptionReporter.class
                                            ),
                                            new org.objectweb.asm.commons.Method(
                                                    "report",
                                                    "(Ljava/lang/Throwable;Ljava/lang/String;Ljava/lang/String;)V"
                                            )
                                    );
                                }
                            };
                        }
                    };

            reader.accept(
                    classVisitor,
                    ClassReader.EXPAND_FRAMES
            );

            return writer.toByteArray();

        } catch (Exception e) {

            System.err.println(
                    "HAL instrumentation failed for "
                            + className
            );

            e.printStackTrace();

            return null;
        }
    }
}