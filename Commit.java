import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Commit {
    private String treeHash;
    private String parentHash;
    private String author;
    private String message;
    private String date;

    public Commit(String treeHash, String parentHash, String author, String message) {
        this.treeHash = treeHash;
        this.parentHash = parentHash;
        this.author = author;
        this.message = message;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public String createCommitFile() throws IOException {
        StringBuilder commitContent = new StringBuilder();
        commitContent.append("tree: ").append(treeHash).append("\n");
        if (parentHash != null) {
            commitContent.append("parent: ").append(parentHash).append("\n");
        }
        commitContent.append("author: ").append(author).append("\n");
        commitContent.append("date: ").append(date).append("\n");
        commitContent.append("message: ").append(message).append("\n");

        String commitHash = sha1Hash(commitContent.toString());
        File commitFile = new File("git/objects", commitHash);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(commitFile))) {
            writer.write(commitContent.toString());
        }
        return commitHash;
    }

    private String sha1Hash(String input) {
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-1");
            byte[] sha1bytes = digester.digest(input.getBytes());
            BigInteger sha1data = new BigInteger(1, sha1bytes);
            String hash = sha1data.toString(16);
            while (hash.length() < 40) {
                hash = "0" + hash;
            }
            return hash;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}