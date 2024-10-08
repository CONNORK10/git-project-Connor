import java.io.File;
import java.io.IOException;

public class Git {
    public static void main(String[] args) throws IOException {
        deleteGit();
        Git git = new Git();
        Blob.createBlob("testFile.txt");
    }

    public Git() throws IOException {
        this.initialize();
    }

    public void initialize() throws IOException {
        // Define paths for .git, objects directory, and index file
        String gitPath = "./git";
        String objectsDirectoryPath = gitPath + "/objects";
        String indexFilePath = gitPath + "/index";
        File gitDirectory = new File(gitPath);
        File objectsDirectory = new File(objectsDirectoryPath);
        File indexFile = new File(indexFilePath);
        if (gitDirectory.exists() && objectsDirectory.exists() && indexFile.exists()) {
            System.out.println("Git Repository already exists.");
        } else {
            if (!gitDirectory.exists()) {
                if (gitDirectory.mkdir()) {
                    System.out.println("Created .git directory.");
                }
            }
            if (!objectsDirectory.exists()) {
                if (objectsDirectory.mkdir()) {
                    System.out.println("Created objects directory.");
                }
            }
            if (!indexFile.exists()) {
                if (indexFile.createNewFile()) {
                    System.out.println("Created index file.");
                }
            }
        }
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
}