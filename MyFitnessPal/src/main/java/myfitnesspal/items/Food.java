package myfitnesspal.items;

public record Food(
        String name,
        String description,
        MeasurementType measurementType,
        double unitsPerServing,
        int servingsPerContainer,
        double calories,
        double carbs,
        double fat,
        double protein
) implements Trackable {

    @Override
    public String toFileString() {
        return "FOOD;" + name + ";" + description + ";"
                + measurementType.name() + ";"
                + unitsPerServing + ";"
                + servingsPerContainer + ";"
                + calories + ";" + carbs
                + ";" + fat + ";" + protein;
    }

    @Override
    public String toString() {
        return name + " (1 serving = " + unitsPerServing + " "
                + measurementType.label() + "; "
                + calories + " kcal; "
                + carbs + "g, "
                + fat + "g, " + protein + "g)";
    }

}
