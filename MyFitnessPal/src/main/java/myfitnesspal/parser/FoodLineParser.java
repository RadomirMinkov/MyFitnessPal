package myfitnesspal.parser;

import myfitnesspal.items.Food;
import myfitnesspal.items.MeasurementType;
import myfitnesspal.items.Trackable;

final class FoodLineParser {

    Trackable parse(String data) {
        String[] p = data.split(";");
        if (p.length != 9) {
            throw new IllegalArgumentException("Invalid FOOD format");
        }

        return new Food(
                p[0],
                p[1],
                MeasurementType.valueOf(p[2]),
                Double.parseDouble(p[3]),
                Integer.parseInt(p[4]),
                Double.parseDouble(p[5]),
                Double.parseDouble(p[6]),
                Double.parseDouble(p[7]),
                Double.parseDouble(p[8]));
    }
}
