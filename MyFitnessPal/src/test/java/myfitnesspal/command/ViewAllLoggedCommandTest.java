package myfitnesspal.command;

import myfitnesspal.FoodLog;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.WaterIntake;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ViewAllLoggedCommandTest {

    private MyFitnessTracker tracker;
    private InputProvider inputProvider;
    private OutputWriter outputWriter;
    private ViewAllLoggedCommand command;

    @BeforeEach
    void setUp() {
        tracker = mock(MyFitnessTracker.class);
        inputProvider = mock(InputProvider.class);
        outputWriter = mock(OutputWriter.class);
        command =
                new ViewAllLoggedCommand(tracker, inputProvider, outputWriter);
    }

    @Test
    void testExecuteWithLogsNoWater() {
        when(inputProvider.readLine()).thenReturn("2025-04-01");

        List<FoodLog> logs = new ArrayList<>();
        logs.add(new FoodLog(LocalDate
                .of(2025, 4, 1),
                "Lunch", "Bread",
                100, 250,
                50, 5, 8));
        logs.add(new FoodLog(LocalDate.of(2025,
                4, 1),
                "Dinner", "Apple",
                150, 78,
                20, 0.5, 1));

        when(tracker.getFoodLogsForDate(LocalDate.of(
                2025, 4, 1)))
                .thenReturn(logs);

        List<WaterIntake> waterIntakes = new ArrayList<>();
        when(tracker.getWaterIntakes()).thenReturn(waterIntakes);

        command.execute();

        verify(outputWriter).write(">6. View All Logged\n");
        verify(outputWriter).write(">When (date):\n-");
        verify(outputWriter).write("\nBreakfast: —");
        verify(outputWriter).write("\nLunch:");
        verify(outputWriter)
                .write(">100g x Bread"
                        + " (Total: 100g; 250 kcal; 50.00g, 5.00g, 8.00g)");
        verify(outputWriter).write("\nSnacks: —");
        verify(outputWriter).write("\nDinner:");
        verify(outputWriter)
                .write(">150g x Apple (Total:"
                        + " 150g; 78 kcal; 20.00g, 0.50g, 1.00g)");
        verify(outputWriter).write("\nWater: No water logged");
        verifyNoMoreInteractions(outputWriter);
    }

    @Test
    void testExecuteNoLogs() {
        when(inputProvider.readLine()).thenReturn("2025-04-01");
        when(tracker.getFoodLogsForDate(
                LocalDate.of(2025, 4, 1)))
                .thenReturn(new ArrayList<>());
        when(tracker.getWaterIntakes()).thenReturn(new ArrayList<>());

        command.execute();

        verify(outputWriter).write(">6. View All Logged\n");
        verify(outputWriter).write(">When (date):\n-");
        verify(outputWriter).write("No foods logged for 2025-04-01");
        verify(outputWriter).write("\nWater: No water logged");
        verifyNoMoreInteractions(outputWriter);
    }

    @Test
    void testExecuteWithLogsAndWater() {
        when(inputProvider.readLine()).thenReturn("2025-04-02");

        List<FoodLog> logs = new ArrayList<>();
        logs.add(new FoodLog(LocalDate.of(2025, 4, 2),
                "Breakfast", "Oatmeal",
                50, 180,
                30, 3, 6));
        when(tracker.getFoodLogsForDate(LocalDate
                .of(2025, 4, 2)))
                .thenReturn(logs);

        List<WaterIntake> waterIntakes = new ArrayList<>();
        waterIntakes.add(new WaterIntake(LocalDate
                .of(2025, 4, 2), 1000));
        waterIntakes.add(new WaterIntake(LocalDate
                .of(2025, 4, 2), 500));
        when(tracker.getWaterIntakes()).thenReturn(waterIntakes);

        command.execute();

        verify(outputWriter).write(">6. View All Logged\n");
        verify(outputWriter).write(">When (date):\n-");
        verify(outputWriter).write("\nBreakfast:");
        verify(outputWriter)
                .write(">50g x Oatmeal "
                        + "(Total: 50g; 180 kcal; 30.00g, 3.00g, 6.00g)");
        verify(outputWriter).write("\nLunch: —");
        verify(outputWriter).write("\nSnacks: —");
        verify(outputWriter).write("\nDinner: —");
        verify(outputWriter).write("\nWater: 1.5L");
        verifyNoMoreInteractions(outputWriter);
    }
}
