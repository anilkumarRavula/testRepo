package com.asm.local;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;
import org.objectweb.asm.commons.Method;

import java.util.ArrayList;

public class PrivateMethodsCallAnalysis {

    static String findPublicMethods = "apache.fineract.cn.portfolio.service.internal.service.LoanAccountService,public org.apache.fineract.cn.portfolio.api.v1.domain.LoanAccount org.apache.fineract.cn.portfolio.service.internal.service.LoanAccountService.getLoanAccountDetails(java.lang.Long)";

    private String targetClass;
    private Method targetMethod;

    private AppClassVisitor cv;

    private ArrayList<Callee> callees = new ArrayList<Callee>();

    public PrivateMethodsCallAnalysis(String targetClass, String targetMethod) {
        this.targetClass = targetClass;
        this.targetMethod = Method.getMethod(targetMethod);
        this.cv = new AppClassVisitor();
    }
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
            System.out.println(name+desc);
            return mv;
        }
    }


    public void findCallingMethodsInJar(String jarPath) throws Exception {
        ClassReader reader = new ClassReader("com.asm.local.PrivateMethodAnalysis");
        reader.accept(cv, 0);
    }

    public static void main(String[] args) throws Exception {
        analyze(findPublicMethods);
    }
    public static  void analyze(String name) throws Exception {
        String[] methodAndClass = name.split(",");
        String className = methodAndClass[0];
        String method = methodAndClass[1];
        PrivateMethodsCallAnalysis analyzer = new PrivateMethodsCallAnalysis(className,method);
        analyzer.findCallingMethodsInJar("");
    }









}
