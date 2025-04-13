package myfitnesspal.command;

import myfitnesspal.FoodLog;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.Recipe;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LogRecipeCommandTest {

    private MyFitnessTracker tracker;
    private InputProvider inputProvider;
    private OutputWriter outputWriter;
    private LogRecipeCommand command;

    @BeforeEach
    void setUp() {
        tracker = mock(MyFitnessTracker.class);
        inputProvider = mock(InputProvider.class);
        outputWriter = mock(OutputWriter.class);
        command = new LogRecipeCommand(
                tracker, inputProvider,
                outputWriter, "file.txt");
    }

    @Test
    void testExecuteNoRecipes() {
        when(tracker.getRecipes()).thenReturn(new ArrayList<>());
        when(inputProvider.readLine())
                .thenReturn("2025-05-01")
                .thenReturn("Breakfast");

        Assertions.assertThrows(IllegalArgumentException.class,
                command::execute,
                "No recipes in the system. Please create a recipe first.");
        verify(outputWriter, atLeastOnce()).write(anyString());
        verify(tracker, never()).addItem(any());
    }

    @Test
    void testExecuteInvalidMealType() {
        Recipe r = mock(Recipe.class);
        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(r);
        when(tracker.getRecipes()).thenReturn(recipeList);
        when(inputProvider.readLine())
                .thenReturn("2025-05-10")
                .thenReturn("")
                .thenReturn("1")
                .thenReturn("1");

        Assertions.assertThrows(IllegalArgumentException.class,
                command::execute,
                "Invalid meal type!");
        verify(tracker, never()).addItem(any());
    }

    @Test
    void testExecuteInvalidRecipeId() {
        Recipe r = mock(Recipe.class);
        when(tracker.getRecipes()).thenReturn(List.of(r));
        when(inputProvider.readLine())
                .thenReturn("2025-05-02")
                .thenReturn("Lunch")
                .thenReturn("99");

        Assertions.assertThrows(IllegalArgumentException.class,
                command::execute,
                "Invalid recipe ID.");
        verify(tracker, never()).addItem(any());
    }

    @Test
    void testExecuteInvalidServings() {
        Recipe r = new Recipe("TestRecipe", "",
                2, 200, 400,
                20, 10, 6, List.of());
        when(tracker.getRecipes()).thenReturn(List.of(r));
        when(inputProvider.readLine())
                .thenReturn("2025-01-01")
                .thenReturn("Dinner")
                .thenReturn("1")
                .thenReturn("0");

        Assertions.assertThrows(IllegalArgumentException.class,
                command::execute,
                "Servings must be > 0");
        verify(tracker, never()).addItem(any());
    }

    @Test
    void testExecuteSuccess() {
        Recipe r = new Recipe("Banitsa", "desc", 4,
                400, 800, 100,
                20, 10,
                List.of());
        when(tracker.getRecipes()).thenReturn(List.of(r));
        when(inputProvider.readLine())
                .thenReturn("2025-04-15")
                .thenReturn("Breakfast")
                .thenReturn("1")
                .thenReturn("2");

        command.execute();

        ArgumentCaptor<FoodLog> captor = ArgumentCaptor.forClass(FoodLog.class);
        verify(tracker).addItem(captor.capture());
        FoodLog log = captor.getValue();
        Assertions.assertEquals(LocalDate.of(2025, 4,
                15), log.date());
        Assertions.assertEquals("Breakfast", log.meal());
        Assertions.assertEquals("Banitsa", log.foodName());
        Assertions.assertEquals(200, log.totalGrams(), 0.001);
        Assertions.assertEquals(400, log.totalCalories(), 0.001);
        Assertions.assertEquals(50, log.totalCarbs(), 0.001);
        Assertions.assertEquals(10, log.totalFat(), 0.001);
        Assertions.assertEquals(5, log.totalProtein(), 0.001);

        verify(outputWriter, atLeastOnce()).write(anyString());
    }
}
