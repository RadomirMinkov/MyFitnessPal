package myfitnesspal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record WaterIntake(LocalDate date, int amount) {

    @Override
    public String toString() {
        return date.toString() + ";" + amount;
    }

    public static WaterIntake fromString(String inputLine) {
        String[] parts = inputLine.split(";");
        if (parts.length != 2) {
            return null;
        }
        String rawDate = parts[0];
        int amount = Integer.parseInt(parts[1]);

        LocalDate parsedDate = parseDateWithMultipleFormats(rawDate);
        if (parsedDate == null) {
            return null;
        }

        return new WaterIntake(parsedDate, amount);
    }

    private static LocalDate parseDateWithMultipleFormats(String dateStr) {
        DateTimeFormatter[] possibleFormats = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("dd.MM.yyyy"),
                DateTimeFormatter.ISO_LOCAL_DATE
        };

        for (DateTimeFormatter formatter : possibleFormats) {
            try {
                return LocalDate.parse(dateStr, formatter);
            } catch (Exception e) {
            }
        }
        return null;
    }
}
