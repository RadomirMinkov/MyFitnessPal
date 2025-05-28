package myfitnesspal.parser;

import myfitnesspal.items.Trackable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class Parser {
    private Parser() {
        throw new UnsupportedOperationException();
    }

    public static Trackable parseLine(String line) {
        String[] parts = line.split(";", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Wrong number of arguments");
        }

        String prefix = parts[0];
        String data   = parts[1];

        return switch (prefix) {
            case "WATER"     -> WaterLineParser.parse(data);
            case "FOOD"      -> FoodLineParser.parse(data);
            case "FOOD_LOG"  -> FoodLogLineParser.parse(data);
            case "MEAL"      -> MealLineParser.parse(data);
            case "RECIPE"    -> RecipeLineParser.parse(data);
            case "BM"        -> BodyMeasurementLineParser.parse(data);
            default          -> null;
        };
    }

    public static LocalDate parseDate(String dateStr) {
        DateTimeFormatter[] f = {
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("dd.MM.yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-yy"),
                DateTimeFormatter.ISO_LOCAL_DATE
        };
        for (DateTimeFormatter fmt : f) {
            try {
                return LocalDate.parse(dateStr, fmt);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new IllegalArgumentException("Invalid date: " + dateStr);
    }
}
