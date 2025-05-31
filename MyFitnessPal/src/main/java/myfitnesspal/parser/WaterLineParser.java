package myfitnesspal.parser;

import myfitnesspal.items.MeasurementType;
import myfitnesspal.items.Trackable;
import myfitnesspal.items.WaterIntake;

import java.time.LocalDate;

final class WaterLineParser {

    Trackable parse(String data) {
        String[] p = data.split(";");
        if (p.length != 3) {
            throw new IllegalArgumentException("Invalid WATER format");
        }

        LocalDate date = Parser.parseDate(p[0]);
        MeasurementType measurementType = MeasurementType.valueOf(p[1]);
        double amount = Double.parseDouble(p[2]);

        return new WaterIntake(date, measurementType, amount);
    }
}
