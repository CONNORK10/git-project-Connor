import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BlobTest {
    public static void main(String[] args) {
        try {
            for (int i = 0; i < 3; i++) {
                System.out.println("Running Test: " + (i + 1));
                runTest("testFile.txt");
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runTest(String filePath) throws IOException {
        Git.deleteGit();
        Git git = new Git();
        File testFile = new File(filePath);
        if (!testFile.exists()) {
            testFile.createNewFile();
        }
        Blob.createBlob(filePath);
        if (checkBlobCreation(filePath)) {
            System.out.println("Test Passed: Files and index entries are present.");
        } else {
            System.out.println("Test Failed: Missing files or index entries.");
        }
        testFile.delete();
        Git.deleteGit();
    }
    private static boolean checkBlobCreation(String filePath) {
        try {
            File indexFile = new File("git/index");
            if (!indexFile.exists()) {
                System.out.println("Index file does not exist.");
                return false;
            }
            BufferedReader reader = new BufferedReader(new FileReader(indexFile));
            boolean entryFound = false;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(filePath)) {
                    entryFound = true;
                    break;
                }
            }
            reader.close();
            if (!entryFound) {
                System.out.println("Blob entry not found in index file.");
                return false;
            }
            File objectsDir = new File("git/objects");
            if (!objectsDir.exists() || Objects.requireNonNull(objectsDir.listFiles()).length == 0) {
                System.out.println("No blob file found in objects directory.");
                return false;
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
