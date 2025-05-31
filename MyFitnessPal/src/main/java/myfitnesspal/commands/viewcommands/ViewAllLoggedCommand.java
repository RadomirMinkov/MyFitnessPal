package myfitnesspal.commands.viewcommands;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.commands.Command;
import myfitnesspal.items.FoodLog;
import myfitnesspal.items.WaterIntake;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.parser.Parser;
import myfitnesspal.utility.PromptUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class ViewAllLoggedCommand implements Command {
    private final MyFitnessTracker tracker;
    private final InputProvider inputProvider;
    private final OutputWriter outputWriter;

    public ViewAllLoggedCommand(MyFitnessTracker tracker,
                                InputProvider inputProvider,
                                OutputWriter outputWriter) {
        this.tracker = tracker;
        this.inputProvider = inputProvider;
        this.outputWriter = outputWriter;
    }

    @Override
    public void execute() {
        outputWriter.write(">6. View All Logged\n");
        LocalDate date = Parser.parseDate(
                PromptUtils.promptLine(inputProvider, outputWriter,
                        ">When (date):"));

        List<FoodLog> logs = tracker.getFoodLogsForDate(date);

        Map<String, List<FoodLog>> groupedByMeal = logs.stream()
                .collect(Collectors.groupingBy(FoodLog::meal));

        if (logs.isEmpty()) {
            outputWriter.write("No foods logged for " + date);
        } else {
            String[] mealOrder = {"Breakfast", "Lunch", "Snacks", "Dinner"};

            for (String meal : mealOrder) {
                List<FoodLog> mealLogs = groupedByMeal.get(meal);
                if (mealLogs != null && !mealLogs.isEmpty()) {
                    outputWriter.write("\n" + meal + ":");
                    for (FoodLog fl : mealLogs) {
                        outputWriter.write(">"
                                + String.format(
                                        "%.0f units x %s (Total:"
                                                + " %.0f units; %.0f kcal; "
                                                + "%.2fg, %.2fg, %.2fg)",
                                fl.totalGrams(),
                                fl.foodName(),
                                fl.totalGrams(),
                                fl.totalCalories(),
                                fl.totalCarbs(),
                                fl.totalFat(),
                                fl.totalProtein()));
                    }
                } else {
                    outputWriter.write("\n" + meal + ": —");
                }
            }
        }

        List<WaterIntake> waterIntakes = tracker
                .getItems(WaterIntake.class).stream()
                .filter(w -> w.date().equals(date))
                .toList();

        if (waterIntakes.isEmpty()) {
            outputWriter.write("\nWater: No water logged");
        } else {
            outputWriter.write("\nWater:");
            for (WaterIntake wi : waterIntakes) {
                outputWriter.write("-> " + wi.amount()
                        + " " + wi.measurementType().label());
            }
        }
    }
}
