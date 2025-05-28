package myfitnesspal.commands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.logcommands.LogRecipeCommand;
import myfitnesspal.items.Food;
import myfitnesspal.items.FoodLog;
import myfitnesspal.items.Recipe;
import myfitnesspal.items.RecipeItem;
import myfitnesspal.items.MeasurementType;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LogRecipeCommandTest {

    @Test
    void testExecuteSuccess() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        InputProvider input = mock(InputProvider.class);
        OutputWriter out = mock(OutputWriter.class);

        Food food = new Food("Tomato", "desc",
                MeasurementType.GRAM, 100, 1,
                20, 4, 0, 1);
        Recipe recipe = new Recipe("Tomato Soup", "desc",
                300, 60, 12, 0, 3,
                2, List.of(new RecipeItem("Tomato",
                1.5)));

        tracker.addItem(food);
        tracker.addItem(recipe);

        when(input.readLine())
                .thenReturn("2025-05-01")
                .thenReturn("Lunch")
                .thenReturn("1")
                .thenReturn("2");

        LogRecipeCommand cmd = new LogRecipeCommand(tracker, input, out);
        cmd.execute();

        List<FoodLog> logs = tracker.getFoodLogsForDate(LocalDate.of(2025,
                5, 1));
        assert logs.size() == 1;
        FoodLog fl = logs.get(0);
        assert fl.foodName().equals("Tomato");
        assert fl.meal().equals("Lunch");
        assert fl.totalGrams() == 300.0;
        assert fl.totalCalories() == 60;
    }

    @Test
    void testInvalidRecipeId() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        InputProvider input = mock(InputProvider.class);
        OutputWriter out = mock(OutputWriter.class);

        tracker.addItem(new Recipe("InvalidRecipe", "",
                0, 0, 0, 0,
                0, 1, List.of()));

        when(input.readLine())
                .thenReturn("2025-05-01")
                .thenReturn("Dinner")
                .thenReturn("5");

        LogRecipeCommand cmd = new LogRecipeCommand(tracker, input, out);
        assertThrows(IllegalArgumentException.class, cmd::execute);

    }

    @Test
    void testMissingFoodInRecipe() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        InputProvider input = mock(InputProvider.class);
        OutputWriter out = mock(OutputWriter.class);

        Recipe recipe = new Recipe("BrokenRecipe", "desc",
                0, 0, 0, 0, 0, 1,
                List.of(new RecipeItem("MissingFood", 2)));
        tracker.addItem(recipe);

        when(input.readLine())
                .thenReturn("2025-05-12")
                .thenReturn("Lunch")
                .thenReturn("1")
                .thenReturn("1");

        LogRecipeCommand cmd = new LogRecipeCommand(tracker, input, out);
        assertThrows(IllegalArgumentException.class, cmd::execute);
    }
}
