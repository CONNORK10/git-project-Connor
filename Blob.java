import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class Blob {
    public static String Sha1Hash(File file) {
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

    public static void createBlob(String filePath) throws IOException {
        File originalFile = new File(filePath);
        if (!originalFile.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        String uniqueFileName = Sha1Hash(originalFile);
        if (uniqueFileName == null) {
            throw new IOException("Failed to generate SHA-1 hash for the file.");
        }
        File objectsDir = new File("git/objects");
        if (!objectsDir.exists() && !objectsDir.mkdirs()) {
            throw new IOException("Failed to create 'objects' directory.");
        }
        File newBlobFile = new File(objectsDir, uniqueFileName);
        if (!newBlobFile.exists()) {
            Files.copy(originalFile.toPath(), newBlobFile.toPath());
            System.out.println("Blob created: " + uniqueFileName);
        }
        insertIntoIndexFile(uniqueFileName, originalFile.getPath());
    }
    public static void insertIntoIndexFile(String sha1Hash, String originalFilePath) throws IOException {
        File indexFile = new File("git/index");
        if (!indexFile.exists() && !indexFile.createNewFile()) {
            throw new IOException("Failed to create index file.");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile, true))) {
            writer.write("blob " + sha1Hash + " " + originalFilePath);
            writer.newLine();
        }
    }
}