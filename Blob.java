import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob {
    // https://www.geeksforgeeks.org/sha-1-hash-in-java/
    public String Sha1Hash(File file) {
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-1");
            byte[] sha1bytes = digester.digest(Files.readAllBytes(file.toPath()));
            BigInteger sha1data = new BigInteger(1, sha1bytes);
            String hash = sha1data.toString(16);
            while (hash.length() < 40) {
                hash = "0" + hash;
            }
            return hash;
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    // Creates a blob and stores the file in the objects directory
    public void createBlob(String filePath) throws IOException {
        File originalFile = new File(filePath);
        if (!originalFile.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        String uniqueFileName = Sha1Hash(originalFile);
        if (uniqueFileName == null) {
            throw new IOException("Failed to generate SHA-1 hash for the file.");
        }
        File objectsDir = new File(".git/objects");
        if (!objectsDir.exists()) {
            if (!objectsDir.mkdirs()) {
                throw new IOException("Failed to create 'objects' directory.");
            }
        }
        File newBlobFile = new File(objectsDir, uniqueFileName);
        if (!newBlobFile.exists()) {
            Files.copy(originalFile.toPath(), newBlobFile.toPath());
            System.out.println("Blob created: " + uniqueFileName);
        }
        insertIntoIndexFile(uniqueFileName, originalFile.getName());
    }

    // Insert a new entry into the index file
    public void insertIntoIndexFile(String sha1Hash, String originalFileName) throws IOException {
        File indexFile = new File(".git/index");
        if (!indexFile.exists()) {
            if (!indexFile.createNewFile()) {
                throw new IOException("Failed to create index file.");
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile, true))) {
            writer.write(sha1Hash + " " + originalFileName);
            writer.newLine();
        }
    }
}