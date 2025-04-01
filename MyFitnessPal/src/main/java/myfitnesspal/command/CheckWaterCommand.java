package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.WaterIntake;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.utility.Parser;

import java.time.LocalDate;
import java.util.List;

public final class CheckWaterCommand implements Command {
    private final MyFitnessTracker tracker;
    private final InputProvider input;
    private final OutputWriter output;

    public CheckWaterCommand(MyFitnessTracker tracker,
                             InputProvider input,
                             OutputWriter output) {
        this.tracker = tracker;
        this.input = input;
        this.output = output;
    }

    @Override
    public void execute() {
        output.write(">When? -");
        String rawDate = input.readLine();

        LocalDate date = Parser.parseDate(rawDate);
        List<WaterIntake> allWater = tracker.getWaterIntakes();
        List<WaterIntake> sameDate = allWater.stream()
                .filter(w -> w.date().equals(date))
                .toList();

        if (sameDate.isEmpty()) {
            output.write(rawDate + ": No water intake recorded.");
        } else {
            output.write(rawDate + ":");
            for (WaterIntake wi : sameDate) {
                output.write("-> " + wi.amount() + " ml");
            }
        }
    }
}
