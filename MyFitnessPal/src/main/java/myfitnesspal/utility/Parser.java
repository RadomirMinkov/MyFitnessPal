package myfitnesspal.utility;

import myfitnesspal.Food;
import myfitnesspal.WaterIntake;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {

    private static final int FOOD_PARAMETERS = 8;
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
            default      -> null;
        };
    }

    private static WaterIntake parseWater(String data) {

        String[] parts = data.split(";");
        if (parts.length != 2) {
            return null;
        }

        LocalDate date = parseDate(parts[0]);
        if (date == null) {
            return null;
        }

        int amount;
        try {
            amount = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return null;
        }

        return new WaterIntake(date, amount);
    }

    public static LocalDate parseDate(String dateStr) {
        DateTimeFormatter[] formats = {
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("dd.MM.yyyy"),
                DateTimeFormatter.ISO_LOCAL_DATE
        };
        LocalDate date = null;
        for (DateTimeFormatter fmt : formats) {
            try {
                 date = LocalDate.parse(dateStr, fmt);
            } catch (DateTimeParseException exception) {
                
            }
        }
        return date;
    }

    private static Food parseFood(String data) {

        String[] parts = data.split(";");

        if (parts.length != FOOD_PARAMETERS ) {
            throw new IllegalArgumentException("Too few arguments");
        }

        String name = parts[0];
        String description = parts[1];
        double servingSize;
        int servingsPerContainer;
        double calories, carbs, fat, protein;

        try {
            servingSize = Double.parseDouble(parts[2]);
            servingsPerContainer = Integer.parseInt(parts[3]);
            calories = Double.parseDouble(parts[4]);
            carbs    = Double.parseDouble(parts[5]);
            fat      = Double.parseDouble(parts[6]);
            protein  = Double.parseDouble(parts[7]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The given arguments aren't in the right format");
        }

        return new Food(name, description, servingSize, servingsPerContainer,
                calories, carbs, fat, protein);
    }
}
