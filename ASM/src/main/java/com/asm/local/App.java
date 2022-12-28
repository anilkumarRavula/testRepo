package com.asm.local;


import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.commons.Method;

import java.util.ArrayList;

public class App {
    private String targetClass;
    private Method targetMethod;

    private AppClassVisitor cv;

    private ArrayList<Callee> callees = new ArrayList<Callee>();

    private static class Callee {
        String className;
        String methodName;
        String methodDesc;
        String source;
        int line;

        public Callee(String cName, String mName, String mDesc, String src, int ln) {
            className = cName; methodName = mName; methodDesc = mDesc; source = src; line = ln;
        }

        @Override
        public String toString() {
            return "Callee{" +
                    "className='" + className + '\'' +
                    ", methodName='" + methodName + '\'' +
                    ", methodDesc='" + methodDesc + '\'' +
                    ", source='" + source + '\'' +
                    ", line=" + line +
                    '}';
        }
    }

    private class AppMethodVisitor extends MethodAdapter {

        boolean callsTarget;
        int line;

        public AppMethodVisitor() { super(new EmptyVisitor()); }

        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
            System.out.println("==methods called -->"+owner+ "--"+opcode+"=="+name+desc);
            if (owner.equals(targetClass)
                    && name.equals(targetMethod.getName())
                    && desc.equals(targetMethod.getDescriptor())) {
                System.out.println("===matched");
                callsTarget = true;
            }
        }

        public void visitCode() {
            callsTarget = false;
        }

        public void visitLineNumber(int line, Label start) {
            this.line = line;
        }

        public void visitEnd() {
            if (callsTarget)
                callees.add(new Callee(cv.className, cv.methodName, cv.methodDesc,
                        cv.source, line));
        }
    }

    private class AppClassVisitor extends ClassAdapter {

        private AppMethodVisitor mv = new AppMethodVisitor();

        public String source;
        public String className;
        public String methodName;
        public String methodDesc;

        public AppClassVisitor() { super(new EmptyVisitor()); }

        public void visit(int version, int access, String name,
                          String signature, String superName, String[] interfaces) {
            className = name;
        }

        public void visitSource(String source, String debug) {
            this.source = source;
        }

        public MethodVisitor visitMethod(int access, String name,
                                         String desc, String signature,
                                         String[] exceptions) {
            methodName = name;
            methodDesc = desc;
            System.out.println(access+" "+name+desc);
            return mv;
        }
    }


    public void findCallingMethodsInJar(String jarPath, String targetClass,
                                        String targetMethodDeclaration) throws Exception {

        this.targetClass = targetClass;
        this.targetMethod = Method.getMethod(targetMethodDeclaration);

        this.cv = new AppClassVisitor();

        ClassReader reader = new ClassReader("com.asm.local.PrivateMethodAnalysis");

        reader.accept(cv, 0);

       // JarFile jarFile = new JarFile(jarPath);
       // Enumeration<JarEntry> entries = jarFile.entries();

        /*while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();

            if (entry.getName().endsWith(".class")) {
                InputStream stream = new BufferedInputStream(jarFile.getInputStream(entry), 1024);
                ClassReader reader = new ClassReader(stream);

                reader.accept(cv, 0);

                stream.close();
            }
        }*/
    }


    public static void main( String[] args ) {
        try {
            App app = new App();

            app.findCallingMethodsInJar("args[0]", "com/asm/local/PrivateMethodAnalysis", "com.asm.local.PrivateMethodAnalysis com.asm.local.PrivateMethodAnalysis.c()");

            for (Callee c : app.callees) {
                System.out.println(c.source+":"+c.line+" "+c.className+" "+c.methodName+" "+c.methodDesc);
                System.out.println(c);
            }

            System.out.println("--\n"+app.callees.size()+" methods invoke "+
                    app.targetClass+" "+
                    app.targetMethod.getName()+" "+app.targetMethod.getDescriptor());
        } catch(Exception x) {
            x.printStackTrace();
        }
    }

}
