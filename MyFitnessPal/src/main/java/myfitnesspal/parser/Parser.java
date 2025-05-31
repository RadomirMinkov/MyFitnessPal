package myfitnesspal.parser;

import myfitnesspal.items.Trackable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class Parser {
    private BodyMeasurementLineParser bodyMeasurementLineParser;
    private FoodLineParser foodLineParser;
    private FoodLogLineParser foodLogLineParser;
    private RecipeLineParser recipeLineParser;
    private MealLineParser mealLineParser;
    private WaterLineParser waterLineParser;
    public Parser() {
        bodyMeasurementLineParser = new BodyMeasurementLineParser();
        foodLineParser = new FoodLineParser();
        foodLogLineParser = new FoodLogLineParser();
        mealLineParser = new MealLineParser();
        recipeLineParser = new RecipeLineParser();
        waterLineParser = new WaterLineParser();
    }

    public Trackable parseLine(String line) {
        String[] parts = line.split(";", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        String prefix = parts[0];
        String data   = parts[1];

        return switch (prefix) {
            case "WATER"     -> waterLineParser.parse(data);
            case "FOOD"      -> foodLineParser.parse(data);
            case "FOOD_LOG"  -> foodLogLineParser.parse(data);
            case "MEAL"      -> mealLineParser.parse(data);
            case "RECIPE"    -> recipeLineParser.parse(data);
            case "BM"        -> bodyMeasurementLineParser.parse(data);
            default          -> null;
        };
    }

    public static LocalDate parseDate(String dateStr) {
        DateTimeFormatter[] dateTimeFormatters = {
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("dd.MM.yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-yy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ISO_LOCAL_DATE
        };
        for (DateTimeFormatter fmt : dateTimeFormatters) {
            try {
                return LocalDate.parse(dateStr, fmt);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new IllegalArgumentException("Invalid date: " + dateStr);
    }
}
