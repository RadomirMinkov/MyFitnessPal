package myfitnesspal.command;

import myfitnesspal.MyFitnessTracker;
import myfitnesspal.WaterIntake;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Scanner;

class CheckWaterCommandTest {

    @Test
    void testExecuteFoundWater() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        tracker.addItem(new WaterIntake(LocalDate.of(2025,
                3, 19), 500));

        String data = "2025-03-19\n";
        Scanner scanner = new Scanner(
                new ByteArrayInputStream(data.getBytes()));
        CheckWaterCommand cmd =
                new CheckWaterCommand(tracker, scanner);

        cmd.execute();
        Assertions.assertEquals(1, tracker.getWaterIntakes().size());
    }

    @Test
    void testExecuteNoWater() {
        MyFitnessTracker tracker = new MyFitnessTracker();

        String data = "2025-03-19\n";
        Scanner scanner = new Scanner(
                new ByteArrayInputStream(data.getBytes()));
        CheckWaterCommand cmd = new CheckWaterCommand(
                tracker, scanner);

        cmd.execute();
        Assertions.assertTrue(tracker.getWaterIntakes().isEmpty());
    }
}
