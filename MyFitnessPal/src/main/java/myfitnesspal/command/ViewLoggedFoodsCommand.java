package myfitnesspal.command;

import myfitnesspal.FoodLog;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.utility.Parser;

import java.time.LocalDate;
import java.util.List;

public final class ViewLoggedFoodsCommand implements Command {
    private final MyFitnessTracker tracker;
    private final InputProvider inputProvider;
    private final OutputWriter outputWriter;

    public ViewLoggedFoodsCommand(MyFitnessTracker tracker,
                                  InputProvider inputProvider,
                                  OutputWriter outputWriter) {
        this.tracker = tracker;
        this.inputProvider = inputProvider;
        this.outputWriter = outputWriter;
    }

    @Override
    public void execute() {
        outputWriter.write(">6. View Foods Logged");

        outputWriter.write(">When (date):\n-");
        String rawDate = inputProvider.readLine().trim();
        LocalDate date = Parser.parseDate(rawDate);

        List<FoodLog> logs = tracker.getFoodLogsForDate(date);
        if (logs.isEmpty()) {
            throw new IllegalArgumentException(
                    "No foods logged for " + rawDate);
        }

        outputWriter.write("Foods logged on " + rawDate + ":");
        for (FoodLog log : logs) {
            outputWriter.write("- " + log);
        }
    }
}
