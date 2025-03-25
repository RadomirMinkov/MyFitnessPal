package myfitnesspal.command;

import myfitnesspal.FoodLog;
import myfitnesspal.MyFitnessTracker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Scanner;

class ViewLoggedFoodsCommandTest {

    @Test
    void testExecuteFoundLogs() {
        MyFitnessTracker tracker = new MyFitnessTracker();
        tracker.addItem(new FoodLog(LocalDate.of(2025, 3,
                19), "Lunch", "Pizza", 200,
                600, 40, 20, 30));

        String data = "2025-03-19\n";
        Scanner scanner = new Scanner(
                new ByteArrayInputStream(data.getBytes()));
        ViewLoggedFoodsCommand cmd =
                new ViewLoggedFoodsCommand(tracker, scanner);

        cmd.execute();
    }

    @Test
    void testExecuteNoLogs() {
        MyFitnessTracker tracker = new MyFitnessTracker();

        String data = "2025-03-19\n";
        Scanner scanner = new Scanner(
                new ByteArrayInputStream(data.getBytes()));
        ViewLoggedFoodsCommand cmd =
                new ViewLoggedFoodsCommand(tracker, scanner);
        Assertions.assertThrows(IllegalArgumentException.class, cmd::execute);
    }
}
