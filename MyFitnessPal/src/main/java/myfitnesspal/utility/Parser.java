package myfitnesspal.utility;

import myfitnesspal.Food;
import myfitnesspal.FoodLog;
import myfitnesspal.Meal;
import myfitnesspal.MealItem;
import myfitnesspal.WaterIntake;
import myfitnesspal.Recipe;
import myfitnesspal.RecipeItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public final class Parser {

    private Parser() {
        throw new UnsupportedOperationException("Utility class");
    }
    private static final int FOOD_PARAMETERS = 8;

    private static final int FOOD_LOG_PARAMETERS = 8;
    public static Trackable parseLine(String line) {
        String[] parts = line.split(";", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        String prefix = parts[0];
        String data = parts[1];

        return switch (prefix) {
            case "WATER" -> parseWater(data);
            case "FOOD"  -> parseFood(data);
            case "FOOD_LOG" -> parseFoodLog(data);
            case "MEAL" -> parseMeal(data);
            case "RECIPE" -> parseRecipe(data);
            default      -> null;
        };
    }
    private static Recipe parseRecipe(String data) {
        String[] parts = data.split(";");
        if (parts.length < 10) {
            throw new IllegalArgumentException("Too few arguments for RECIPE");
        }
        String name = parts[0];
        String description = parts[1];
        int servings = Integer.parseInt(parts[2]);
        double totalGrams = Double.parseDouble(parts[3]);
        double totalCals = Double.parseDouble(parts[4]);
        double totalCarbs = Double.parseDouble(parts[5]);
        double totalFat = Double.parseDouble(parts[6]);
        double totalProtein = Double.parseDouble(parts[7]);
        int itemCount = Integer.parseInt(parts[8]);

        if (parts.length < 9 + itemCount * 2) {
            throw new IllegalArgumentException(
                    "Invalid RECIPE item count/format");
        }
        List<RecipeItem> ingredients = new ArrayList<>();
        int idx = 9;
        for (int i = 0; i < itemCount; i++) {
            String ingredientName = parts[idx++];
            double ingredientsServings =
                    Double.parseDouble(parts[idx++]);
            ingredients.add(new RecipeItem(ingredientName,
                    ingredientsServings));
        }
        return new Recipe(
                name, description, servings,
                totalGrams, totalCals, totalCarbs,
                totalFat, totalProtein, ingredients
        );
    }


    private static Meal parseMeal(String data) {
        String[] parts = data.split(";");
        if (parts.length < 8) {
            throw new IllegalArgumentException(
                    "Too few arguments for MEAL");
        }

        String name = parts[0];
        String description = parts[1];

        double totalGrams = Double.parseDouble(parts[2]);
        double totalCalories = Double.parseDouble(parts[3]);
        double totalCarbs = Double.parseDouble(parts[4]);
        double totalFat = Double.parseDouble(parts[5]);
        double totalProtein = Double.parseDouble(parts[6]);

        int itemCount = Integer.parseInt(parts[7]);

        if (parts.length < 8 + itemCount * 2) {
            throw new IllegalArgumentException(
                    "Invalid MEAL item count/format");
        }

        List<MealItem> mealItems = new ArrayList<>();
        int idx = 8;
        for (int i = 0; i < itemCount; i++) {
            String ingredientName = parts[idx++];
            double ingredientsServings = Double.parseDouble(parts[idx++]);
            mealItems.add(new MealItem(ingredientName, ingredientsServings));
        }

        return new Meal(name, description, totalGrams,
                totalCalories, totalCarbs,
                totalFat, totalProtein, mealItems);
    }
    private static WaterIntake parseWater(String data) {

        String[] parts = data.split(";");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        LocalDate date = parseDate(parts[0]);

        int amount;
        try {
            amount = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        return new WaterIntake(date, amount);
    }

    public static LocalDate parseDate(String dateStr) {
        DateTimeFormatter[] formats = {
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("dd.MM.yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-yy"),
                DateTimeFormatter.ISO_LOCAL_DATE
        };
        for (DateTimeFormatter fmt : formats) {
            try {
                return LocalDate.parse(dateStr, fmt);
            } catch (DateTimeParseException ignored) {
            }
        }

        throw new IllegalArgumentException("Invalid date: " + dateStr);
    }

    private static Food parseFood(String data) {

        String[] parts = data.split(";");

        if (parts.length != FOOD_PARAMETERS) {
            throw new IllegalArgumentException("Too few arguments");
        }

        String name = parts[0];
        String description = parts[1];
        double servingSize;
        int servingsPerContainer;
        double calories;
        double carbs;
        double fat;
        double protein;

        try {
            servingSize = Double.parseDouble(parts[2]);
            servingsPerContainer = Integer.parseInt(parts[3]);
            calories = Double.parseDouble(parts[4]);
            carbs    = Double.parseDouble(parts[5]);
            fat      = Double.parseDouble(parts[6]);
            protein  = Double.parseDouble(parts[7]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "The given arguments aren't in the right format");
        }

        return new Food(name, description,
                servingSize, servingsPerContainer,
                calories, carbs, fat, protein);
    }
    private static FoodLog parseFoodLog(String data) {
        String[] parts = data.split(";");
        if (parts.length != FOOD_LOG_PARAMETERS) {
            throw new IllegalArgumentException("Too few arguments");
        }

        try {
            return new FoodLog(parseDate(parts[0]),
                    parts[1], parts[2],
                    Double.parseDouble(parts[3]),
                    Double.parseDouble(parts[4]),
                    Double.parseDouble(parts[5]),
                    Double.parseDouble(parts[6]),
                    Double.parseDouble(parts[7]));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(e);
        }

    }
}
