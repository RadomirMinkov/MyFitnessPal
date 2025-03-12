package myfitnesspal.utility;

import myfitnesspal.Food;
import myfitnesspal.WaterIntake;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    @DisplayName("Parse valid WATER line")
    void testParseWater() {
        String line = "WATER;2025-03-12;500";
        Trackable result = Parser.parseLine(line);
        assertTrue(result instanceof WaterIntake, "Parsed object should be a WaterIntake");
        WaterIntake wi = (WaterIntake) result;

        assertEquals(LocalDate.of(2025, 3, 12), wi.date(), "Date must match the parsed date");
        assertEquals(500, wi.amount(), "Amount must match the parsed amount");
    }

    @Test
    @DisplayName("Parse invalid WATER line")
    void testParseInvalidWater() {
        String line = "WATER;2025/03;abc";
        Trackable result = Parser.parseLine(line);
        assertNull(result, "Parser should return null for invalid lines");
    }

    @Test
    @DisplayName("Parse valid FOOD line")
    void testParseFood() {
        String line = "FOOD;Banana;Fresh Banana;118;1;105;27;0.3;1.3";
        Trackable result = Parser.parseLine(line);
        assertTrue(result instanceof Food, "Parsed object should be a Food");
        Food food = (Food) result;

        assertEquals("Banana", food.name(), "Food name must match parsed name");
        assertEquals("Fresh Banana", food.description(), "Food description must match parsed description");
        assertEquals(118, food.servingSize(), 1e-9, "Serving size must match");
        assertEquals(1, food.servingsPerContainer(), "Servings per container must match");
        assertEquals(105, food.calories(), 1e-9, "Calories must match");
        assertEquals(27, food.carbs(), 1e-9, "Carbs must match");
        assertEquals(0.3, food.fat(), 1e-9, "Fat must match");
        assertEquals(1.3, food.protein(), 1e-9, "Protein must match");
    }

    @Test
    @DisplayName("Parse date with multiple formats")
    void testParseDate() {
        LocalDate date1 = Parser.parseDate("2025-03-12");
        assertNotNull(date1, "Should parse ISO_LOCAL_DATE");
        assertEquals(LocalDate.of(2025, 3, 12), date1);

        LocalDate date2 = Parser.parseDate("2025/03/12");
        assertNotNull(date2, "Should parse yyyy/MM/dd");
        assertEquals(LocalDate.of(2025, 3, 12), date2);

        LocalDate date3 = Parser.parseDate("12.03.2025");
        assertNotNull(date3, "Should parse dd.MM.yyyy");
        assertEquals(LocalDate.of(2025, 3, 12), date3);

        LocalDate invalidDate = Parser.parseDate("03-12-2025");
        assertNull(invalidDate, "Should return null for unrecognized date format");
    }
}
