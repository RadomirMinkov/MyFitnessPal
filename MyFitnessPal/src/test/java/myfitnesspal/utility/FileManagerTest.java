package myfitnesspal.utility;

import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    private static final String TEST_FILE = "test_output.txt";

    @AfterEach
    void cleanUp() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            assertTrue(file.delete(), "Could not delete the test file");
        }
    }

    @Test
    @DisplayName("Test writing and reading lines")
    void testWriteAndRead() {
        List<String> linesToWrite = List.of("line1", "line2", "line3");
        FileManager.saveRawLines(TEST_FILE, linesToWrite);

        List<String> readLines = FileManager.loadRawLines(TEST_FILE);
        assertEquals(linesToWrite.size(), readLines.size());
        for (int i = 0; i < linesToWrite.size(); i++) {
            assertEquals(linesToWrite.get(i), readLines.get(i), "Line content should match");
        }
    }

    @Test
    @DisplayName("Test loading from non-existent file returns empty list")
    void testNonExistentFile() {
        List<String> result = FileManager.loadRawLines("no_such_file.txt");
        assertNotNull(result, "Should return a non-null list");
        assertTrue(result.isEmpty(), "Should return an empty list if file does not exist");
    }
}
