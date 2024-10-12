import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

public class Git {
    // Make a method which **initializes a Git repo.**  It should be able to:
    // **Create** a `git` directory if it doesn't exist
    // 1. **Create** an `objects` directory ***inside*** the `git` directory if it doesn't exist
    // 2. **Create** a file named `index` ***inside*** the `git` directory if it doesn't exist 
    //      (Note: it is NOT .txt it is simply called ‘index’)
    
    public void initialize() throws IOException{
        // Define paths for .git, objects directory, and index file
        String gitPath = "./git";
        String objectsDirectoryPath = gitPath + "/objects";
        String indexFilePath = gitPath + "/index";
        File gitDirectory = new File(gitPath);
        File objectsDirectory = new File(objectsDirectoryPath);
        File indexFile = new File(indexFilePath);
        if (gitDirectory.exists() && objectsDirectory.exists() && indexFile.exists()) {
            System.out.println("Git Repository already exists");
        } 
        else {
            if (!gitDirectory.exists()) {
                gitDirectory.mkdir();
            }
            if (!objectsDirectory.exists()) {
                objectsDirectory.mkdir();
            }
            if (!indexFile.exists()) {
                indexFile.createNewFile();
            }
        }   
    }
}