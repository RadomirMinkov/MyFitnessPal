package myfitnesspal;

import myfitnesspal.utility.FileManager;
import myfitnesspal.utility.Parser;
import myfitnesspal.utility.Trackable;

import java.util.ArrayList;
import java.util.List;

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
        List<WaterIntake> waterList = new ArrayList<>();
        for (Trackable t : items) {
            if (t instanceof WaterIntake wi) {
                waterList.add(wi);
            }
        }
        return waterList;
    }

    public List<Food> getFoods() {
        List<Food> foodList = new ArrayList<>();
        for (Trackable t : items) {
            if (t instanceof Food f) {
                foodList.add(f);
            }
        }
        return foodList;
    }
    public List<FoodLog> getFoodLogs() {
        List<FoodLog> logs = new ArrayList<>();
        for (Trackable t : items) {
            if (t instanceof FoodLog fl) {
                logs.add(fl);
            }
        }
        return logs;
    }
    public List<FoodLog> getFoodLogsForDate(java.time.LocalDate date) {
        return getFoodLogs()
                .stream()
                .filter(log -> log.date().equals(date))
                .toList();
    }
    public List<Meal> getMeals() {
        List<Meal> mealList = new ArrayList<>();
        for (Trackable t : items) {
            if (t instanceof Meal m) {
                mealList.add(m);
            }
        }
        return mealList;
    }
    public List<Recipe> getRecipes() {
        List<Recipe> list = new ArrayList<>();
        for (Trackable t : items) {
            if (t instanceof Recipe r) {
                list.add(r);
            }
        }
        return list;
    }
}
