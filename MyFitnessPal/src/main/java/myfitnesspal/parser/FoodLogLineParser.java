package myfitnesspal.parser;

import myfitnesspal.items.FoodLog;
import myfitnesspal.items.Trackable;

final class FoodLogLineParser {

    private static final int PARAMS = 8;

    Trackable parse(String data) {
        String[] p = data.split(";");
        if (p.length != PARAMS) {
            throw new IllegalArgumentException("Too few arguments");
        }

        return new FoodLog(
                Parser.parseDate(p[0]),
                p[1],
                p[2],
                Double.parseDouble(p[3]),
                Double.parseDouble(p[4]),
                Double.parseDouble(p[5]),
                Double.parseDouble(p[6]),
                Double.parseDouble(p[7]));
    }
}
