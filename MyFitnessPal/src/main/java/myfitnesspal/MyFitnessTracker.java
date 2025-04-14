package myfitnesspal;

import myfitnesspal.utility.FileManager;
import myfitnesspal.utility.Parser;
import myfitnesspal.utility.Trackable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class MyFitnessTracker {
    private final List<Trackable> items = new ArrayList<>();

    public void load(String fileName) {
        List<String> lines = FileManager.loadRawLines(fileName);
        items.clear();

        for (String line : lines) {
            Trackable t = Parser.parseLine(line);
            if (t != null) {
                items.add(t);
            }
        }
    }

    public void save(String fileName) {
        List<String> lines = new ArrayList<>();
        for (Trackable item : items) {
            lines.add(item.toFileString());
        }
        FileManager.saveRawLines(fileName, lines);
    }

    public void addItem(Trackable item) {
        items.add(item);
    }

    public List<WaterIntake> getWaterIntakes() {
        return items.stream()
                .filter(WaterIntake.class::isInstance)
                .map(WaterIntake.class::cast)
                .collect(Collectors.toList());
    }

    public List<Food> getFoods() {
        return items.stream()
                .filter(Food.class::isInstance)
                .map(Food.class::cast)
                .collect(Collectors.toList());
    }
    public List<FoodLog> getFoodLogs() {
        return items.stream()
                .filter(FoodLog.class::isInstance)
                .map(FoodLog.class::cast)
                .collect(Collectors.toList());
    }
    public List<FoodLog> getFoodLogsForDate(java.time.LocalDate date) {
        return getFoodLogs().stream()
                .filter(log -> log.date().equals(date))
                .collect(Collectors.toList());
    }
    public List<Meal> getMeals() {
        return items.stream()
                .filter(Meal.class::isInstance)
                .map(Meal.class::cast)
                .collect(Collectors.toList());
    }
    public List<Recipe> getRecipes() {
        return items.stream()
                .filter(Recipe.class::isInstance)
                .map(Recipe.class::cast)
                .collect(Collectors.toList());
    }
}
