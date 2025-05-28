package myfitnesspal.commands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.logcommands.LogBodyMeasurementCommand;
import myfitnesspal.items.BodyMeasurement;
import myfitnesspal.items.BodyMeasurementMetric;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LogBodyMeasurementCommandTest {

    @Test
    void testExecuteWeight() {
        MyFitnessTracker tracker = new MyFitnessTracker();

        InputProvider in = mock(InputProvider.class);
        OutputWriter out = mock(OutputWriter.class);

        when(in.readLine())
                .thenReturn("weight")
                .thenReturn("kg")
                .thenReturn("2025-05-10")
                .thenReturn("82");

        LogBodyMeasurementCommand cmd =
                new LogBodyMeasurementCommand(tracker, in, out);
        cmd.execute();

        List<BodyMeasurement> list = tracker.getBodyMeasurements();
        Assertions.assertEquals(1, list.size());

        BodyMeasurement bm = list.get(0);
        Assertions.assertEquals(LocalDate.of(2025,
                5, 10), bm.date());
        Assertions.assertEquals(BodyMeasurementMetric.WEIGHT, bm.metric());
        Assertions.assertEquals("kg", bm.unit());
        Assertions.assertEquals(82.0, bm.values()
                .get("value"), 0.001);

        verify(out).writeln(">13. Log body measurement:");
        verify(out).writeln("Done!");
    }

    @Test
    void testExecuteBiceps() {
        MyFitnessTracker tracker = new MyFitnessTracker();

        InputProvider in = mock(InputProvider.class);
        OutputWriter out = mock(OutputWriter.class);

        when(in.readLine())
                .thenReturn("biceps")
                .thenReturn("cm")
                .thenReturn("2025-05-12")
                .thenReturn("34.1")
                .thenReturn("34.5");

        LogBodyMeasurementCommand cmd =
                new LogBodyMeasurementCommand(tracker, in, out);
        cmd.execute();

        BodyMeasurement bm = tracker.getBodyMeasurements().get(0);
        Assertions.assertEquals(34.1,
                bm.values().get("left"), 0.001);
        Assertions.assertEquals(34.5,
                bm.values().get("right"), 0.001);
    }

    @Test
    void testExecuteInvalidMetric() {
        MyFitnessTracker tracker = new MyFitnessTracker();

        InputProvider in = mock(InputProvider.class);
        OutputWriter out = mock(OutputWriter.class);

        when(in.readLine()).thenReturn("invalid");

        LogBodyMeasurementCommand cmd =
                new LogBodyMeasurementCommand(tracker, in, out);

        Assertions.assertThrows(IllegalArgumentException.class,
                cmd::execute);
    }
}
