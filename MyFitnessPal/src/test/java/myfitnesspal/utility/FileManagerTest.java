package myfitnesspal.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class FileManagerTest {

    @Test
    void testSaveAndLoadRawLines() throws Exception {
        Path tempFile = Files.createTempFile("test", ".txt");
        String fileName = tempFile.toString();

        List<String> linesToSave = List.of("Line1", "Line2", "Line3");
        FileManager.saveRawLines(fileName, linesToSave);

        List<String> loadedLines = FileManager.loadRawLines(fileName);
        Assertions.assertEquals(linesToSave.size(), loadedLines.size());
        Assertions.assertEquals(linesToSave, loadedLines);

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testLoadNonExistingFile() throws Exception {
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.deleteIfExists(tempFile);
        String fileName = tempFile.toString();

        List<String> lines = FileManager.loadRawLines(fileName);
        Assertions.assertTrue(lines.isEmpty(), "Expected empty lines from non-existing file.");
    }
}
