package myfitnesspal.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

class InputReaderTest {

    @Test
    void testReadDoubleValid() {
        String data = "12.34\n";
        Scanner scanner = new Scanner(
                new ByteArrayInputStream(data.getBytes()));
        InputReader inputReader = new InputReader(scanner);

        double value = inputReader.readDouble();
        Assertions.assertEquals(12.34, value, 0.0001);
    }

    @Test
    void testReadDoubleInvalidThenValid() {
        String data = "abc\n3.14\n";
        Scanner scanner = new Scanner(
                new ByteArrayInputStream(data.getBytes()));
        InputReader inputReader = new InputReader(scanner);

        double value = inputReader.readDouble();
        Assertions.assertEquals(3.14, value, 0.0001);
    }

    @Test
    void testReadIntValid() {
        String data = "42\n";
        Scanner scanner = new Scanner(
                new ByteArrayInputStream(data.getBytes()));
        InputReader inputReader = new InputReader(scanner);

        int value = inputReader.readInt();
        Assertions.assertEquals(42, value);
    }

    @Test
    void testReadIntInvalidThenValid() {
        String data = "forty-two\n99\n";
        Scanner scanner = new Scanner(
                new ByteArrayInputStream(data.getBytes()));
        InputReader inputReader = new InputReader(scanner);

        int value = inputReader.readInt();
        Assertions.assertEquals(99, value);
    }
}
