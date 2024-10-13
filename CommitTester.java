import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class CommitTester {

    public static void main(String[] args) {
        try {
            Git git = new Git();
            testStage(git, "testfile1.txt");
            testStage(git, "testfile2.txt");
            testCommit(git, "Test commit", "Jonah");
            testCommit(git, "second commit", "Johnah");
            testCommit(git, "third commit", "Jonah");
            // resetFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void resetFiles() throws IOException {
        File indexFile = new File("git" + File.separator + "index");
        if (indexFile.exists()) {
            FileWriter writer = new FileWriter(indexFile, false);
            writer.write("");
            writer.close();
            System.out.println("Index file reset to empty.");
        }
        File objectsDir = new File("git" + File.separator + "objects");
        if (objectsDir.exists()) {
            for (File file : objectsDir.listFiles()) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        }
    }

    public static void testStage(Git git, String filePath) throws IOException {
        File testFile = new File(filePath);
        if (!testFile.exists()) {
            try (FileWriter writer = new FileWriter(testFile)) {
                writer.write("This is a test file for staging: " + filePath);
            }
        }
        git.stage(filePath);
        // check index file
        File indexFile = new File("git" + File.separator + "index");
        if (indexFile.exists()) {
            System.out.println("File successfully staged: " + filePath);
        } else {
            System.out.println("Error: File was not staged.");
        }
    }

    public static void testCommit(Git git, String message, String author) throws IOException {
        git.commit(message, author);
        // check head file
        File headFile = new File("git" + File.separator + "HEAD");
        if (headFile.exists()) {
            String commitHash = Blob.readFile("git" + File.separator + "HEAD").trim();
            System.out.println("Latest commit hash: " + commitHash);
            // check commit file
            File commitFile = new File("git" + File.separator + "objects" + File.separator + commitHash);
            if (commitFile.exists()) {
                System.out.println("Commit successfully created.");
            } else {
                System.out.println("Error: Commit file was not created.");
            }
        } else {
            System.out.println("Error: HEAD file not found.");
        }
    }
}