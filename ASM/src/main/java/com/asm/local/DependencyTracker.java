//package com.asm.local;
//
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.Graphics2D;
//import java.awt.RenderingHints;
//import java.awt.geom.AffineTransform;
//import java.awt.image.BufferedImage;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Enumeration;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipFile;
//
//import javax.imageio.ImageIO;
//
//import jdk.internal.org.objectweb.asm.Handle;
//import org.objectweb.asm.ClassReader;
//
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//
//import org.objectweb.asm.AnnotationVisitor;
//import org.objectweb.asm.ClassVisitor;
//import org.objectweb.asm.FieldVisitor;
//import org.objectweb.asm.Label;
//import org.objectweb.asm.MethodVisitor;
//import org.objectweb.asm.Opcodes;
//import org.objectweb.asm.Type;
//import org.objectweb.asm.signature.SignatureReader;
//import org.objectweb.asm.signature.SignatureVisitor;
//public class DependencyTracker {
//
//    private static final int CELL_PAD = 1;
//
//    private static final int GRID_SIZE = 10;
//
//    private static final int CELLS_SIZE = 8;
//
//    private static final int LABEL_WIDTH = 200;
//
//    private static final String LABEL_FONT = "Tahoma-9";
//
//    public static void main(final String[] args) throws IOException {
//        DependencyVisitor v = new DependencyVisitor();
//
//        ZipFile f = new ZipFile(args[0]);
//
//        long l1 = System.currentTimeMillis();
//        Enumeration< ? extends ZipEntry> en = f.entries();
//        while (en.hasMoreElements()) {
//            ZipEntry e = en.nextElement();
//            String name = e.getName();
//            if (name.endsWith(".class") && name.contains("TransactionSearchAggregator")) {
//                System.out.println(name);
//                new ClassReader(f.getInputStream(e)).accept(v, 0);
//            }
//        }
//        long l2 = System.currentTimeMillis();
//
//        Map<String, Map<String, Integer>> globals = v.getGlobals();
//        Set<String> jarPackages = globals.keySet();
//        Set<String> classPackages = v.getPackages();
//        int size = classPackages.size();
//        System.err.println("time: " + (l2 - l1) / 1000f + "  " + size);
//
//        String[] jarNames = jarPackages.toArray(new String[jarPackages.size()]);
//        String[] classNames = classPackages.toArray(new String[classPackages.size()]);
//        Arrays.sort(jarNames);
//        Arrays.sort(classNames);
//
//        buildDiagram(jarNames, classNames, globals);
//    }
//
//    public static void buildDiagram(
//            final String[] jarNames,
//            final String[] classNames,
//            final Map<String, Map<String, Integer>> globals) throws IOException
//    {
//        // normalize
//        int max = 0;
//        for (int i = 0; i < classNames.length; i++) {
//            Map<String, Integer> map = globals.get(classNames[i]);
//            if (map == null) {
//                continue;
//            }
//            Integer maxCount = Collections.max(map.values());
//            if (maxCount > max) {
//                max = maxCount;
//            }
//        }
//
//        List<Color> colors = new ArrayList<Color>();
//        for (int i = LABEL_WIDTH; i >= 0; i--) {
//            colors.add(new Color(i, i, 255));
//        }
//        for (int i = 255; i >= 128; i--) {
//            colors.add(new Color(0, 0, i));
//        }
//        int maxcolor = colors.size() - 1;
//
//        int heigh = CELL_PAD + (CELLS_SIZE + CELL_PAD) * classNames.length;
//        int width = CELL_PAD + (CELLS_SIZE + CELL_PAD) * jarNames.length;
//
//        BufferedImage img = new BufferedImage(width + LABEL_WIDTH, heigh
//                + LABEL_WIDTH, BufferedImage.TYPE_INT_RGB);
//
//        Graphics2D g = img.createGraphics();
//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                RenderingHints.VALUE_ANTIALIAS_ON);
//
//        g.setColor(Color.WHITE);
//        g.fillRect(0, 0, width + LABEL_WIDTH, heigh + LABEL_WIDTH);
//
//        // draw lines
//        g.setColor(Color.LIGHT_GRAY);
//        for (int y = GRID_SIZE; y < classNames.length; y += GRID_SIZE) {
//            g.drawLine(0, y * (CELLS_SIZE + CELL_PAD), width, y
//                    * (CELLS_SIZE + CELL_PAD));
//        }
//        for (int x = GRID_SIZE; x < jarNames.length; x += GRID_SIZE) {
//            g.drawLine(x * (CELLS_SIZE + CELL_PAD), 0, x
//                    * (CELLS_SIZE + CELL_PAD), heigh);
//        }
//
//        // draw diagram
//        for (int y = 0; y < classNames.length; y++) {
//            // System.err.println( y+" : "+classNames[ y]);
//
//            for (int x = 0; x < jarNames.length; x++) {
//                Map<String, Integer> map = globals.get(jarNames[x]);
//
//                Integer count = map == null ? null : map.get(classNames[y]);
//                if (count != null) {
//                    int b = (int) ((float) count * maxcolor / max);
//
//                    g.setColor(colors.get(b));
//                    g.fillRect(CELL_PAD + x * (CELLS_SIZE + CELL_PAD),
//                            CELL_PAD + y * (CELLS_SIZE + CELL_PAD),
//                            CELLS_SIZE,
//                            CELLS_SIZE);
//                }
//            }
//        }
//
//        // draw labels
//        Font f = Font.decode(LABEL_FONT);
//        g.setFont(f);
//        // g.setColor( new Color( 70, 70, 255));
//        g.setColor(Color.GRAY);
//
//        for (int y = 0; y < classNames.length; y++) {
//            AffineTransform trans = g.getTransform();
//            g.transform(AffineTransform.getTranslateInstance(CELL_PAD * 2
//                    + width, CELLS_SIZE + y * (CELLS_SIZE + CELL_PAD)));
//            g.transform(AffineTransform.getRotateInstance(Math.PI / 12));
//            g.drawString(classNames[y], 0, 0);
//            g.setTransform(trans);
//        }
//
//        for (int x = 0; x < jarNames.length; x++) {
//            AffineTransform trans = g.getTransform();
//            g.transform(AffineTransform.getTranslateInstance(CELL_PAD * 2 + x
//                    * (CELLS_SIZE + CELL_PAD), heigh + CELL_PAD * 2));
//            g.transform(AffineTransform.getRotateInstance(Math.PI / 2.5));
//            g.drawString(jarNames[x], 0, 0);
//            g.setTransform(trans);
//        }
//
//        FileOutputStream fos = new FileOutputStream("test.png");
//        ImageIO.write(img, "png", fos);
//        fos.flush();
//        fos.close();
//    }
//
//    public static class DependencyVisitor implements ClassVisitor {
//        Set<String> packages = new HashSet<String>();
//
//        Map<String, Map<String, Integer>> groups = new HashMap<String, Map<String, Integer>>();
//
//        Map<String, Integer> current;
//
//        public Map<String, Map<String, Integer>> getGlobals() {
//            return groups;
//        }
//
//        public Set<String> getPackages() {
//            return packages;
//        }
//
//        public DependencyVisitor() {
//
//        }
//
//        // ClassVisitor
//
//        @Override
//        public void visit(
//                final int version,
//                final int access,
//                final String name,
//                final String signature,
//                final String superName,
//                final String[] interfaces)
//        {
//            String p = getGroupKey(name);
//            current = groups.get(p);
//            if (current == null) {
//                current = new HashMap<String, Integer>();
//                groups.put(p, current);
//            }
//
//            if (signature == null) {
//                if (superName != null) {
//                    addInternalName(superName);
//                }
//                addInternalNames(interfaces);
//            } else {
//                addSignature(signature);
//            }
//        }
//
//        @Override
//        public AnnotationVisitor visitAnnotation(
//                final String desc,
//                final boolean visible)
//        {
//            addDesc(desc);
//            return new AnnotationDependencyVisitor();
//        }
//
//        @Override
//        public FieldVisitor visitField(
//                final int access,
//                final String name,
//                final String desc,
//                final String signature,
//                final Object value)
//        {
//            if (signature == null) {
//                addDesc(desc);
//            } else {
//                addTypeSignature(signature);
//            }
//            if (value instanceof Type) {
//                addType((Type) value);
//            }
//            return new FieldDependencyVisitor();
//        }
//
//        @Override
//        public MethodVisitor visitMethod(
//                final int access,
//                final String name,
//                final String desc,
//                final String signature,
//                final String[] exceptions)
//        {
//            if (signature == null) {
//                addMethodDesc(desc);
//            } else {
//                addSignature(signature);
//            }
//            addInternalNames(exceptions);
//            return new MethodDependencyVisitor();
//        }
//
//        class AnnotationDependencyVisitor implements AnnotationVisitor {
//
//            public AnnotationDependencyVisitor() {
//
//            }
//
//            @Override
//            public void visit(final String name, final Object value) {
//                if (value instanceof Type) {
//                    addType((Type) value);
//                }
//            }
//
//            @Override
//            public void visitEnum(
//                    final String name,
//                    final String desc,
//                    final String value)
//            {
//                addDesc(desc);
//            }
//
//            @Override
//            public AnnotationVisitor visitAnnotation(
//                    final String name,
//                    final String desc)
//            {
//                addDesc(desc);
//                return this;
//            }
//
//            @Override
//            public AnnotationVisitor visitArray(final String name) {
//                return this;
//            }
//
//            @Override
//            public void visitEnd() {
//
//            }
//        }
//
//        class FieldDependencyVisitor implements FieldVisitor {
//
//            public FieldDependencyVisitor() {
//                super(Opcodes.ASM5);
//            }
//
//            @Override
//            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
//                addDesc(desc);
//                return new AnnotationDependencyVisitor();
//            }
//        }
//
//        class MethodDependencyVisitor implements MethodVisitor {
//
//            public MethodDependencyVisitor() {
//                super(Opcodes.ASM5);
//            }
//
//            @Override
//            public AnnotationVisitor visitAnnotationDefault() {
//                return new AnnotationDependencyVisitor();
//            }
//
//            @Override
//            public AnnotationVisitor visitAnnotation(
//                    final String desc,
//                    final boolean visible)
//            {
//                addDesc(desc);
//                return new AnnotationDependencyVisitor();
//            }
//
//            @Override
//            public AnnotationVisitor visitParameterAnnotation(
//                    final int parameter,
//                    final String desc,
//                    final boolean visible)
//            {
//                addDesc(desc);
//                return new AnnotationDependencyVisitor();
//            }
//
//            @Override
//            public void visitTypeInsn(final int opcode, final String type) {
//                addType(Type.getObjectType(type));
//            }
//
//            @Override
//            public void visitFieldInsn(
//                    final int opcode,
//                    final String owner,
//                    final String name,
//                    final String desc)
//            {
//                addInternalName(owner);
//                addDesc(desc);
//            }
//
//            @Override
//            public void visitMethodInsn(
//                    final int opcode,
//                    final String owner,
//                    final String name,
//                    final String desc)
//            {
//                addInternalName(owner);
//                addMethodDesc(desc);
//            }
//
//            @Override
//            public void visitInvokeDynamicInsn(
//                    String name,
//                    String desc,
//                    Handle bsm,
//                    Object... bsmArgs)
//            {
//                addMethodDesc(desc);
//                addConstant(bsm);
//                for(int i=0; i<bsmArgs.length; i++) {
//                    addConstant(bsmArgs[i]);
//                }
//            }
//
//            @Override
//            public void visitLdcInsn(final Object cst) {
//                addConstant(cst);
//            }
//
//            @Override
//            public void visitMultiANewArrayInsn(final String desc, final int dims) {
//                addDesc(desc);
//            }
//
//            @Override
//            public void visitLocalVariable(
//                    final String name,
//                    final String desc,
//                    final String signature,
//                    final Label start,
//                    final Label end,
//                    final int index)
//            {
//                addTypeSignature(signature);
//            }
//
//            @Override
//            public void visitTryCatchBlock(
//                    final Label start,
//                    final Label end,
//                    final Label handler,
//                    final String type)
//            {
//                if (type != null) {
//                    addInternalName(type);
//                }
//            }
//        }
//
//        class SignatureDependencyVisitor extends SignatureVisitor {
//
//            String signatureClassName;
//
//            public SignatureDependencyVisitor() {
//                super(Opcodes.ASM4);
//            }
//
//            @Override
//            public void visitClassType(final String name) {
//                signatureClassName = name;
//                addInternalName(name);
//            }
//
//            @Override
//            public void visitInnerClassType(final String name) {
//                signatureClassName = signatureClassName + "$" + name;
//                addInternalName(signatureClassName);
//            }
//        }
//
//        // ---------------------------------------------
//
//        private String getGroupKey(String name) {
//            int n = name.lastIndexOf('/');
//            if (n > -1) {
//                name = name.substring(0, n);
//            }
//            packages.add(name);
//            return name;
//        }
//
//        private void addName(final String name) {
//            if (name == null) {
//                return;
//            }
//            String p = getGroupKey(name);
//            if (current.containsKey(p)) {
//                current.put(p, current.get(p) + 1);
//            } else {
//                current.put(p, 1);
//            }
//        }
//
//        void addInternalName(final String name) {
//            addType(Type.getObjectType(name));
//        }
//
//        private void addInternalNames(final String[] names) {
//            for (int i = 0; names != null && i < names.length; i++) {
//                addInternalName(names[i]);
//            }
//        }
//
//        void addDesc(final String desc) {
//            addType(Type.getType(desc));
//        }
//
//        void addMethodDesc(final String desc) {
//            addType(Type.getReturnType(desc));
//            Type[] types = Type.getArgumentTypes(desc);
//            for (int i = 0; i < types.length; i++) {
//                addType(types[i]);
//            }
//        }
//
//        void addType(final Type t) {
//            switch (t.getSort()) {
//                case Type.ARRAY:
//                    addType(t.getElementType());
//                    break;
//                case Type.OBJECT:
//                    addName(t.getInternalName());
//                    break;
//                case Type.METHOD:
//                    addMethodDesc(t.getDescriptor());
//                    break;
//            }
//        }
//
//        private void addSignature(final String signature) {
//            if (signature != null) {
//                new SignatureReader(signature).accept(new SignatureDependencyVisitor());
//            }
//        }
//
//        void addTypeSignature(final String signature) {
//            if (signature != null) {
//                new SignatureReader(signature).acceptType(new SignatureDependencyVisitor());
//            }
//        }
//
//        void addConstant(final Object cst) {
//            if (cst instanceof Type) {
//                addType((Type) cst);
//            } else if (cst instanceof Handle) {
//                Handle h = (Handle) cst;
//                addInternalName(h.getOwner());
//                addMethodDesc(h.getDesc());
//            }
//        }
//    }
//
//}