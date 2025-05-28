package myfitnesspal.commands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.logcommands.LogMealCommand;
import myfitnesspal.items.Food;
import myfitnesspal.items.FoodLog;
import myfitnesspal.items.MeasurementType;
import myfitnesspal.items.Meal;
import myfitnesspal.items.MealItem;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LogMealCommandTest {

    @Test
    void testExecuteSuccess() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        InputProvider input = mock(InputProvider.class);
        OutputWriter output = mock(OutputWriter.class);

        Food foodA = new Food("FoodA", "desc",
                MeasurementType.GRAM, 100,
                1, 200, 20, 10, 5);
        Food foodB = new Food("FoodB", "desc",
                MeasurementType.GRAM, 50,
                1, 100, 10, 2, 3);
        when(tracker.getFoods()).thenReturn(List.of(foodA, foodB));

        MealItem item1 = new MealItem("FoodA", 1.0);
        MealItem item2 = new MealItem("FoodB", 0.5);
        Meal meal = new Meal("MixedMeal", "desc",
                150, 300, 30, 15,
                8, List.of(item1, item2));
        when(tracker.getMeals()).thenReturn(List.of(meal));

        when(input.readLine()).thenReturn("2025-05-01",
                "Lunch", "1", "2");

        LogMealCommand cmd = new LogMealCommand(tracker, input, output);
        cmd.execute();

        verify(tracker, times(2))
                .addItem(any(FoodLog.class));
        verify(output)
                .write("Logged meal successfully: MixedMeal x 2.0 serving(s).");
    }

    @Test
    void testNoMealsThrows() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        when(tracker.getMeals()).thenReturn(List.of());
        InputProvider input = mock(InputProvider.class);
        when(input.readLine()).thenReturn("2025-05-01", "Lunch");
        OutputWriter output = mock(OutputWriter.class);

        LogMealCommand cmd = new LogMealCommand(tracker,
                input, output);
        Assertions.assertThrows(IllegalArgumentException.class,
                cmd::execute);
    }

    @Test
    void testInvalidMealIdThrows() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        Meal meal = new Meal("MealX", "desc",
                100, 200, 20, 10,
                5, List.of());
        when(tracker.getMeals()).thenReturn(List.of(meal));

        InputProvider input = mock(InputProvider.class);
        when(input.readLine()).thenReturn("2025-05-01",
                "Lunch", "5", "1");

        OutputWriter output = mock(OutputWriter.class);
        LogMealCommand cmd = new LogMealCommand(tracker, input, output);

        Assertions.assertThrows(IllegalArgumentException.class,
                cmd::execute);
    }

    @Test
    void testUnknownFoodThrows() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        MealItem item = new MealItem("UnknownFood", 1);
        Meal meal = new Meal("MealX", "",
                100, 200, 20,
                10, 5, List.of(item));
        when(tracker.getMeals()).thenReturn(List.of(meal));
        when(tracker.getFoods()).thenReturn(List.of());

        InputProvider input = mock(InputProvider.class);
        when(input.readLine()).thenReturn("2025-05-01",
                "Lunch", "1", "1");

        OutputWriter output = mock(OutputWriter.class);
        LogMealCommand cmd = new LogMealCommand(tracker, input, output);

        Assertions.assertThrows(IllegalArgumentException.class, cmd::execute);
    }
}
