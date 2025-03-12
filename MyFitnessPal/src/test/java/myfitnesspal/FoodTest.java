package myfitnesspal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FoodTest {

    @Test
    @DisplayName("Test Food creation and toFileString")
    void testFoodCreation() {
        Food food = new Food("Apple",
                "Red Apple",
                100.0, 1,
                52.0, 14.0, 0.2, 0.3);

        assertEquals("Apple", food.name());
        assertEquals("Red Apple", food.description());
        assertEquals(100.0, food.servingSize());
        assertEquals(1, food.servingsPerContainer());
        assertEquals(52.0, food.calories());
        assertEquals(14.0, food.carbs());
        assertEquals(0.2, food.fat());
        assertEquals(0.3, food.protein());

        String fileStr = food.toFileString();
        assertTrue(fileStr.startsWith("FOOD;"), "File string must start with FOOD;");
        String[] parts = fileStr.split(";");
        assertEquals(9, parts.length);
    }
}
