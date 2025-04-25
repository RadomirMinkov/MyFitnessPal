package myfitnesspal.items;


import java.time.LocalDate;

public record FoodLog(LocalDate date,
                      String meal, String foodName,
                      double totalGrams, double totalCalories,
                      double totalCarbs, double totalFat,
                      double totalProtein) implements Trackable {

    @Override
    public String toFileString() {
        return "FOOD_LOG;"
                + date + ";"
                + meal + ";"
                + foodName + ";"
                + totalGrams + ";"
                + totalCalories + ";"
                + totalCarbs + ";"
                + totalFat + ";"
                + totalProtein;
    }

    @Override
    public String toString() {
        return String.format(
                "%sg %s (%.0f kcal; %.2fg, %.2fg, %.2fg) [Meal: %s, Date: %s]",
                totalGrams, foodName, totalCalories, totalCarbs, totalFat,
                totalProtein, meal, date);
    }

    @Override
    public Trackable getTrackableType() {
        return this;
    }
}
