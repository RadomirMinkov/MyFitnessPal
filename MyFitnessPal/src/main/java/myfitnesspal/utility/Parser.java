package myfitnesspal.utility;

import myfitnesspal.Food;
import myfitnesspal.FoodLog;
import myfitnesspal.WaterIntake;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class Parser {

    private Parser() {
        throw new UnsupportedOperationException("Utility class");
    }
    private static final int FOOD_PARAMETERS = 8;

    private static final int FOOD_LOG_PARAMETERS = 8;
    public static Trackable parseLine(String line) {
        String[] parts = line.split(";", 2);
        if (parts.length < 2) {
            return null;
        }

        String prefix = parts[0];
        String data = parts[1];

        return switch (prefix) {
            case "WATER" -> parseWater(data);
            case "FOOD"  -> parseFood(data);
            case "FOOD_LOG" -> parseFoodLog(data);
            default      -> null;
        };
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

        LocalDate date = parseDate(parts[0]);

        String meal       = parts[1];
        String foodName   = parts[2];

        double grams;
        double cals;
        double carbs;
        double fat;
        double protein;

        try {
            grams   = Double.parseDouble(parts[3]);
            cals    = Double.parseDouble(parts[4]);
            carbs   = Double.parseDouble(parts[5]);
            fat     = Double.parseDouble(parts[6]);
            protein = Double
                    .parseDouble(parts[7]);
        } catch (NumberFormatException e) {
            return null;
        }

        return new FoodLog(date, meal,
                foodName, grams, cals, carbs, fat, protein);
    }
}
