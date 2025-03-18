package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.WaterIntake;
import myfitnesspal.utility.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Scanner;

class DrinkWaterCommandTest {

    @Test
    void testExecute_ValidInput() {
        String data = "2025-03-19\n500\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(data.getBytes()));
        MyFitnessTracker tracker = new MyFitnessTracker();

        DrinkWaterCommand cmd = new DrinkWaterCommand(tracker, scanner, "testfile.txt");
        cmd.execute();

        Assertions.assertEquals(1, tracker.getWaterIntakes().size());
        WaterIntake wi = tracker.getWaterIntakes().get(0);
        Assertions.assertEquals(LocalDate.of(2025, 3, 19), wi.date());
        Assertions.assertEquals(500, wi.amount());
    }

    @Test
    void testExecute_InvalidAmount() {
        String data = "2025-03-19\nnotAnInt\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(data.getBytes()));
        MyFitnessTracker tracker = new MyFitnessTracker();

        DrinkWaterCommand cmd = new DrinkWaterCommand(tracker, scanner, "testfile.txt");
        Assertions.assertThrows(IllegalArgumentException.class, cmd::execute);
    }
}
