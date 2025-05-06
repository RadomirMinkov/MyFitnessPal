package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.items.Meal;
import myfitnesspal.items.MealItem;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ViewAllMealsCommandTest {

    private MyFitnessTracker tracker;
    private OutputWriter outputWriter;

    @BeforeEach
    void setUp() {
        tracker = mock(MyFitnessTracker.class);
        outputWriter = mock(OutputWriter.class);
    }

    @Test
    void testExecuteNoMeals() {
        when(tracker.getMeals()).thenReturn(List.of());

        ViewAllMealsCommand cmd =
                new ViewAllMealsCommand(tracker, outputWriter);
        cmd.execute();

        verify(outputWriter).write(">8. View All Meals");
        verify(outputWriter).write("No meals found.");
        verifyNoMoreInteractions(outputWriter);
    }

    @Test
    void testExecuteWithMeals() {
        Meal meal = new Meal(
                "Omelette",
                "Eggs and cheese",
                250,
                500,
                2,
                40,
                25,
                List.of(new MealItem("Egg", 2),
                        new MealItem("Cheese", 1))
        );

        when(tracker.getMeals()).thenReturn(List.of(meal));

        ViewAllMealsCommand cmd =
                new ViewAllMealsCommand(tracker, outputWriter);
        cmd.execute();

        verify(outputWriter).write(">8. View All Meals");
        verify(outputWriter).write(
                "1. Omelette (250 units, 500 kcal; 2.00g, 40.00g, 25.00g)");
        verifyNoMoreInteractions(outputWriter);
    }
}
