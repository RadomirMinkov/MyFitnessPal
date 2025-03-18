package myfitnesspal.utility;

import myfitnesspal.Food;
import myfitnesspal.FoodLog;
import myfitnesspal.WaterIntake;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ParserTest {

    @Test
    void testParseLine_Water() {
        String line = "WATER;2025-03-18;500";
        Trackable t = Parser.parseLine(line);

        Assertions.assertTrue(t instanceof WaterIntake);
        WaterIntake wi = (WaterIntake) t;
        Assertions.assertEquals(LocalDate.of(2025, 3, 18), wi.date());
        Assertions.assertEquals(500, wi.amount());
    }

    @Test
    void testParseLine_Food() {
        String line = "FOOD;Pizza;Cheesy slice;100;2;300;30;10;15";
        Trackable t = Parser.parseLine(line);

        Assertions.assertTrue(t instanceof Food);
        Food f = (Food) t;
        Assertions.assertEquals("Pizza", f.name());
        Assertions.assertEquals("Cheesy slice", f.description());
        Assertions.assertEquals(100, f.servingSize());
        Assertions.assertEquals(2, f.servingsPerContainer());
        Assertions.assertEquals(300, f.calories());
        Assertions.assertEquals(30, f.carbs());
        Assertions.assertEquals(10, f.fat());
        Assertions.assertEquals(15, f.protein());
    }

    @Test
    void testParseLine_FoodLog() {
        String line = "FOOD_LOG;2025-03-18;Lunch;Pizza;200;600;40;20;30";
        Trackable t = Parser.parseLine(line);

        Assertions.assertTrue(t instanceof FoodLog);
        FoodLog fl = (FoodLog) t;
        Assertions.assertEquals(LocalDate.of(2025, 3, 18), fl.date());
        Assertions.assertEquals("Lunch", fl.meal());
        Assertions.assertEquals("Pizza", fl.foodName());
        Assertions.assertEquals(200, fl.totalGrams());
        Assertions.assertEquals(600, fl.totalCalories());
        Assertions.assertEquals(40, fl.totalCarbs());
        Assertions.assertEquals(20, fl.totalFat());
        Assertions.assertEquals(30, fl.totalProtein());
    }

    @Test
    void testParseDate_SupportedFormats() {
        LocalDate d1 = Parser.parseDate("2025/03/19");
        Assertions.assertEquals(LocalDate.of(2025, 3, 19), d1);

        LocalDate d2 = Parser.parseDate("19.03.2025");
        Assertions.assertEquals(LocalDate.of(2025, 3, 19), d2);

        LocalDate d3 = Parser.parseDate("2025-03-19");
        Assertions.assertEquals(LocalDate.of(2025, 3, 19), d3);
    }

    @Test
    void testParseDate_InvalidFormat() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Parser.parseDate("invalid-date");
        });
    }
}
