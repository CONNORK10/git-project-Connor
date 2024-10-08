import java.io.File;
import java.io.IOException;

public class GitTest {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 3; i++) {
            System.out.println("Running Tests: " + (i + 1));
            runTest();
            System.out.println();
        }
    }

    private static void runTest() throws IOException {
        Git.deleteGit();
        Git git = new Git();
        if (checkInitialization()) {
            System.out.println("Test Passed: All files and directories are present.");
        } else {
            System.out.println("Test Failed: Missing files or directories.");
        }
        Git.deleteGit();
    }

    private static boolean checkInitialization() {
        String gitPath = "./git";
        String objectsDirectoryPath = gitPath + "/objects";
        String indexFilePath = gitPath + "/index";
        File gitDirectory = new File(gitPath);
        File objectsDirectory = new File(objectsDirectoryPath);
        File indexFile = new File(indexFilePath);
        return gitDirectory.exists() && objectsDirectory.exists() && indexFile.exists();
    }
}
