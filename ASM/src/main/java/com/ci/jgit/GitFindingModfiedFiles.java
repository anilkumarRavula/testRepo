package com.ci.jgit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GitFindingModfiedFiles {

    public static final String BRANCH_NAME = "vikasratn-oak-patch-27";
    public static final String MAIN_BRANCH_NAME = "develop";
    public static final String REMOTE_URL = "https://github.com/OakNorthAI/fineract-cn-portfolio.git";

    public static void main(String[] args) throws IOException, GitAPIException {

        try (Git git = CloneRemoteRepository.cloneRepo("gitlocalCassandra",REMOTE_URL)) {
            try (Repository repository = git.getRepository()) {
                if(repository.exactRef("refs/heads/"+ BRANCH_NAME) == null) {
                    // first we need to ensure that the remote branch is visible locally
                    Ref ref = git.branchCreate().setName(BRANCH_NAME).setStartPoint("origin/"+ BRANCH_NAME).call();

                    System.out.println("Created local with ref: " + ref);
                }
                // the diff works on TreeIterators, we prepare two for the two branches
                AbstractTreeIterator newTreeParser = prepareTreeParser(repository, "refs/heads/"+ BRANCH_NAME);
                AbstractTreeIterator oldTreeParser = prepareTreeParser(repository, "refs/heads/"+MAIN_BRANCH_NAME);
                // then the procelain diff-command returns a list of diff entries
                List<DiffEntry> diff = git.diff().setOldTree(oldTreeParser).setNewTree(newTreeParser).call();
                for (DiffEntry entry : diff) {
                    System.out.println("Entry: " + entry);
                    //compare(repository, entry.getOldId().toObjectId().getName(), entry.getNewId().toObjectId().getName());
                    System.out.println("Entry: " + entry + ", from: " + entry.getOldId() + ", to: " + entry.getNewId());

                    try (DiffFormatter formatter = new DiffFormatter(System.out)) {
                        formatter.setRepository(repository);
                        FileHeader fileHeader = formatter.toFileHeader(entry);
                        String oldPath = fileHeader.getOldPath();
                        formatter.format(entry);

                        if(!oldPath.endsWith("java")) {
                            System.out.println("================");
                            System.out.println("Skipping ---");
                            System.out.println("================");
                            continue;
                        }
                        TreeMap<Integer,String> lineNumbersAndMethods = getLineNumbersAndMethods(repository.getWorkTree().getPath().replaceAll("\\\\","/")+"/"+oldPath);

                        for (Edit edit : formatter.toFileHeader(entry).toEditList()) {
                            Map.Entry methods = lineNumbersAndMethods.floorEntry(edit.getBeginA());
                            if(methods != null) {
                                System.out.println("================");
                                System.out.println("Modfied method"+ methods.getValue() );
                                System.out.println("================");

                            }
                        }
/*
                        for (Edit edit : formatter.toFileHeader(entry).toEditList()) {
                            linesDeleted += edit.getEndA() - edit.getBeginA();
                            linesAdded += edit.getEndB() - edit.getBeginB();
                            System.out.println(linesDeleted);System.out.println(linesAdded);
                        }
                        formatter.format(entry);
*/

                    }
/*
                    ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
                    try (DiffFormatter formatter = new DiffFormatter(memoryStream)) {
                        formatter.setRepository(repository);
                        formatter.format(entry);
                    }
                    System.out.println("pppppppppppppppppppppppppppppp");
                    System.out.println(memoryStream.toString());
*/
                }
            }
        }
    }
    static String expression = "(?:(?:public|private|protected|static|final|native|synchronized|abstract|transient)+\\s+)+[$_\\w<>\\[\\]\\s]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?\n";
    private static TreeMap<Integer,String> getLineNumbersAndMethods(String filePath) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            AtomicInteger atomicInteger = new AtomicInteger();
            Map<Integer,String> lines = stream.collect(Collectors.toMap(line -> atomicInteger.incrementAndGet(), Function.identity()));
            // identify methods in the source file
           return lines.entrySet().stream().filter(integerStringEntry -> {
                final Pattern pattern = Pattern.compile(expression, Pattern.COMMENTS | Pattern.MULTILINE);
                final Matcher matcher = pattern.matcher(integerStringEntry.getValue());
                return matcher.find();
            }).collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue, (k1,k2)-> k2,()-> new TreeMap<>()));
           // System.out.println(lines);
        }
    }
    public static void compare(Repository repository, String oldId, String newId) throws IOException, GitAPIException {
        System.out.println("comparing: "+oldId+"--> new "+ newId);
            // the diff works on TreeIterators, we prepare two for the two branches
            AbstractTreeIterator oldTreeParser = prepareTreeParserForFiles(repository, oldId);
            AbstractTreeIterator newTreeParser = prepareTreeParserForFiles(repository, newId);

            // then the porcelain diff-command returns a list of diff entries
            try (Git git = new Git(repository)) {
                List<DiffEntry> diff = git.diff().
                        setOldTree(oldTreeParser).
                        setNewTree(newTreeParser).
                        setPathFilter(PathFilter.create("README.md")).
                        // to filter on Suffix use the following instead
                        //setPathFilter(PathSuffixFilter.create(".java")).
                                call();
                for (DiffEntry entry : diff) {
                    System.out.println("Entry: " + entry + ", from: " + entry.getOldId() + ", to: " + entry.getNewId());
                    int linesAdded = 0;
                    int linesDeleted = 0;
                    int filesChanged = 0;

                    try (DiffFormatter formatter = new DiffFormatter(System.out)) {
                        formatter.setRepository(repository);

                        for (Edit edit : formatter.toFileHeader(entry).toEditList()) {
                            linesDeleted += edit.getEndA() - edit.getBeginA();
                            linesAdded += edit.getEndB() - edit.getBeginB();
                            System.out.println(linesDeleted);System.out.println(linesAdded);
                        }
                        formatter.format(entry);

                    }
                    ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();
                    try (DiffFormatter formatter = new DiffFormatter(memoryStream)) {
                        formatter.setRepository(repository);
                        formatter.format(entry);
                    }
                    System.out.println("pppppppppppppppppppppppppppppp");
                    System.out.println(memoryStream.toString());
                }
            }
    }
    public static Repository openJGitCookbookRepository() throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        return builder
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build();
    }
    private static AbstractTreeIterator prepareTreeParserForFiles(Repository repository, String objectId) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        //noinspection Duplicates
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(ObjectId.fromString(objectId));
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }

            walk.dispose();

            return treeParser;
        }
    }
    private static AbstractTreeIterator prepareTreeParser(Repository repository, String ref) throws IOException {
        // from the commit we can build the tree which allows us to construct the TreeParser
        Ref head = repository.exactRef(ref);
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(head.getObjectId());
            RevTree tree = walk.parseTree(commit.getTree().getId());

            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }

            walk.dispose();

            return treeParser;
        }
    }

}
