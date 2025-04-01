package myfitnesspal.command;

import myfitnesspal.Food;
import myfitnesspal.FoodLog;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LogFoodCommandTest {

    @Test
    void testExecuteSuccessfulLog() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        InputProvider inputProvider = mock(InputProvider.class);
        OutputWriter outputWriter = mock(OutputWriter.class);

        List<Food> foods = new ArrayList<>();
        foods.add(new Food("Apple", "Juicy fruit",
                100, 1,
                52, 14, 0.2, 0.3));
        foods.add(new Food("Bread", "Whole wheat",
                50, 4,
                120, 25, 2, 4));
        when(tracker.getFoods()).thenReturn(foods);

        when(inputProvider.readLine())
                .thenReturn("2")
                .thenReturn("2")
                .thenReturn("2025-04-01")
                .thenReturn("Lunch");

        LogFoodCommand command = new LogFoodCommand(
                tracker, inputProvider, outputWriter, "testfile.txt"
        );
        command.execute();

        verify(outputWriter).write(">5. Log Food\n");
        verify(outputWriter).write("1. " + foods.get(0));
        verify(outputWriter).write("2. " + foods.get(1));
        verify(outputWriter).write(">Which food (food id):\n-");
        verify(outputWriter).write("(Either)\n>Number of serving(s):\n-");
        verify(outputWriter).write(">When (date):\n-");
        verify(outputWriter).write(">When (meal) "
                + "[Breakfast/Lunch/Snacks/Dinner]:\n-");
        verify(outputWriter).write(startsWith("Logged successfully:\n"));

        verify(tracker).addItem(argThat(item -> {
            if (!(item instanceof FoodLog fl)) {
                return false;
            }
            return fl.foodName().equals("Bread")
                    && fl.date().equals(LocalDate.of(
                            2025, 4, 1))
                    && fl.meal().equals("Lunch");
        }));

    }

    @Test
    void testExecuteNoFoods() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        InputProvider inputProvider = mock(InputProvider.class);
        OutputWriter outputWriter = mock(OutputWriter.class);

        when(tracker.getFoods()).thenReturn(new ArrayList<>());
        when(inputProvider.readLine()).thenReturn("1");

        LogFoodCommand command = new LogFoodCommand(
                tracker, inputProvider, outputWriter, "testfile.txt"
        );
        Assertions.assertThrows(
                IllegalArgumentException.class, command::execute);

        verify(outputWriter).write(">5. Log Food\n");
    }

    @Test
    void testExecuteInvalidFoodId() {
        MyFitnessTracker tracker = mock(MyFitnessTracker.class);
        InputProvider inputProvider = mock(InputProvider.class);
        OutputWriter outputWriter = mock(OutputWriter.class);

        List<Food> foods = new ArrayList<>();
        foods.add(new Food("TestFood", "desc",
                100, 1,
                100, 10, 1, 2));
        when(tracker.getFoods()).thenReturn(foods);

        when(inputProvider.readLine())
                .thenReturn("5")
                .thenReturn("1")
                .thenReturn("2025-04-01")
                .thenReturn("Breakfast");

        LogFoodCommand command = new LogFoodCommand(
                tracker, inputProvider, outputWriter, "file.txt"
        );
        Assertions.assertThrows(
                IllegalArgumentException.class, command::execute);

        verify(outputWriter).write(">5. Log Food\n");
        verify(outputWriter).write("1. " + foods.get(0));
        verify(outputWriter).write(">Which food (food id):\n-");

    }
}
