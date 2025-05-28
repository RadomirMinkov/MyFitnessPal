package myfitnesspal.commands.viewcommands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.Command;
import myfitnesspal.graphic.ChartFileUtils;
import myfitnesspal.items.BodyMeasurement;
import myfitnesspal.items.BodyMeasurementMetric;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.parser.Parser;
import myfitnesspal.utility.PromptUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

public final class MeasurementReportCommand implements Command {
    private final MyFitnessTracker tracker;
    private final InputProvider in;
    private final OutputWriter out;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public MeasurementReportCommand(MyFitnessTracker t,
                                    InputProvider in,
                                    OutputWriter out) {
        this.tracker = t;
        this.in = in;
        this.out = out;
    }

    @Override
    public void execute() {
        out.write(">14. Measurement reports:\n");
        BodyMeasurementMetric bodyMeasurementMetric = promptMetric();
        LocalDate[] range = promptRange();
        List<BodyMeasurement> list = tracker.getBodyMeasurements().stream()
                .filter(bodyMeasurement ->
                        bodyMeasurement.metric() == bodyMeasurementMetric
                                && !bodyMeasurement.date().isBefore(range[0])
                                && !bodyMeasurement.date().isAfter(range[1]))
                .sorted(Comparator.comparing(BodyMeasurement::date))
                .toList();
        print(list);

        if (!list.isEmpty()) {
            var file = ChartFileUtils.generateLineChart(
                    list, bodyMeasurementMetric
                            + " progress", list.get(0).unit());
            out.writeln("Chart saved: " + file.getAbsolutePath());
        }
    }

    private BodyMeasurementMetric promptMetric() {
        String s = PromptUtils.promptLine(in, out,
                ">Which body measurement (weight/hip/waist/thigh/biceps):");
        return BodyMeasurementMetric.fromString(s);
    }

    private LocalDate[] promptRange() {
        String t = PromptUtils.promptLine(in, out,
                        ">What timeline?"
                                + " (week, month or period d/m/y-d/m/y):")
                .trim().toLowerCase();
        LocalDate now = LocalDate.now();
        if (t.equals("week")) {
            return new LocalDate[]{now.minusWeeks(1), now};
        }

        if (t.equals("month")) {
            return new LocalDate[]{now.minusMonths(1), now};
        }
        String[] p = t.split("-");
        return new LocalDate[]{Parser.parseDate(p[0]), Parser.parseDate(p[1])};
    }

    private void print(List<BodyMeasurement> measurements) {
        Double previousValue = null;
        for (BodyMeasurement measurement : measurements) {
            double currentValue =
                    measurement.values().values().iterator().next();
            double change = previousValue
                    == null ? 0 : currentValue - previousValue;
            out.write(String.format("   %s - %.1f%s (%+.1f)%n",
                    measurement.date().format(FORMATTER),
                    currentValue, measurement.unit(), change));
            previousValue = currentValue;
        }
    }
}
