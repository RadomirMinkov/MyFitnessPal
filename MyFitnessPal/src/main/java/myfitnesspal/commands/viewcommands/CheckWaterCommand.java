package myfitnesspal.commands.viewcommands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.Command;
import myfitnesspal.items.WaterIntake;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.parser.Parser;
import myfitnesspal.utility.PromptUtils;

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
        LocalDate date = Parser.parseDate(
                PromptUtils.promptLine(input, output, ">When?"));

        List<WaterIntake> sameDate =
                tracker.getItems(WaterIntake.class).stream()
                .filter(w -> w.date().equals(date))
                .toList();

        if (sameDate.isEmpty()) {
            output.write(date + ": No water intake recorded.");
        } else {
            output.write(date + ":");
            for (WaterIntake wi : sameDate) {
                output.write("-> " + wi.amount() + " "
                        + wi.measurementType().label());
            }
        }
    }
}
