package myfitnesspal.commands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.viewcommands.ViewAllRecipesCommand;
import myfitnesspal.items.Recipe;
import myfitnesspal.items.RecipeItem;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ViewAllRecipesCommandTest {

    private MyFitnessTracker tracker;
    private OutputWriter outputWriter;

    @BeforeEach
    void setUp() {
        tracker = mock(MyFitnessTracker.class);
        outputWriter = mock(OutputWriter.class);
    }

    @Test
    void testExecuteNoRecipes() {
        when(tracker.getRecipes()).thenReturn(List.of());

        ViewAllRecipesCommand command =
                new ViewAllRecipesCommand(tracker, outputWriter);
        command.execute();

        verify(outputWriter).writeln(">11. View All Recipes");
        verify(outputWriter).writeln("No recipes found.");
        verifyNoMoreInteractions(outputWriter);
    }

    @Test
    void testExecuteWithRecipes() {
        Recipe recipe1 = new Recipe(
                "Fruit Salad", "Fresh mix",
                2,
                250,
                200,
                50,
                2,
                1,
                List.of(new RecipeItem("Apple", 1),
                        new RecipeItem("Banana", 1))
        );

        Recipe recipe2 = new Recipe(
                "Veg Soup", "Warm & hearty",
                3,
                300,
                150,
                30,
                5,
                3,
                List.of(new RecipeItem("Carrot", 1),
                        new RecipeItem("Potato", 1))
        );

        when(tracker.getRecipes()).thenReturn(List.of(recipe1, recipe2));

        ViewAllRecipesCommand command = new ViewAllRecipesCommand(
                tracker, outputWriter);
        command.execute();

        verify(outputWriter).writeln(">11. View All Recipes");
        verify(outputWriter).write("1. Fruit Salad (2 servings,"
                + " 250 units, 200 kcal; 50.00g, 2.00g, 1.00g)");
        verify(outputWriter).write("2. Veg Soup (3 servings, 300 units,"
                + " 150 kcal; 30.00g, 5.00g, 3.00g)");
        verifyNoMoreInteractions(outputWriter);
    }
}
