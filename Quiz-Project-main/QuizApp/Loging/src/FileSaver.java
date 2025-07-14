/*import java.io.File;
import java.io.IOException;

public class FileSaver {

    public void saveFile(String sourcePath, String destinationPath) throws IOException {
        File sourceFile = new File(sourcePath);
        File destinationFile = new File(destinationPath);

        // Check if the source file exists
        if (!sourceFile.exists()) {
            throw new IOException("Source file does not exist.");
        }

        // Ensure the destination directory exists
        File destinationDir = destinationFile.getParentFile();
        if (!destinationDir.exists()) {
            if (!destinationDir.mkdirs()) {
                throw new IOException("Failed to create destination directory.");
            }
        }

        // Copy the file
        java.nio.file.Files.copy(sourceFile.toPath(), destinationFile.toPath());
    }
}*/
