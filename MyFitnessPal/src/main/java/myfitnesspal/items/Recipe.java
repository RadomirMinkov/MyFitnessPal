package myfitnesspal.items;

import java.util.List;

public record Recipe(
        String name,
        String description,
        int servings,
        double totalGrams,
        double totalCalories,
        double totalCarbs,
        double totalFat,
        double totalProtein,
        List<RecipeItem> items
) implements Trackable {

    @Override
    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RECIPE;")
                .append(name == null ? "" : name).append(";")
                .append(description == null ? "" : description).append(";")
                .append(servings).append(";")
                .append(totalGrams).append(";")
                .append(totalCalories).append(";")
                .append(totalCarbs).append(";")
                .append(totalFat).append(";")
                .append(totalProtein).append(";")
                .append(items.size()).append(";");

        for (RecipeItem ri : items) {
            sb.append(ri.foodName()).append(";")
                    .append(ri.servings()).append(";");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        double gramsPer = totalGrams / servings;
        double calsPer = totalCalories / servings;
        double carbsPer = totalCarbs / servings;
        double fatPer = totalFat / servings;
        double proteinPer = totalProtein / servings;

        return String.format(
                "%s (%.2fg; %.2f kcal; %.2fg, %.2fg, %.2fg)",
                name,
                gramsPer, calsPer, carbsPer, fatPer, proteinPer
        );
    }

}
