package myfitnesspal.utility;

import myfitnesspal.items.BodyMeasurement;
import myfitnesspal.items.BodyMeasurementMetric;
import myfitnesspal.items.Food;
import myfitnesspal.items.FoodLog;
import myfitnesspal.items.Meal;
import myfitnesspal.items.MealItem;
import myfitnesspal.items.MeasurementType;
import myfitnesspal.items.Recipe;
import myfitnesspal.items.RecipeItem;
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
        Assertions.assertEquals(LocalDate.of(2025, 3,
                18), wi.date());
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
        Assertions.assertEquals(LocalDate.of(2025, 3,
                19), d1);
        LocalDate d2 = Parser.parseDate("19.03.2025");
        Assertions.assertEquals(LocalDate.of(2025, 3,
                19), d2);
        LocalDate d3 = Parser.parseDate("2025-03-19");
        Assertions.assertEquals(LocalDate.of(2025, 3,
                19), d3);
    }

    @Test
    void testParseDateInvalidFormat() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Parser.parseDate("invalid-date");
        });
    }
    @Test
    void testParseLineBodyMeasurementWeight() {
        String line = "BM;2025-05-27;WEIGHT;kg;value=82.0";
        Trackable t = Parser.parseLine(line);
        Assertions.assertTrue(t instanceof BodyMeasurement);
        BodyMeasurement bm = (BodyMeasurement) t;
        Assertions.assertEquals(LocalDate.of(2025, 5,
                27), bm.date());
        Assertions.assertEquals(BodyMeasurementMetric.WEIGHT, bm.metric());
        Assertions.assertEquals("kg", bm.unit());
        Assertions.assertEquals(82.0,
                bm.values().get("value"), 0.001);
    }

    @Test
    void testParseLineBodyMeasurementBiceps() {
        String line = "BM;2025-05-27;BICEPS;cm;left=34.1;right=34.5";
        Trackable t = Parser.parseLine(line);
        Assertions.assertTrue(t instanceof BodyMeasurement);
        BodyMeasurement bm = (BodyMeasurement) t;
        Assertions.assertEquals(34.1,
                bm.values().get("left"), 0.001);
        Assertions.assertEquals(34.5,
                bm.values().get("right"), 0.001);
    }

    @Test
    void testParseLineBodyMeasurementInvalid() {
        String invalid = "BM;2025-05-27;WEIGHT;kg";
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Parser.parseLine(invalid));
    }
    @Test
    void testParseLineRecipe() {
        String line = "RECIPE;Smoothie;Fruit blend;2;500.0;300.0;"
                + "60.0;5.0;10.0;3;Banana;1.0;Milk;0.5;Honey;0.2";
        Trackable t = Parser.parseLine(line);
        Assertions.assertTrue(t instanceof Recipe);
        Recipe r = (Recipe) t;

        Assertions.assertEquals("Smoothie", r.name());
        Assertions.assertEquals("Fruit blend", r.description());
        Assertions.assertEquals(2, r.servings());
        Assertions.assertEquals(500.0, r.totalGrams(), 0.001);
        Assertions.assertEquals(300.0, r.totalCalories(), 0.001);
        Assertions.assertEquals(60.0, r.totalCarbs(), 0.001);
        Assertions.assertEquals(5.0, r.totalFat(), 0.001);
        Assertions.assertEquals(10.0, r.totalProtein(), 0.001);

        Assertions.assertEquals(3, r.items().size());
        RecipeItem i1 = r.items().get(0);
        RecipeItem i2 = r.items().get(1);
        RecipeItem i3 = r.items().get(2);

        Assertions.assertEquals("Banana", i1.foodName());
        Assertions.assertEquals(1.0, i1.servings(), 0.001);
        Assertions.assertEquals("Milk",   i2.foodName());
        Assertions.assertEquals(0.5, i2.servings(), 0.001);
        Assertions.assertEquals("Honey",  i3.foodName());
        Assertions.assertEquals(0.2, i3.servings(), 0.001);
    }

    @Test
    void testParseLineRecipeMissingItems() {
        String bad = "RECIPE;R;D;1;100;200;30;5;10;2;ItemA;1.0";
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Parser.parseLine(bad));
    }
}
