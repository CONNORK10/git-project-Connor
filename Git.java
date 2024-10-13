import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Git {
    public Git() throws IOException {
        this.initialize();
    }

    public void initialize() throws IOException {
        String gitPath = "./git";
        String objectsDirectoryPath = gitPath + "/objects";
        String indexFilePath = gitPath + "/index";
        String headFilePath = gitPath + "/HEAD";  // Create HEAD file

        File gitDirectory = new File(gitPath);
        File objectsDirectory = new File(objectsDirectoryPath);
        File indexFile = new File(indexFilePath);
        File headFile = new File(headFilePath);

        if (gitDirectory.exists() && objectsDirectory.exists() && indexFile.exists() && headFile.exists()) {
            System.out.println("Git Repository Already Exists");
        } else {
            if (!gitDirectory.exists() && gitDirectory.mkdir()) {
                System.out.println("Created .git directory.");
            }
            if (!objectsDirectory.exists() && objectsDirectory.mkdir()) {
                System.out.println("Created objects directory.");
            }
            if (!indexFile.exists() && indexFile.createNewFile()) {
                System.out.println("Created index file.");
            }
            if (!headFile.exists() && headFile.createNewFile()) {
                System.out.println("Created HEAD file.");
            }
        }
    }

    public void updateHead(String commitHash) throws IOException {
        FileWriter writer = new FileWriter("./git/HEAD", false);
        writer.write(commitHash);
        writer.close();
    }

    public String getHead() throws IOException {
        return new String(Files.readAllBytes(Paths.get("./git/HEAD")));
    }

    public static void deleteGit() {
        deleteFolder("git");
    }

    private static void deleteFolder(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            return;
        }
        File[] toDelete = folder.listFiles();
        for (File file : toDelete) {
            if (file.isDirectory()) {
                deleteFolder(file.getPath());
            }
            file.delete();
        }
        folder.delete();
    }

    public String commit(String author, String message) throws IOException {
        String parentHash = getHead();
        String treeHash = generateTreeHash();  // Placeholder method for root tree hash
        Commit commit = new Commit(treeHash, parentHash, author, message);
        String commitHash = commit.createCommitFile();
        updateHead(commitHash);
        return commitHash;
    }
    
    private String generateTreeHash() {
        // Placeholder for the actual implementation of creating a tree from index
        return "sample_tree_hash";
    }
}