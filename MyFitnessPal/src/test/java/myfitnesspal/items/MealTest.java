package myfitnesspal.items;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class MealTest {

    @Test
    void testRecordFields() {
        List<MealItem> items = List.of(
                new MealItem("FoodA", 2.0),
                new MealItem("FoodB", 1.5)
        );
        Meal meal = new Meal(
                "TestMeal",
                "Some description",
                300,
                600,
                50,
                20,
                10,
                items
        );

        Assertions.assertEquals("TestMeal", meal.name());
        Assertions.assertEquals("Some description", meal.description());
        Assertions.assertEquals(300, meal.totalGrams(), 0.001);
        Assertions.assertEquals(600, meal.totalCalories(), 0.001);
        Assertions.assertEquals(50, meal.totalCarbs(), 0.001);
        Assertions.assertEquals(20, meal.totalFat(), 0.001);
        Assertions.assertEquals(10, meal.totalProtein(), 0.001);
        Assertions.assertEquals(items, meal.items());
    }

    @Test
    void testToFileString() {
        List<MealItem> items = List.of(
                new MealItem("FoodA", 1.0),
                new MealItem("FoodB", 2.0)
        );
        Meal meal = new Meal("MealX", "DescX",
                150, 400,
                30, 10, 5, items);
        String result = meal.toFileString();

        Assertions.assertTrue(result.startsWith(
                "MEAL;MealX;DescX;150.0;400.0;30.0;10.0;5.0;2;"));
        Assertions.assertTrue(result.contains("FoodA;1.0;"));
        Assertions.assertTrue(result.contains("FoodB;2.0;"));
    }

    @Test
    void testToString() {
        Meal meal = new Meal("Breakfast", "desc",
                200, 400, 40,
                12, 8,
                List.of(new MealItem("Food", 1.0)));
        String str = meal.toString();
        // Format: "Breakfast (200g; 400 kcal; 40.00g, 12.00g, 8.00g)"
        Assertions.assertTrue(str.contains("Breakfast"));
        Assertions.assertTrue(str.contains("200g"));
        Assertions.assertTrue(str.contains("400 kcal"));
        Assertions.assertTrue(str.contains("40.00g, 12.00g, 8.00g"));
    }

    @Test
    void testEmptyFields() {
        Meal meal = new Meal(null, null,
                0, 0,
                0, 0, 0, List.of());
        String fileStr = meal.toFileString();
        Assertions.assertTrue(fileStr.startsWith("MEAL;;;"
                + "0.0;0.0;0.0;0.0;0.0;0;"));
        Assertions.assertEquals(
                "null (0g; 0 kcal; 0.00g, 0.00g, 0.00g)", meal.toString());
    }
}
