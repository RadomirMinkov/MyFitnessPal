package myfitnesspal.utility;

import myfitnesspal.Food;
import myfitnesspal.FoodLog;
import myfitnesspal.Meal;
import myfitnesspal.MealItem;
import myfitnesspal.WaterIntake;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ParserTest {

    @Test
    void testParseLineWater() {
        String line = "WATER;2025-03-18;500";
        Trackable t = Parser.parseLine(line);
        Assertions.assertTrue(t instanceof WaterIntake);
        WaterIntake wi = (WaterIntake) t;
        Assertions.assertEquals(LocalDate.of(
                2025, 3, 18), wi.date());
        Assertions.assertEquals(500, wi.amount());
    }

    @Test
    void testParseLineFood() {
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
    void testParseLineFoodLog() {
        String line = "FOOD_LOG;2025-03-18;Lunch;Pizza;200;600;40;20;30";
        Trackable t = Parser.parseLine(line);
        Assertions.assertTrue(t instanceof FoodLog);
        FoodLog fl = (FoodLog) t;
        Assertions.assertEquals(LocalDate.of(
                2025, 3, 18), fl.date());
        Assertions.assertEquals("Lunch", fl.meal());
        Assertions.assertEquals("Pizza", fl.foodName());
        Assertions.assertEquals(200, fl.totalGrams());
        Assertions.assertEquals(600, fl.totalCalories());
        Assertions.assertEquals(40, fl.totalCarbs());
        Assertions.assertEquals(20, fl.totalFat());
        Assertions.assertEquals(30, fl.totalProtein());
    }

    @Test
    void testParseLineMeal() {
        String line =
                "MEAL;MealX;DescX;150.0;400.0;30.0;10.0;5.0;2;FoodA;"
                        + "1.5;FoodB;2.0;";
        Trackable t = Parser.parseLine(line);
        Assertions.assertTrue(t instanceof Meal);
        Meal meal = (Meal) t;
        Assertions.assertEquals("MealX", meal.name());
        Assertions.assertEquals("DescX", meal.description());
        Assertions.assertEquals(150.0,
                meal.totalGrams(), 0.001);
        Assertions.assertEquals(400.0, meal.totalCalories(), 0.001);
        Assertions.assertEquals(30.0, meal.totalCarbs(), 0.001);
        Assertions.assertEquals(10.0, meal.totalFat(), 0.001);
        Assertions.assertEquals(5.0, meal.totalProtein(), 0.001);
        Assertions.assertEquals(2, meal.items().size());
        MealItem item1 = meal.items().get(0);
        MealItem item2 = meal.items().get(1);
        Assertions.assertEquals("FoodA", item1.foodName());
        Assertions.assertEquals(1.5, item1.servings(), 0.001);
        Assertions.assertEquals("FoodB", item2.foodName());
        Assertions.assertEquals(2.0, item2.servings(), 0.001);
    }

    @Test
    void testParseLineMealMissingItems() {
        String invalidLine = "MEAL;Name;Desc;100;200;10;5;3;2;FoodA;1.5";
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Parser.parseLine(invalidLine);
        });
    }

    @Test
    void testParseDateSupportedFormats() {
        LocalDate d1 = Parser.parseDate("2025/03/19");
        Assertions.assertEquals(LocalDate.of(2025, 3,
                19), d1);
        LocalDate d2 = Parser.parseDate("19.03.2025");
        Assertions.assertEquals(LocalDate.of(2025, 3,
                19), d2);
        LocalDate d3 = Parser.parseDate("2025-03-19");
        Assertions.assertEquals(LocalDate.of(2025,
                3, 19), d3);
    }

    @Test
    void testParseDateInvalidFormat() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Parser.parseDate("invalid-date");
        });
    }
}
