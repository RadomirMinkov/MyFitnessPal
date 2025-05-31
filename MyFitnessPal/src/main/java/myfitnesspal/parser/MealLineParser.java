package myfitnesspal.parser;

import myfitnesspal.items.Meal;
import myfitnesspal.items.MealItem;
import myfitnesspal.items.Trackable;

import java.util.ArrayList;
import java.util.List;

import static myfitnesspal.utility.Constants.MEAL_PARAMS;

final class MealLineParser {

    Trackable parse(String data) {
        String[] p = data.split(";");
        if (p.length < MEAL_PARAMS) {
            throw new IllegalArgumentException("Too few arguments for MEAL");
        }

        int count = Integer.parseInt(p[7]);
        if (p.length < MEAL_PARAMS + count * 2) {
            throw new IllegalArgumentException(
                    "Invalid MEAL item count/format");
        }

        List<MealItem> mealItemList = new ArrayList<>();
        int idx = MEAL_PARAMS;
        for (int i = 0; i < count; i++) {
            mealItemList.add(new MealItem(p[idx++],
                    Double.parseDouble(p[idx++])));
        }

        return new Meal(
                p[0], p[1],
                Double.parseDouble(p[2]),
                Double.parseDouble(p[3]),
                Double.parseDouble(p[4]),
                Double.parseDouble(p[5]),
                Double.parseDouble(p[6]),
                mealItemList);
    }
}
