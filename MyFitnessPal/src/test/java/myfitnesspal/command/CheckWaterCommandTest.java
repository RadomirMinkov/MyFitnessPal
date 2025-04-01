package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;

import myfitnesspal.WaterIntake;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


class CheckWaterCommandTest {

    @Test
    void testExecuteFoundWater() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        tracker.addItem(new WaterIntake(LocalDate.of(
                2025, 3, 19), 500));

        InputProvider inputProvider = mock(InputProvider.class);
        OutputWriter outputWriter = mock(OutputWriter.class);

        when(inputProvider.readLine()).thenReturn("2025-03-19");

        CheckWaterCommand cmd = new CheckWaterCommand(
                tracker, inputProvider, outputWriter);

        cmd.execute();

        verify(inputProvider, times(1)).readLine();

        verify(outputWriter).write(">When? -");
        verify(outputWriter).write("2025-03-19:");
        verify(outputWriter).write("-> 500 ml");

        verifyNoMoreInteractions(outputWriter);
    }

    @Test
    void testExecuteNoWater() {
        MyFitnessTracker tracker = new MyFitnessTracker();

        InputProvider inputProvider = mock(InputProvider.class);
        OutputWriter outputWriter = mock(OutputWriter.class);

        when(inputProvider.readLine()).thenReturn("2025-03-19");

        CheckWaterCommand cmd = new CheckWaterCommand(
                tracker, inputProvider, outputWriter);

        cmd.execute();

        verify(inputProvider, times(1)).readLine();
        verify(outputWriter).write(">When? -");
        verify(outputWriter).write("2025-03-19: No water intake recorded.");
        verifyNoMoreInteractions(outputWriter);
    }
}
