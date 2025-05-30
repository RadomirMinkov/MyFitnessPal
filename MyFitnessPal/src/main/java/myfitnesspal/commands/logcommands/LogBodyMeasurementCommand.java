package myfitnesspal.commands.logcommands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.Command;
import myfitnesspal.items.BodyMeasurement;
import myfitnesspal.items.BodyMeasurementMetric;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.parser.Parser;
import myfitnesspal.utility.PromptUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public final class LogBodyMeasurementCommand implements Command {
    private final MyFitnessTracker tracker;
    private final InputProvider in;
    private final OutputWriter out;

    public LogBodyMeasurementCommand(MyFitnessTracker t,
                                     InputProvider in,
                                     OutputWriter out) {
        this.tracker = t;
        this.in = in;
        this.out = out;
    }

    @Override
    public void execute() {
        out.writeln(">13. Log body measurement:");
        BodyMeasurementMetric metric = promptMetric();

        String unit = switch (metric) {
            case WEIGHT -> PromptUtils.promptLine(in, out, ">Choose unit (kg):")
                    .toLowerCase();
            case HIP, WAIST, THIGH, BICEPS -> PromptUtils.promptLine(in, out,
                    ">Choose unit (cm or inch):").toLowerCase();
        };

        if (metric == BodyMeasurementMetric.WEIGHT && !unit.equals("kg")) {
            throw new IllegalArgumentException("Weight must be in kg.");
        }
        if (metric != BodyMeasurementMetric.WEIGHT && !unit.equals("cm")
                && !unit.equals("inch")) {
            throw new IllegalArgumentException(
                    "Only cm or inch are allowed for this metric.");
        }

        LocalDate date = Parser.parseDate(
                PromptUtils.promptLine(in, out, ">When (date):"));
        Map<String, Double> vals = readValues(metric);
        tracker.addItem(new BodyMeasurement(date, metric, unit, vals));
        out.writeln("Done!");
    }

    private BodyMeasurementMetric promptMetric() {
        String s = PromptUtils.promptLine(in, out,
                ">Which body measurement (weight/hip/waist/thigh/biceps):");
        return BodyMeasurementMetric.fromString(s);
    }

    private Map<String, Double> readValues(
            BodyMeasurementMetric bodyMeasurementMetric) {
        Map<String, Double> map = new HashMap<>();
        if (bodyMeasurementMetric == BodyMeasurementMetric.BICEPS) {
            map.put("left", PromptUtils.promptDouble(in, out,
                    ">Input left bicep circumference:"));
            map.put("right", PromptUtils.promptDouble(in, out,
                    ">Input right bicep circumference:"));
        } else {
            map.put("value", PromptUtils.promptDouble(in, out,
                    ">Input value:"));
        }
        return map;
    }
}
