import java.io.File;
import java.io.IOException;

public class Git {
    public Git() throws IOException {
        this.initialize();
    }

    public void initialize() throws IOException {
        String gitPath = "./git";
        String objectsDirectoryPath = gitPath + "/objects";
        String indexFilePath = gitPath + "/index";

        File gitDirectory = new File(gitPath);
        File objectsDirectory = new File(objectsDirectoryPath);
        File indexFile = new File(indexFilePath);

        if (gitDirectory.exists() && objectsDirectory.exists() && indexFile.exists()) {
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