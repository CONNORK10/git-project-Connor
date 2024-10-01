import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class Blob {
    // https://www.geeksforgeeks.org/sha-1-hash-in-java/
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

    // Creates a blob and stores the file in the objects directory
    public static void createBlob(String filePath) throws IOException {
        File originalFile = new File(filePath);
        if (!originalFile.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        if (originalFile.isDirectory()) {
            createTree(filePath);
        } else {
            String uniqueFileName = Sha1Hash(originalFile);
            if (uniqueFileName == null) {
                throw new IOException("Failed to generate SHA-1 hash for the file.");
            }
            File objectsDir = new File("git/objects");
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
            insertIntoIndexFile(uniqueFileName, originalFile.getPath());
        }
    }

    private static void createTree(String dirFilePath) throws IOException {
        File originalFile = new File(dirFilePath);
        File[] files = originalFile.listFiles();
        for (File f : files) {
            createBlob(f.getPath());
        }
        StringBuilder str = new StringBuilder();
        for (File f : files) {
            String hash = getHash(f.getPath());
            if (f.isFile()) {
                str.append("blob " + hash + " " + f.getPath());
            } else {
                str.append("tree " + hash + " " + f.getPath());
            }
            str.append("\n");
        }
        File temp = new File("randomStupid.tmp");
        BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
        writer.write(str.toString());
        writer.close();
        String hash = Sha1Hash(temp);
        temp.delete();
        writer = new BufferedWriter(new FileWriter("git/objects/" + hash));
        writer.write(str.toString());
        writer.close();
        insertDirectoryIntoIndexFile(hash, dirFilePath);
    }

    //index 46 is the position of the start of the filePath on each line of the index file
    private static String getHash(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("git/index"));
        while (reader.ready()) {
            String line = reader.readLine();
            if (Objects.equals(line.substring(46), path)) {
                reader.close();
                return line.substring(line.length() - path.length() - 41, line.length() - path.length() - 1);
            }
        }
        reader.close();
        throw new FileNotFoundException();
    }

    // Insert a new entry into the index file
    public static void insertIntoIndexFile(String sha1Hash, String originalFilePath) throws IOException {
        File indexFile = new File("git/index");
        if (!indexFile.exists()) {
            if (!indexFile.createNewFile()) {
                throw new IOException("Failed to create index file.");
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile, true))) {
            writer.write("blob " + sha1Hash + " " + originalFilePath);
            writer.newLine();
        }
    }

    public static void insertDirectoryIntoIndexFile(String sha1Hash, String originalFileName) throws IOException {
        File indexFile = new File("git/index");
        if (!indexFile.exists()) {
            if (!indexFile.createNewFile()) {
                throw new IOException("Failed to create index file.");
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile, true))) {
            writer.write("tree " + sha1Hash + " " + originalFileName);
            writer.newLine();
        }
    }
}