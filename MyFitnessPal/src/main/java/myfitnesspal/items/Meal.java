package myfitnesspal.items;

import java.util.List;

public record Meal(
        String name,
        String description,
        double totalGrams,
        double totalCalories,
        double totalCarbs,
        double totalFat,
        double totalProtein,
        List<MealItem> items) implements Trackable {

    @Override
    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MEAL;")
                .append(name == null ? "" : name).append(";")
                .append(description == null ? ""
                        : description).append(";")
                .append(totalGrams).append(";")
                .append(totalCalories).append(";")
                .append(totalCarbs).append(";")
                .append(totalFat).append(";")
                .append(totalProtein).append(";")
                .append(items.size()).append(";");

        for (MealItem mi : items) {
            sb.append(mi.foodName()).append(";");
            sb.append(mi.servings()).append(";");
        }

        return sb.toString();
    }

    @Override
    public Trackable getTrackableType() {
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s (%.0fg; %.0f kcal;"
                        + " %.2fg, %.2fg, %.2fg)",
                name, totalGrams, totalCalories,
                totalCarbs, totalFat, totalProtein);
    }
}
