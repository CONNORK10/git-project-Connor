
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class BlobTest {
    public static void main(String[] args) {
        try {
            for (int i = 0; i < 3; i++) {
                System.out.println("Blob Test Iteration: " + (i + 1));
                runBlobCreationTest("testFile.txt");
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void runBlobCreationTest(String filePath) throws IOException {
        Git.deleteGit();
        Git git = new Git();
        // Create a sample file
        File testFile = new File(filePath);
        if (!testFile.exists()) {
            if (testFile.createNewFile()) {
                System.out.println("Created test file for blob creation.");
            }
        }
        if (checkBlobCreation(filePath)) {
            System.out.println("Blob Creation Test Passed.");
        } else {
            System.out.println("Blob Creation Test Failed.");
        }
        testFile.delete();
        Git.deleteGit();
    }
    private static boolean checkBlobCreation(String filePath) {
        try {
            File indexFile = new File("git/index");
            if (!indexFile.exists()) {
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
            reader.close()
            if (!entryFound) {
                return false;
            }
            File objectsDir = new File("git/objects");
            return objectsDir.exists() && Objects.requireNonNull(objectsDir.listFiles()).length > 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}