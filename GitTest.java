
import java.io.File;
import java.io.IOException;

public class GitTest {
    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 3; i++) {
            System.out.println("Test Iteration: " + (i + 1));
            runGitInitializationTest();
            System.out.println();
        }
    }
    private static void runGitTest() throws IOException {
        Git.deleteGit();
        Git git = new Git();
        if (checkInitialization()) {
            System.out.println("Initialization Test Passed.");
        } else {
            System.out.println("Initialization Test Failed.");
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