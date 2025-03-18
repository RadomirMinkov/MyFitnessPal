package myfitnesspal;

import myfitnesspal.utility.FileManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

class MyFitnessTrackerTest {

    private MyFitnessTracker tracker;

    @BeforeEach
    void setup() {
        tracker = new MyFitnessTracker();
    }

    @Test
    void testAddItemAndGetWaterIntakes() {
        WaterIntake wi = new WaterIntake(LocalDate.of(2025,
                3,19), 500);
        tracker.addItem(wi);
        List<WaterIntake> waterIntakes = tracker.getWaterIntakes();
        Assertions.assertEquals(1, waterIntakes.size());
        Assertions.assertEquals(wi, waterIntakes.get(0));
    }

    @Test
    void testAddItemAndGetFoods() {
        Food f = new Food("Pizza", "Cheesy slice",
                100, 2, 300, 30, 10, 15);
        tracker.addItem(f);
        List<Food> foods = tracker.getFoods();
        Assertions.assertEquals(1, foods.size());
        Assertions.assertEquals(f, foods.get(0));
    }

    @Test
    void testAddItemAndGetFoodLogs() {
        FoodLog fl = new FoodLog(LocalDate.of(2025,3,19),
                "Lunch","Pizza",200,600,40,
                20,30);
        tracker.addItem(fl);
        List<FoodLog> logs = tracker.getFoodLogs();
        Assertions.assertEquals(1, logs.size());
        Assertions.assertEquals(fl, logs.get(0));
    }

    @Test
    void testLoadAndSave() throws Exception {
        Path tempFile = Files.createTempFile("testTracker", ".txt");
        String fileName = tempFile.toString();

        List<String> linesToSave = List.of(
                "WATER;2025-03-19;500",
                "FOOD;Pizza;Cheesy slice;100;2;300;30;10;15",
                "FOOD_LOG;2025-03-19;Lunch;Pizza;200;600;40;20;30"
        );
        FileManager.saveRawLines(fileName, linesToSave);
        tracker.load(fileName);

        Assertions.assertEquals(3,
                tracker.getWaterIntakes().size()
                        + tracker.getFoods().size()
                        + tracker.getFoodLogs().size());

        tracker.addItem(new WaterIntake(LocalDate.of(2025,3,20), 600));
        tracker.save(fileName);

        MyFitnessTracker anotherTracker = new MyFitnessTracker();
        anotherTracker.load(fileName);
        Assertions.assertEquals(4, anotherTracker.getWaterIntakes().size() +
                anotherTracker.getFoods().size() +
                anotherTracker.getFoodLogs().size());

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testGetFoodLogsForDate() {
        FoodLog fl1 = new FoodLog(LocalDate.of(2025,3,19),
                "Lunch","Pizza",200,600,40,20,30);
        FoodLog fl2 = new FoodLog(LocalDate.of(2025,3,20),
                "Dinner","Pasta",300,500,50,10,20);
        tracker.addItem(fl1);
        tracker.addItem(fl2);

        List<FoodLog> logsFor19 = tracker.getFoodLogsForDate(LocalDate.of(2025,3,19));
        Assertions.assertEquals(1, logsFor19.size());
        Assertions.assertEquals(fl1, logsFor19.get(0));
    }
}
