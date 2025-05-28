package myfitnesspal.parser;

import myfitnesspal.items.Recipe;
import myfitnesspal.items.RecipeItem;
import myfitnesspal.items.Trackable;

import java.util.ArrayList;
import java.util.List;

final class RecipeLineParser {
    private RecipeLineParser() { }

    static Trackable parse(String data) {
        String[] p = data.split(";");
        if (p.length < 10) {
            throw new IllegalArgumentException("Too few arguments for RECIPE");
        }

        int count = Integer.parseInt(p[8]);
        if (p.length < 9 + count * 2) {
            throw new IllegalArgumentException(
                    "Invalid RECIPE item count/format");
        }

        List<RecipeItem> recipeItemList = new ArrayList<>();
        int idx = 9;
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
