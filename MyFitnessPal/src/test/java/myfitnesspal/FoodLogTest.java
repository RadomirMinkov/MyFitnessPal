package myfitnesspal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class FoodLogTest {

    @Test
    void testFoodLogRecord() {
        LocalDate date = LocalDate.of(2025, 3, 19);
        FoodLog log = new FoodLog(date, "Lunch", "Pizza",
                200, 600, 40, 20, 30);

        Assertions.assertEquals(date, log.date());
        Assertions.assertEquals("Lunch", log.meal());
        Assertions.assertEquals("Pizza", log.foodName());
        Assertions.assertEquals(200, log.totalGrams());
        Assertions.assertEquals(600, log.totalCalories());
        Assertions.assertEquals(40, log.totalCarbs());
        Assertions.assertEquals(20, log.totalFat());
        Assertions.assertEquals(30, log.totalProtein());
    }

    @Test
    void testToFileString() {
        LocalDate date = LocalDate.of(2025, 3, 19);
        FoodLog log = new FoodLog(date, "Lunch", "Pizza",
                200, 600, 40, 20, 30);

        String fileStr = log.toFileString();
        Assertions.assertTrue(
                fileStr.contains(
                        "FOOD_LOG;2025-03-19;Lunch;Pizza;"
                                + "200.0;600.0;40.0;20.0;30.0"));
    }

    @Test
    void testToString() {
        LocalDate date = LocalDate.of(2025, 3, 19);
        FoodLog log = new FoodLog(date, "Lunch", "Pizza",
                200, 600, 40, 20, 30);

        String s = log.toString();
        Assertions.assertTrue(s.contains("200.0g Pizza"));
        Assertions.assertTrue(s.contains("600 kcal"));
        Assertions.assertTrue(s.contains("Lunch"));
        Assertions.assertTrue(s.contains("2025-03-19"));
    }
}
