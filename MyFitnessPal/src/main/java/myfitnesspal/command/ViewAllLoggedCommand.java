package myfitnesspal.command;

import myfitnesspal.items.FoodLog;
import myfitnesspal.MyFitnessTracker;
import myfitnesspal.items.WaterIntake;
import myfitnesspal.utility.InputProvider;
import myfitnesspal.utility.OutputWriter;
import myfitnesspal.utility.Parser;

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
        outputWriter.write(">When (date):\n-");
        String rawDate = inputProvider.readLine().trim();
        LocalDate date = Parser.parseDate(rawDate);

        List<FoodLog> logs = tracker.getFoodLogsForDate(date);

        Map<String, List<FoodLog>> groupedByMeal =
                logs.stream().collect(Collectors.
                        groupingBy(FoodLog::meal));

        if (logs.isEmpty()) {
            outputWriter.write("No foods logged for " + rawDate);
        } else {
            String[] mealOrder = {"Breakfast",
                    "Lunch", "Snacks", "Dinner"};

            for (String meal : mealOrder) {
                List<FoodLog> mealLogs = groupedByMeal.get(meal);
                if (mealLogs != null && !mealLogs.isEmpty()) {
                    outputWriter.write("\n" + meal + ":");
                    for (FoodLog fl : mealLogs) {
                        outputWriter.write(">"
                                + String.format(
                                        "%.0fg x %s (Total: %.0fg; %.0f"
                                                + " kcal; %.2fg, %.2fg, %.2fg)",
                                fl.totalGrams(), fl.foodName(),
                                fl.totalGrams(), fl.totalCalories(),
                                fl.totalCarbs(), fl.totalFat(),
                                fl.totalProtein()));
                    }
                } else {
                    outputWriter.write("\n" + meal + ": —");
                }
            }
        }

        List<WaterIntake> waterIntakes = tracker.
                getWaterIntakes().stream()
                .filter(w -> w.date().equals(date))
                .toList();

        int totalMl = waterIntakes.stream().
                mapToInt(WaterIntake::amount).sum();
        double totalLiters = totalMl / 1000.0;

        outputWriter.write("\nWater: "
                + (totalLiters > 0 ? totalLiters
                + "L" : "No water logged"));
    }
}
