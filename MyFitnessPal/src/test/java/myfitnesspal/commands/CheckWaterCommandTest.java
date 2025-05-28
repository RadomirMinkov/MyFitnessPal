package myfitnesspal.commands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.viewcommands.CheckWaterCommand;
import myfitnesspal.items.MeasurementType;
import myfitnesspal.items.WaterIntake;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class CheckWaterCommandTest {

    @Test
    void testExecuteWithWater() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        tracker.addItem(new WaterIntake(
                LocalDate.of(2025, 5, 6),
                MeasurementType.PIECE,
                750
        ));
        tracker.addItem(new WaterIntake(
                LocalDate.of(2025, 5, 6),
                MeasurementType.MILLILITER,
                500
        ));

        InputProvider input = mock(InputProvider.class);
        OutputWriter output = mock(OutputWriter.class);

        when(input.readLine()).thenReturn("2025-05-06");

        CheckWaterCommand command =
                new CheckWaterCommand(tracker, input, output);
        command.execute();

        verify(output).write(">When?\n-");
        verify(output).write("2025-05-06:");
        verify(output).write("-> 750.0 pcs");
        verify(output).write("-> 500.0 ml");
        verifyNoMoreInteractions(output);
    }

    @Test
    void testExecuteNoWater() {
        MyFitnessTracker tracker = new MyFitnessTracker();

        InputProvider input = mock(InputProvider.class);
        OutputWriter output = mock(OutputWriter.class);

        when(input.readLine()).thenReturn("2025-05-06");

        CheckWaterCommand command =
                new CheckWaterCommand(tracker, input, output);
        command.execute();

        verify(output).write(">When?\n-");
        verify(output).write("2025-05-06: No water intake recorded.");
        verifyNoMoreInteractions(output);
    }
}
