package myfitnesspal.command;

import myfitnesspal.Meal;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ViewAllMealsCommandTest {

    @Test
    void testExecuteNoMeals() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        OutputWriter outputWriter = mock(OutputWriter.class);

        when(tracker.getMeals()).thenReturn(new ArrayList<>());

        ViewAllMealsCommand command =
                new ViewAllMealsCommand(tracker, outputWriter);
        command.execute();

        verify(outputWriter).write(">8. View All Meals");
        verify(outputWriter).write("No meals found.");
    }

    @Test
    void testExecuteWithMeals() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        OutputWriter outputWriter = mock(OutputWriter.class);

        Meal meal1 = mock(Meal.class);
        Meal meal2 = mock(Meal.class);

        List<Meal> mealList = new ArrayList<>();
        mealList.add(meal1);
        mealList.add(meal2);

        when(tracker.getMeals()).thenReturn(mealList);
        when(meal1.toString()).thenReturn("MealOne");
        when(meal2.toString()).thenReturn("MealTwo");

        ViewAllMealsCommand command =
                new ViewAllMealsCommand(tracker, outputWriter);
        command.execute();

        verify(outputWriter).write(">8. View All Meals");
        verify(outputWriter).write("1. MealOne");
        verify(outputWriter).write("2. MealTwo");
    }
}
