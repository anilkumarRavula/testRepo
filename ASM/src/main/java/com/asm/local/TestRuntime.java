package com.asm.local;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;


public class TestRuntime {
    static String className = "com.asm.local.PrivateMethodAnalysis";
    static String cloneableInterface = "java/lang/Cloneable";
    static ClassReader reader;
    static ClassWriter writer;

    public static void main(String[] args) throws IOException {
        ClassPrinter cp = new ClassPrinter();
        ClassReader cr = new ClassReader(className);
        cr.accept(cp, 0);
        try (Stream<String> stream = Files.lines(Paths.get("C:\\anil\\codesetup\\ASM\\src\\main\\java\\com\\asm\\local\\PrivateMethodAnalysis.java"))) {
            AtomicInteger atomicInteger = new AtomicInteger();
            stream.forEach(line -> System.out.println("line "+ atomicInteger.incrementAndGet()+  "  "+line ));
        }
    }
    public  static class ClassPrinter implements ClassVisitor {
        private int line;

        public ClassPrinter() {
        }
        public void visit(int version, int access, String name,
                          String signature, String superName, String[] interfaces) {
            System.out.println(name + " extends " + superName + " {");
        }
        public void visitSource(String source, String debug) {
        }
        public void visitOuterClass(String owner, String name, String desc) {
        }
        public AnnotationVisitor visitAnnotation(String desc,
                                                 boolean visible) {
            return null;
        }
        public void visitAttribute(Attribute attr) {
        }
        public void visitInnerClass(String name, String outerName,
                                    String innerName, int access) {
        }
        public FieldVisitor visitField(int access, String name, String desc,
                                       String signature, Object value) {
            System.out.println(" " + desc + " " + name);
            return null;
        }
        public MethodVisitor visitMethod(int access, String name,
                                         String desc, String signature, String[] exceptions) {

            System.out.println(" " + name + desc);
            return null;
        }

        public void visitEnd() {
            System.out.println("}");
        }
    }
}
