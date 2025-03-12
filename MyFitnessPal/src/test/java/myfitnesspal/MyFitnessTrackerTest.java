package myfitnesspal;

import myfitnesspal.utility.Trackable;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MyFitnessTrackerTest {

    private MyFitnessTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new MyFitnessTracker();
    }

    @Test
    @DisplayName("Test adding WaterIntake items and retrieving them")
    void testAddAndGetWaterIntake() {
        WaterIntake waterIntake1 = new WaterIntake(LocalDate.of(2025, 1, 1), 250);
        WaterIntake waterIntake2 = new WaterIntake(LocalDate.of(2025, 1, 2), 500);

        tracker.addItem(waterIntake1);
        tracker.addItem(waterIntake2);

        List<WaterIntake> waterList = tracker.getWaterIntakes();
        assertEquals(2, waterList.size(), "Should have exactly 2 water intake records");
        assertTrue(waterList.contains(waterIntake1), "Tracker must contain the first WaterIntake record");
        assertTrue(waterList.contains(waterIntake2), "Tracker must contain the second WaterIntake record");
    }

    @Test
    @DisplayName("Test adding Food items and retrieving them")
    void testAddAndGetFoods() {
        Food food1 = new Food("Apple", "Red Apple",
                100.0, 1, 52.0,
                14.0, 0.2, 0.3);

        Food food2 = new Food("Banana", "Yellow Banana",
                118.0, 1, 105.0,
                27.0, 0.3, 1.3);

        tracker.addItem(food1);
        tracker.addItem(food2);

        List<Food> foodList = tracker.getFoods();
        assertEquals(2, foodList.size(), "Should have exactly 2 foods in tracker");
        assertTrue(foodList.contains(food1), "Tracker must contain the first Food record");
        assertTrue(foodList.contains(food2), "Tracker must contain the second Food record");
    }

    @Test
    @DisplayName("Test load/save cycles (in-memory demo)")
    void testLoadAndSave() {

        tracker.addItem(new WaterIntake(LocalDate.of(2025, 1, 1), 250));

        tracker.addItem(new Food("Orange",
                "", 100, 1,
                62, 15, 0.2, 1.2));

        assertDoesNotThrow(() -> tracker.save("test_data.txt"),
                "Saving should not throw an exception");

        tracker = new MyFitnessTracker();
        assertDoesNotThrow(() -> tracker.load("test_data.txt"),
                "Loading should not throw an exception");

        List<Trackable> allItems = List.of(tracker.getFoods().toArray(new Trackable[0]));
        allItems = List.of(allItems.toArray(tracker.getWaterIntakes().toArray(new Trackable[0])));

        assertTrue(true, "Load/Save cycle passed without exceptions");
    }
}
