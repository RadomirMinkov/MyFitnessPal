package myfitnesspal.items;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FoodTest {

    @Test
    void testFoodRecord() {
        Food food = new Food("Pizza", "Cheesy slice",
                100, 2, 300, 30, 10, 15);

        Assertions.assertEquals("Pizza", food.name());
        Assertions.assertEquals("Cheesy slice", food.description());
        Assertions.assertEquals(100, food.servingSize());
        Assertions.assertEquals(2, food.servingsPerContainer());
        Assertions.assertEquals(300, food.calories());
        Assertions.assertEquals(30, food.carbs());
        Assertions.assertEquals(10, food.fat());
        Assertions.assertEquals(15, food.protein());
    }

    @Test
    void testToFileString() {
        Food food = new Food("Pizza", "Cheesy slice",
                100, 2, 300, 30, 10, 15);
        String fileString = food.toFileString();
        Assertions.assertTrue(
                fileString.startsWith(
                        "FOOD;Pizza;Cheesy slice;"
                                + "100.0;2;300.0;30.0;10.0;15.0"));
    }

    @Test
    void testToString() {
        Food food = new Food("Pizza", "Cheesy slice",
                100, 2, 300, 30, 10, 15);
        String s = food.toString();
        Assertions.assertTrue(s.contains("Pizza"));
        Assertions.assertTrue(s.contains("100.0g"));
        Assertions.assertTrue(s.contains("300.0 kcal"));
    }
}
