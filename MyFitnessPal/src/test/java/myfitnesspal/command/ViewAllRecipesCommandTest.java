package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.Recipe;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ViewAllRecipesCommandTest {

    private MyFitnessTracker tracker;
    private OutputWriter outputWriter;
    private ViewAllRecipesCommand command;

    @BeforeEach
    void setUp() {
        tracker = mock(MyFitnessTracker.class);
        outputWriter = mock(OutputWriter.class);
        command = new ViewAllRecipesCommand(tracker, outputWriter);
    }

    @Test
    void testExecuteNoRecipes() {
        when(tracker.getRecipes()).thenReturn(new ArrayList<>());
        command.execute();
        verify(outputWriter).writeln(">11. View All Recipes");
        verify(outputWriter).writeln("No recipes found.");
        verifyNoMoreInteractions(outputWriter);
    }

    @Test
    void testExecuteWithRecipes() {
        Recipe r1 = mock(Recipe.class);
        Recipe r2 = mock(Recipe.class);
        List<Recipe> list = new ArrayList<>();
        list.add(r1);
        list.add(r2);
        when(tracker.getRecipes()).thenReturn(list);
        when(r1.toString()).thenReturn("Banitsa");
        when(r2.toString()).thenReturn("Salad");

        command.execute();

        verify(outputWriter).writeln(">11. View All Recipes");
        verify(outputWriter).write("1. Banitsa");
        verify(outputWriter).write("2. Salad");
        verifyNoMoreInteractions(outputWriter);
    }
}
