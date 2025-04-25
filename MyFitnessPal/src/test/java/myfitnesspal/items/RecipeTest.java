package myfitnesspal.items;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class RecipeTest {

    @Test
    void testRecordFields() {
        List<RecipeItem> items = List.of(
                new RecipeItem("Tomato", 2.0),
                new RecipeItem("Salt", 0.5)
        );
        Recipe recipe = new Recipe(
                "Tomato Soup",
                "Simple recipe",
                4,
                500.0,
                200.0,
                30.0,
                10.0,
                8.0,
                items
        );
        Assertions.assertEquals("Tomato Soup", recipe.name());
        Assertions.assertEquals("Simple recipe", recipe.description());
        Assertions.assertEquals(4, recipe.servings());
        Assertions.assertEquals(500.0, recipe.totalGrams(), 0.001);
        Assertions.assertEquals(200.0, recipe.totalCalories(), 0.001);
        Assertions.assertEquals(30.0, recipe.totalCarbs(), 0.001);
        Assertions.assertEquals(10.0, recipe.totalFat(), 0.001);
        Assertions.assertEquals(8.0, recipe.totalProtein(), 0.001);
        Assertions.assertEquals(items, recipe.items());
    }

    @Test
    void testToFileString() {
        List<RecipeItem> items = List.of(
                new RecipeItem("Cheese", 2.0),
                new RecipeItem("Ham", 1.5)
        );
        Recipe recipe = new Recipe("Sandwich",
                "With cheese and ham", 2,
                300.0, 600.0,
                40.0, 15.0, 25.0, items);
        String fileString = recipe.toFileString();
        Assertions.assertTrue(fileString.startsWith(
                "RECIPE;Sandwich;With cheese and ham;2;"
                        + "300.0;600.0;40.0;15.0;25.0;2;"));
        Assertions.assertTrue(fileString.contains("Cheese;2.0;"));
        Assertions.assertTrue(fileString.contains("Ham;1.5;"));
    }

    @Test
    void testToString() {
        Recipe recipe = new Recipe("Pasta",
                "Tomato sauce", 4,
                400.0, 800.0,
                100.0, 10.0, 20.0,
                List.of(new RecipeItem("Tomato", 1.0)));
        String output = recipe.toString();
        Assertions.assertTrue(output.contains(
                "Pasta (100.00g; 200.00 kcal; 25.00g, 2.50g, 5.00g)"));
    }

    @Test
    void testEmptyItems() {
        Recipe recipe = new Recipe("Empty",
                null, 1, 0,
                0, 0, 0,
                0, List.of());
        Assertions.assertEquals(
                "Empty (0.00g; 0.00 kcal; 0.00g, 0.00g, 0.00g)",
                recipe.toString());
        Assertions.assertTrue(recipe.toFileString().contains(
                ";0.0;0.0;0.0;0.0;0.0;0;"));
    }
    @Test
    void testGetTrackableType() {
        Recipe recipe = new Recipe("Cake", "desc",
                4, 800, 1600, 200, 100, 80, List.of());
        Trackable type = recipe.getTrackableType();
        Assertions.assertTrue(type instanceof Recipe);
        Assertions.assertEquals(recipe, type);
    }
}
