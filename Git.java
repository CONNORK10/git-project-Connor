import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.math.BigInteger;

public class Git {
    private final Map<String, String> index = new HashMap<>(); // In-memory index for simplicity

    public Git() throws IOException {
        this.initialize();
    }

    /**
     * Initializes the Git repository by creating necessary directories and files.
     */
    public void initialize() throws IOException {
        String gitPath = "./git";
        String objectsDirectoryPath = gitPath + "/objects";
        String indexFilePath = gitPath + "/index";
        String headFilePath = gitPath + "/HEAD";

        File gitDirectory = new File(gitPath);
        File objectsDirectory = new File(objectsDirectoryPath);
        File indexFile = new File(indexFilePath);
        File headFile = new File(headFilePath);

        if (!gitDirectory.exists() && gitDirectory.mkdir()) System.out.println("Created .git directory.");
        if (!objectsDirectory.exists() && objectsDirectory.mkdir()) System.out.println("Created objects directory.");
        if (!indexFile.exists() && indexFile.createNewFile()) System.out.println("Created index file.");
        if (!headFile.exists() && headFile.createNewFile()) System.out.println("Created HEAD file.");
    }

    /**
     * Stages a file or directory for the next commit by creating blobs and updating the index.
     *
     * @param filePath The path to the file or directory to be staged.
     */
    public void stage(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("Error: File or directory not found: " + filePath);
                return;
            }

            if (file.isDirectory()) {
                stageDirectory(file);
            } else {
                stageFile(file);
            }

            System.out.println("Staged " + filePath + " for commit.");
        } catch (IOException e) {
            System.err.println("Error staging file: " + e.getMessage());
        }
    }

    private void stageDirectory(File directory) throws IOException {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                stageDirectory(file);
            } else {
                stageFile(file);
            }
        }
    }

    private void stageFile(File file) throws IOException {
        String fileHash = sha1Hash(file);
        File objectsDir = new File("git/objects");
        if (!objectsDir.exists() && !objectsDir.mkdirs()) {
            throw new IOException("Failed to create 'objects' directory.");
        }
        File newBlobFile = new File(objectsDir, fileHash);
        if (!newBlobFile.exists()) {
            Files.copy(file.toPath(), newBlobFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        index.put(file.getPath(), fileHash);
        insertIntoIndexFile("blob", fileHash, file.getPath());
    }

    public String commit(String author, String message) {
        try {
            String treeHash = generateTreeHash();
            String parentHash = getHead();

            Commit newCommit = new Commit(treeHash, parentHash, author, message);
            String commitHash = newCommit.createCommitFile();

            updateHead(commitHash);
            System.out.println("Created commit with hash: " + commitHash);
            return commitHash;
        } catch (IOException e) {
            System.err.println("Error creating commit: " + e.getMessage());
            return null;
        }
    }

    public void checkout(String commitHash) {
        try {
            File commitFile = new File("git/objects", commitHash);
            if (!commitFile.exists()) {
                System.err.println("Error: Commit not found.");
                return;
            }

            Commit targetCommit = readCommit(commitFile);
            restoreWorkingDirectory(targetCommit.getTreeHash());
            updateHead(commitHash);
            System.out.println("Checked out commit " + commitHash);
        } catch (IOException e) {
            System.err.println("Error during checkout: " + e.getMessage());
        }
    }

    private String generateTreeHash() throws IOException {
        return sha1Hash(new File("git/index"));
    }

    private Commit readCommit(File commitFile) throws IOException {
        return new Commit("sample_tree_hash", null, "author", "message");
    }

    private void restoreWorkingDirectory(String treeHash) throws IOException {
        System.out.println("Restoring working directory to tree hash: " + treeHash);
    }

    private void insertIntoIndexFile(String type, String sha1Hash, String originalFilePath) throws IOException {
        File indexFile = new File("git/index");
        try (var writer = Files.newBufferedWriter(indexFile.toPath(), StandardOpenOption.APPEND)) {
            writer.write(type + " " + sha1Hash + " " + originalFilePath);
            writer.newLine();
        }
    }

    private void updateHead(String commitHash) throws IOException {
        File headFile = new File("git/HEAD");
        Files.writeString(headFile.toPath(), commitHash);
    }

    private String getHead() throws IOException {
        return Files.readString(new File("git/HEAD").toPath());
    }

    private static String sha1Hash(File file) throws IOException {
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-1");
            byte[] sha1bytes = digester.digest(Files.readAllBytes(file.toPath()));
            BigInteger sha1data = new BigInteger(1, sha1bytes);
            String hash = sha1data.toString(16);
            while (hash.length() < 40) hash = "0" + hash;
            return hash;
        } catch (Exception e) {
            throw new IOException("Failed to generate SHA-1 hash for file.", e);
        }
    }
}