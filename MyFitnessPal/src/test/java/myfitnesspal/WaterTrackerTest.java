package myfitnesspal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static java.nio.file.Files.writeString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WaterTrackerTest {

    private WaterTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new WaterTracker();
    }

    @Test
    void testAddIntakeIncreasesSize() {
        tracker.addIntake(LocalDate.of(2025, 3, 4), 250);
        int finalSize = tracker.getIntakes().size();
        assertEquals(1, finalSize);
    }

    @Test
    void testGetIntakesForDateReturnsEmptyWhenNoneMatch() {
        tracker.addIntake(LocalDate.of(2025, 3, 4), 100);
        tracker.addIntake(LocalDate.of(2025, 3, 5), 200);

        List<WaterIntake> result = tracker.getIntakesForDate(LocalDate.of(2025, 3, 6));
        assertEquals(0, result.size());
    }

    @Test
    void testGetIntakesForDateReturnsOneWhenOneMatches() {
        tracker.addIntake(LocalDate.of(2025, 3, 4), 100);
        tracker.addIntake(LocalDate.of(2025, 3, 5), 200);

        List<WaterIntake> result = tracker.getIntakesForDate(LocalDate.of(2025, 3, 4));
        assertEquals(1, result.size());
    }

    @Test
    void testLoadDataFromFile(@TempDir File tempDir) throws IOException {
        File testFile = new File(tempDir, "water_data.txt");
        String fileContent =
                "2025-03-04;150\n" +
                        "2025/03/05;200\n" +
                        "05.03.2025;300\n";

        writeString(testFile.toPath(), fileContent);

        tracker.loadDataFromFile(testFile.getAbsolutePath());

        int finalSize = tracker.getIntakes().size();
        assertEquals(3, finalSize);
    }

    @Test
    void testSaveDataToFile(@TempDir File tempDir) throws IOException {
        File testFile = new File(tempDir, "save_test.txt");

        tracker.addIntake(LocalDate.of(2025, 3, 4), 250);
        tracker.addIntake(LocalDate.of(2025, 3, 4), 500);
        tracker.saveDataToFile(testFile.getAbsolutePath());

        int linesCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(testFile))) {
            while (br.readLine() != null) {
                linesCount++;
            }
        }

        assertEquals(2, linesCount);
    }
}
