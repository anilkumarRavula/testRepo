package com.ci.jgit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

public class CloneRemoteRepository {
    public static final String REMOTE_URL = "https://github.com/OakNorthAI/fineract-cn-portfolio.git";

    public static void main(String[] args) throws IOException, GitAPIException {
        // prepare a new folder for the cloned repository
       // cloneRepo("gitlocal");
    }

    static Git  cloneRepo(String gitlocal, String remoteUrl) throws IOException {
        File localPath = File.createTempFile(gitlocal, "");
        if(!localPath.delete()) {
            throw new IOException("Could not delete temporary file " + localPath);
        }

        // then clone
        System.out.println("Cloning from " + remoteUrl + " to " + localPath);
        try (Git result = Git.cloneRepository()
                .setURI(remoteUrl)
                .setCredentialsProvider(getCredentialsProvider())
                .setDirectory(localPath)
                .setProgressMonitor(new SimpleProgressMonitor())
                .call()) {
            // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
            System.out.println("Having repository: " + result.getRepository().getDirectory());
            return result;
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return null;
        // clean up here to not keep using more and more disk-space for these samples
        //FileUtils.deleteDirectory(localPath);
    }

    public static UsernamePasswordCredentialsProvider getCredentialsProvider() {
        return new UsernamePasswordCredentialsProvider(System.getProperty("git_user", "anildotravula"), System.getProperty("git_pwd", "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"));
    }

    private static class SimpleProgressMonitor implements ProgressMonitor {
        @Override
        public void start(int totalTasks) {
            System.out.println("Starting work on " + totalTasks + " tasks");
        }

        @Override
        public void beginTask(String title, int totalWork) {
            System.out.println("Start " + title + ": " + totalWork);
        }

        @Override
        public void update(int completed) {
            System.out.print(completed + "-");
        }

        @Override
        public void endTask() {
            System.out.println("Done");
        }

        @Override
        public boolean isCancelled() {
            return false;
        }
    }
}
