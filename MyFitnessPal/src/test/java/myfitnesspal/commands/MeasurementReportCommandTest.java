package myfitnesspal.commands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.viewcommands.MeasurementReportCommand;
import myfitnesspal.graphic.ChartFileUtils;
import myfitnesspal.items.BodyMeasurement;
import myfitnesspal.items.BodyMeasurementMetric;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MeasurementReportCommandTest {

    @Test
    void testExecuteWeekGeneratesChart() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        tracker.addItem(measure(LocalDate.now()
                .minusDays(6), 80));
        tracker.addItem(measure(LocalDate.now()
                .minusDays(3), 82));
        tracker.addItem(measure(LocalDate.now(), 83));

        InputProvider in = mock(InputProvider.class);
        OutputWriter  out = mock(OutputWriter.class);

        when(in.readLine()).thenReturn("weight")
                .thenReturn("week");

        try (MockedStatic<ChartFileUtils> mocked =
                     mockStatic(ChartFileUtils.class)) {
            File fake = new File("chart.png");
            mocked.when(() -> ChartFileUtils
                            .generateLineChart(any(), any(), any()))
                    .thenReturn(fake);

            MeasurementReportCommand cmd =
                    new MeasurementReportCommand(tracker, in, out);
            cmd.execute();

            List<BodyMeasurement> list = tracker.getBodyMeasurements();
            Assertions.assertEquals(3, list.size());

            mocked.verify(() ->
                    ChartFileUtils.generateLineChart(any(),
                            contains("weight"), eq("kg")));
            verify(out).write(">14. Measurement reports:\n");
            verify(out).writeln("Chart saved: " + fake.getAbsolutePath());
        }
    }

    @Test
    void testExecuteEmptyRangeNoChart() {
        MyFitnessTracker tracker = new MyFitnessTracker();

        InputProvider in = mock(InputProvider.class);
        OutputWriter  out = mock(OutputWriter.class);

        when(in.readLine()).thenReturn("weight")
                .thenReturn("01.01.2024-02.01.2024");

        try (MockedStatic<ChartFileUtils> mocked =
                     mockStatic(ChartFileUtils.class)) {
            MeasurementReportCommand cmd =
                    new MeasurementReportCommand(tracker, in, out);
            cmd.execute();

            mocked.verifyNoInteractions();
            verify(out, never()).writeln(startsWith("Chart saved"));
        }
    }

    private BodyMeasurement measure(LocalDate d, double v) {
        return new BodyMeasurement(
                d, BodyMeasurementMetric.WEIGHT, "kg",
                Map.of("value", v));
    }
}
