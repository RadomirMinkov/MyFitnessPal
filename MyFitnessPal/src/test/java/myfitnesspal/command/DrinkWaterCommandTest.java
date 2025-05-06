package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.items.MeasurementType;
import myfitnesspal.items.WaterIntake;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DrinkWaterCommandTest {

    @Test
    void testExecuteValidInputWithMl() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        InputProvider input = mock(InputProvider.class);
        OutputWriter output = mock(OutputWriter.class);

        when(input.readLine())
                .thenReturn("2025-03-19")
                .thenReturn("2")
                .thenReturn("500");

        DrinkWaterCommand cmd = new DrinkWaterCommand(tracker, input, output);
        cmd.execute();

        WaterIntake wi = tracker.getWaterIntakes().get(0);
        Assertions.assertEquals(LocalDate.of(2025, 3, 19), wi.date());
        Assertions.assertEquals(500, wi.amount());
        Assertions.assertEquals(MeasurementType.MILLILITER,
                wi.measurementType());

        verify(output).write("Water intake recorded successfully!");
    }

    @Test
    void testExecuteValidInputWithPieces() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        InputProvider input = mock(InputProvider.class);
        OutputWriter output = mock(OutputWriter.class);

        when(input.readLine())
                .thenReturn("2025-03-19")
                .thenReturn("3")
                .thenReturn("250")
                .thenReturn("2");

        DrinkWaterCommand cmd = new DrinkWaterCommand(tracker, input, output);
        cmd.execute();

        WaterIntake wi = tracker.getWaterIntakes().get(0);
        Assertions.assertEquals(LocalDate.of(2025, 3, 19), wi.date());
        Assertions.assertEquals(500, wi.amount());
        Assertions.assertEquals(MeasurementType.MILLILITER,
                wi.measurementType());

        verify(output).write("Water intake recorded successfully!");
    }

    @Test
    void testExecuteInvalidAmountInput() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        InputProvider input = mock(InputProvider.class);
        OutputWriter output = mock(OutputWriter.class);

        when(input.readLine())
                .thenReturn("2025-03-19")
                .thenReturn("2")
                .thenReturn("invalid");

        DrinkWaterCommand cmd = new DrinkWaterCommand(tracker,
                input, output);

        Assertions.assertThrows(IllegalArgumentException.class, cmd::execute);
        Assertions.assertTrue(tracker.getWaterIntakes().isEmpty());
    }
}
