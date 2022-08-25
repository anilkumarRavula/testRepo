package com.asm.local;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FindingMethodNames {
    static String expression = "(?:(?:public|private|protected|static|final|native|synchronized|abstract|transient)+\\s+)?[$_\\w<>\\[\\]\\s]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?\n";

    private static TreeMap<Integer, String> getLineNumbersAndMethods(String filePath) throws IOException {
        File file = new File(filePath);
        String stringWriter = new String(Files.readAllBytes(Paths.get(filePath)));
        final Pattern pattern = Pattern.compile(expression, Pattern.COMMENTS | Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(stringWriter);
        ArrayList<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        Iterator<String> it = list.iterator();
        System.out.println("List of matches: ");
        while (it.hasNext()) {
            String method = it.next();
            String methodName = method.substring(0, method.indexOf("{") + 1);
            String[] methodParts = methodName.split("\\r\\n");
            String finalMethodName = Arrays.stream(methodParts).map(String::trim).collect(Collectors.joining(" "));
            System.out.println(finalMethodName);
            System.out.println("======================");
        }

        return null;
    }

    private static TreeMap<Integer, String> getLineNumber(String filePath) throws IOException {
        final Pattern pattern = Pattern.compile(expression, Pattern.COMMENTS | Pattern.MULTILINE);

        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            AtomicInteger atomicInteger = new AtomicInteger();
            Map<Integer, String> lines = stream.collect(Collectors.toMap(line -> atomicInteger.incrementAndGet(), Function.identity()));
            // identify methods in the source file
            return lines.entrySet().stream().filter(integerStringEntry -> {
                final Matcher matcher = pattern.matcher(integerStringEntry.getValue());
                return matcher.find();
            }).collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                String name = entry.getValue();
                System.out.println(name);
                return trimToMethodNameAndReturnType(name);
            }, (k1, k2) -> k2, () -> new TreeMap<>()));
            // log.info(lines);
        }
    }

    private static String trimToMethodNameAndReturnType(String name) {
        if (name.isEmpty() || !name.contains("(")) return name;

        //System.out.println("input name==="+name);
        String methodPrefix = name.substring(0, name.indexOf("("));
        String[] methodNameAndReturnType = methodPrefix.split("\\s+");
        if (methodNameAndReturnType.length > 1) {
            IntStream.range(methodNameAndReturnType.length - 2, methodNameAndReturnType.length).mapToObj(index -> methodNameAndReturnType[index]).collect(Collectors.joining(" "));
            return IntStream.range(methodNameAndReturnType.length - 2, methodNameAndReturnType.length).mapToObj(index -> methodNameAndReturnType[index]).collect(Collectors.joining(" ")) + name.substring(name.indexOf("("));
        } else {
            return "";
        }
    }

    public static TreeMap<Integer, String> getLineNumbersAndMethodsWithLines(String filePath) throws IOException {
        TreeMap<Integer, String> lineNumberAndString = getLineNumber(filePath);
        System.out.println(lineNumberAndString);
        ArrayList<String> macthedMethods = getMatchedLines(filePath);
        Map<String, String> partsAndMap = contructMethodNameAndParts(macthedMethods);
        return lineNumberAndString.entrySet().stream().filter(entry -> partsAndMap.containsKey(entry.getValue())).collect(Collectors.toMap(Map.Entry::getKey, entry -> partsAndMap.get(entry.getValue()), (k1, k2) -> k2, () -> new TreeMap<>()));
    }

    private static Map<String, String> contructMethodNameAndParts(List<String> macthedMethods) {
        Iterator<String> it = macthedMethods.iterator();
        Map<String, String> partsAndMap = new HashMap<>();
        while (it.hasNext()) {
            String method = it.next();
            System.out.println("ORG-->" + method);
            String methodName = method.substring(0, method.indexOf("{") + 1);
            String[] methodParts = methodName.split("\\r\\n");
            String finalMethodName = Arrays.stream(methodParts).map(String::trim).collect(Collectors.joining(" "));
            System.out.println("MOD-->");
            String modifiedName = trimToMethodNameAndReturnType(finalMethodName);
            if (!modifiedName.isEmpty() && modifiedName.trim().length() > 0) {
                System.out.println(modifiedName);
                modifiedName = modifiedName.trim().replaceAll("[\\s+\\{\\s+]"," ")
                        .replaceAll("(:?@\\w+(:?\\s*\\(.*?\\)\\s*)?|\\s*final )"," ")
                        //.replaceAll("\\s+[a-zA-z]+,",",")
                        .replaceAll("(\\s*(\\w+)\\s+\\w+\\s*(,|\\))+)+?","$2$3");
                System.out.println(modifiedName);
                for (String methodPart : methodParts) {
                    partsAndMap.put(trimToMethodNameAndReturnType(methodPart.trim()), modifiedName);
                }
            }
            System.out.println("======================");
        }
        System.out.println(partsAndMap);
        return partsAndMap;
    }

    private static ArrayList<String> getMatchedLines(String filePath) throws IOException {
        String stringWriter = new String(Files.readAllBytes(Paths.get(filePath)));
        final Pattern pattern = Pattern.compile(expression, Pattern.COMMENTS | Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(stringWriter);
        ArrayList<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    public static void main(String[] args) throws IOException {
        TreeMap<Integer, String> partsAndMap = getLineNumbersAndMethodsWithLines("C:\\anil\\codesetup\\intail\\fineract-cn-portfolio\\service\\src\\main\\java\\org\\apache\\fineract\\cn\\portfolio\\service\\rest\\LoanRestController.java");
        System.out.println(partsAndMap);


    }

}
