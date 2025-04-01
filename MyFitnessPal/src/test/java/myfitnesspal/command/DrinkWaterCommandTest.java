package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.WaterIntake;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class DrinkWaterCommandTest {

    @Test
    void testExecuteValidInput() {
        MyFitnessTracker tracker = new MyFitnessTracker();

        InputProvider inputProvider = mock(InputProvider.class);
        when(inputProvider.readLine())
                .thenReturn("2025-03-19")
                .thenReturn("500");

        OutputWriter outputWriter = mock(OutputWriter.class);

        DrinkWaterCommand cmd = new DrinkWaterCommand(
                tracker, inputProvider, outputWriter, "testfile.txt"
        );

        cmd.execute();

        Assertions.assertEquals(1, tracker.getWaterIntakes().size());
        WaterIntake wi = tracker.getWaterIntakes().get(0);
        Assertions.assertEquals(LocalDate.of(2025, 3, 19), wi.date());
        Assertions.assertEquals(500, wi.amount());

        verify(outputWriter).write(">When? -");
        verify(outputWriter).write("\n>How much?(ml) -");
        verify(outputWriter).write("\n>Water intake recorded successfully!");

        verifyNoMoreInteractions(outputWriter);
    }

    @Test
    void testExecuteInvalidAmount() {
        MyFitnessTracker tracker = new MyFitnessTracker();

        InputProvider inputProvider = mock(InputProvider.class);
        when(inputProvider.readLine())
                .thenReturn("2025-03-19")
                .thenReturn("notAnInt");

        OutputWriter outputWriter = mock(OutputWriter.class);

        DrinkWaterCommand cmd = new DrinkWaterCommand(
                tracker, inputProvider, outputWriter, "testfile.txt"
        );

        Assertions.assertThrows(IllegalArgumentException.class, cmd::execute);

        verify(outputWriter).write(">When? -");
        verify(outputWriter).write("\n>How much?(ml) -");
        verifyNoMoreInteractions(outputWriter);
        Assertions.assertTrue(tracker.getWaterIntakes().isEmpty());
    }
}
