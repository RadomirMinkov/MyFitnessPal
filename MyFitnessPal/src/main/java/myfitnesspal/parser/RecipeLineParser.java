package myfitnesspal.parser;

import myfitnesspal.items.Recipe;
import myfitnesspal.items.RecipeItem;
import myfitnesspal.items.Trackable;

import java.util.ArrayList;
import java.util.List;

import static myfitnesspal.utility.Constants.RECIPE_PARAMS;

final class RecipeLineParser {
    Trackable parse(String data) {
        String[] p = data.split(";");
        if (p.length <= RECIPE_PARAMS) {
            throw new IllegalArgumentException("Too few arguments for RECIPE");
        }

        int count = Integer.parseInt(p[8]);
        if (p.length < RECIPE_PARAMS + count * 2) {
            throw new IllegalArgumentException(
                    "Invalid RECIPE item count/format");
        }

        List<RecipeItem> recipeItemList = new ArrayList<>();
        int idx = RECIPE_PARAMS;
        for (int i = 0; i < count; i++) {
            recipeItemList.add(new RecipeItem(p[idx++],
                    Double.parseDouble(p[idx++])));
        }

        return new Recipe(
                p[0],
                p[1],
                Integer.parseInt(p[2]),
                Double.parseDouble(p[3]),
                Double.parseDouble(p[4]),
                Double.parseDouble(p[5]),
                Double.parseDouble(p[6]),
                Double.parseDouble(p[7]),
                recipeItemList);
    }
}
