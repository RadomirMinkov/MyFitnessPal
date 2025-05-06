package myfitnesspal.utility;

import myfitnesspal.items.Food;
import myfitnesspal.items.FoodLog;
import myfitnesspal.items.Meal;
import myfitnesspal.items.MealItem;
import myfitnesspal.items.MeasurementType;
import myfitnesspal.items.Trackable;
import myfitnesspal.items.WaterIntake;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class ParserTest {

    @Test
    void testParseLineWater() {
        String line = "WATER;2025-03-18;MILLILITER;500";
        Trackable t = Parser.parseLine(line);
        Assertions.assertTrue(t instanceof WaterIntake);
        WaterIntake wi = (WaterIntake) t;
        Assertions.assertEquals(LocalDate.of(2025, 3, 18), wi.date());
        Assertions.assertEquals(MeasurementType.MILLILITER,
                wi.measurementType());
        Assertions.assertEquals(500.0, wi.amount(), 0.001);
    }

    @Test
    void testParseLineFood() {
        String line =
                "FOOD;Pizza;Cheesy slice;GRAM;100.0;2;300.0;30.0;10.0;15.0";
        Trackable t = Parser.parseLine(line);
        Assertions.assertTrue(t instanceof Food);
        Food f = (Food) t;
        Assertions.assertEquals("Pizza", f.name());
        Assertions.assertEquals("Cheesy slice", f.description());
        Assertions.assertEquals(MeasurementType.GRAM, f.measurementType());
        Assertions.assertEquals(100.0, f.unitsPerServing());
        Assertions.assertEquals(2, f.servingsPerContainer());
        Assertions.assertEquals(300.0, f.calories());
        Assertions.assertEquals(30.0, f.carbs());
        Assertions.assertEquals(10.0, f.fat());
        Assertions.assertEquals(15.0, f.protein());
    }

    @Test
    void testParseLineFoodLog() {
        String line = "FOOD_LOG;2025-03-18;Lunch;Pizza;200;600;40;20;30";
        Trackable t = Parser.parseLine(line);
        Assertions.assertTrue(t instanceof FoodLog);
        FoodLog fl = (FoodLog) t;
        Assertions.assertEquals(LocalDate.of(2025, 3, 18),
                fl.date());
        Assertions.assertEquals("Lunch", fl.meal());
        Assertions.assertEquals("Pizza", fl.foodName());
        Assertions.assertEquals(200.0, fl.totalGrams());
        Assertions.assertEquals(600.0, fl.totalCalories());
        Assertions.assertEquals(40.0, fl.totalCarbs());
        Assertions.assertEquals(20.0, fl.totalFat());
        Assertions.assertEquals(30.0, fl.totalProtein());
    }

    @Test
    void testParseLineMeal() {
        String line =
                "MEAL;MealX;DescX;150.0;400.0;"
                        + "30.0;10.0;5.0;2;FoodA;1.5;FoodB;2.0;";
        Trackable t = Parser.parseLine(line);
        Assertions.assertTrue(t instanceof Meal);
        Meal meal = (Meal) t;
        Assertions.assertEquals("MealX", meal.name());
        Assertions.assertEquals("DescX", meal.description());
        Assertions.assertEquals(150.0, meal.totalGrams(), 0.001);
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
        Assertions.assertEquals(LocalDate.of(2025, 3, 19), d1);
        LocalDate d2 = Parser.parseDate("19.03.2025");
        Assertions.assertEquals(LocalDate.of(2025, 3, 19), d2);
        LocalDate d3 = Parser.parseDate("2025-03-19");
        Assertions.assertEquals(LocalDate.of(2025, 3, 19), d3);
    }

    @Test
    void testParseDateInvalidFormat() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Parser.parseDate("invalid-date");
        });
    }
}
