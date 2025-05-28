package myfitnesspal.commands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.viewcommands.ViewAllLoggedCommand;
import myfitnesspal.items.FoodLog;
import myfitnesspal.items.MeasurementType;
import myfitnesspal.items.WaterIntake;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ViewAllLoggedCommandTest {

    private MyFitnessTracker tracker;
    private InputProvider input;
    private OutputWriter out;

    @BeforeEach
    void setup() {
        tracker = mock(MyFitnessTracker.class);
        input = mock(InputProvider.class);
        out = mock(OutputWriter.class);
    }

    @Test
    void testExecuteWithData() {
        when(input.readLine()).thenReturn("2025-04-01");
        LocalDate date = LocalDate.of(2025, 4, 1);

        List<FoodLog> logs = List.of(
                new FoodLog(date, "Lunch", "Bread",
                        100, 250, 50,
                        5, 8),
                new FoodLog(date, "Lunch", "Apple",
                        150, 78, 20,
                        0.5, 1)
        );
        List<WaterIntake> water = List.of(
                new WaterIntake(date, MeasurementType.MILLILITER, 1000),
                new WaterIntake(date, MeasurementType.MILLILITER, 500)
        );

        when(tracker.getFoodLogsForDate(date)).thenReturn(logs);
        when(tracker.getWaterIntakes()).thenReturn(water);

        new ViewAllLoggedCommand(tracker, input, out).execute();

        verify(out).write(contains(">6. View All Logged"));
        verify(out).write(contains(">When (date):"));
        verify(out).write(contains("Lunch:"));
        verify(out).write(contains("Bread"));
        verify(out).write(contains("Apple"));
        verify(out).write(contains("Water:"));
        verify(out).write(contains("1000"));
        verify(out).write(contains("500"));
    }
}
