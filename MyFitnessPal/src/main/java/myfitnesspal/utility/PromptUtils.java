package myfitnesspal.utility;

import myfitnesspal.items.MeasurementType;

public final class PromptUtils {

    private PromptUtils() { }

    public static MeasurementType promptMeasurementType(
            InputProvider in,
            OutputWriter out) {
        out.write(">Choose a measurement type?\n"
                + ">1. Grams + servings\n"
                + ">2. Milliliters + servings\n"
                + ">3. Pieces + servings\n-");
        int choice = Integer.parseInt(in.readLine().trim());
        return MeasurementType.fromChoice(choice);
    }

    public static double promptDouble(InputProvider in,
                                      OutputWriter out,
                                      String prompt) {
        out.write(prompt + "\n-");
        return Double.parseDouble(in.readLine().trim());
    }

    public static int promptInt(InputProvider in,
                                OutputWriter out,
                                String prompt) {
        out.write(prompt + "\n-");
        return Integer.parseInt(in.readLine().trim());
    }

    public static String promptLine(InputProvider in,
                                    OutputWriter out,
                                    String prompt) {
        out.write(prompt + "\n-");
        return in.readLine().trim();
    }

    public static int promptNutritionBasis(InputProvider in,
                                           OutputWriter out,
                                           MeasurementType type,
                                           double unitsPerServing) {
        out.write(">Choose nutrient info:\n"
                + ">1. Per " + type.label() + "\n"
                + ">2. Per serving (" + unitsPerServing
                + " " + type.label() + ")\n-");
        return Integer.parseInt(in.readLine().trim());
    }

    public static int promptFoodLogMode(InputProvider in,
                                        OutputWriter out,
                                        MeasurementType type,
                                        double unitsPerServing) {
        out.write(">Choose logging measurement:\n"
                + ">1. " + type.label() + "\n"
                + ">2. Servings (" + unitsPerServing
                + " " + type.label() + ")\n-");
        return Integer.parseInt(in.readLine().trim());
    }
}
