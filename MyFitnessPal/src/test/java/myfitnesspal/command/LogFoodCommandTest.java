package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.items.Food;
import myfitnesspal.items.FoodLog;
import myfitnesspal.items.MeasurementType;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LogFoodCommandTest {

    @Test
    void testExecutePerUnitLogging() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        Food food = new Food("Milk", "desc",
                MeasurementType.MILLILITER, 100, 1,
                80, 5, 4, 3);
        tracker.addItem(food);

        InputProvider input = mock(InputProvider.class);
        OutputWriter out = mock(OutputWriter.class);

        when(input.readLine())
                .thenReturn("1")
                .thenReturn("2025-05-01")
                .thenReturn("Breakfast")
                .thenReturn("1")
                .thenReturn("200");

        LogFoodCommand command = new LogFoodCommand(tracker, input, out);
        command.execute();

        List<FoodLog> logs = tracker.getFoodLogs();
        Assertions.assertEquals(1, logs.size());
        FoodLog log = logs.get(0);

        Assertions.assertEquals("Milk", log.foodName());
        Assertions.assertEquals(LocalDate.of(2025, 5,
                1), log.date());
        Assertions.assertEquals("Breakfast", log.meal());
        Assertions.assertEquals(200, log.totalGrams(), 0.001);
        Assertions.assertEquals(160, log.totalCalories(), 0.001);
        Assertions.assertEquals(10, log.totalCarbs(), 0.001);
        Assertions.assertEquals(8, log.totalFat(), 0.001);
        Assertions.assertEquals(6, log.totalProtein(), 0.001);

        verify(out).write(">5. Log Food\n");
        verify(out).write("1. " + food);
        verify(out).write("Logged successfully:\n" + log);
    }

    @Test
    void testExecutePerServingLogging() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        Food food = new Food("Yogurt", "desc",
                MeasurementType.GRAM, 150, 1,
                120, 10, 6, 5);
        tracker.addItem(food);

        InputProvider input = mock(InputProvider.class);
        OutputWriter out = mock(OutputWriter.class);

        when(input.readLine())
                .thenReturn("1")
                .thenReturn("2025-05-02")
                .thenReturn("Lunch")
                .thenReturn("2")
                .thenReturn("2");

        LogFoodCommand command = new LogFoodCommand(tracker,
                input, out);
        command.execute();

        List<FoodLog> logs = tracker.getFoodLogs();
        Assertions.assertEquals(1, logs.size());
        FoodLog log = logs.get(0);

        Assertions.assertEquals("Yogurt", log.foodName());
        Assertions.assertEquals(LocalDate.of(2025, 5, 2),
                log.date());
        Assertions.assertEquals("Lunch", log.meal());
        Assertions.assertEquals(300, log.totalGrams(), 0.001);
        Assertions.assertEquals(240, log.totalCalories(), 0.001);
        Assertions.assertEquals(20, log.totalCarbs(), 0.001);
        Assertions.assertEquals(12, log.totalFat(), 0.001);
        Assertions.assertEquals(10, log.totalProtein(), 0.001);

        verify(out).write(">5. Log Food\n");
        verify(out).write("1. " + food);
        verify(out).write("Logged successfully:\n" + log);
    }

    @Test
    void testExecuteWithInvalidFoodId() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        tracker.addItem(new Food("Banana", "desc",
                MeasurementType.PIECE, 1, 1,
                90, 23, 0.3, 1.1));

        InputProvider input = mock(InputProvider.class);
        OutputWriter out = mock(OutputWriter.class);

        when(input.readLine()).thenReturn("99");

        LogFoodCommand command = new LogFoodCommand(tracker, input, out);

        Assertions.assertThrows(IllegalArgumentException.class,
                command::execute);
    }

    @Test
    void testExecuteWithEmptyFoodList() {
        MyFitnessTracker tracker = new MyFitnessTracker();

        InputProvider input = mock(InputProvider.class);
        OutputWriter out = mock(OutputWriter.class);

        LogFoodCommand command = new LogFoodCommand(tracker, input, out);

        Assertions.assertThrows(IllegalArgumentException.class,
                command::execute);
    }
}
