package myfitnesspal;

import myfitnesspal.utility.Trackable;

public record Food(String name, String description,
                   double servingSize, int servingsPerContainer,
                   double calories, double carbs,
                   double fat, double protein) implements Trackable {

    @Override
    public String toFileString() {
        return "FOOD;" + (name == null ? "" : name) + ";"
                + (description == null ? "" : description) + ";"
                + servingSize + ";" + servingsPerContainer + ";"
                + calories + ";"  + carbs + ";"
                + fat + ";"  + protein;
    }

    @Override
    public String toString() {
        return name
                + " (" + servingSize + "g; "
                + calories + " kcal; "
                + carbs + "g, "
                + fat + "g, "
                + protein + "g)";
    }
}
