package myfitnesspal.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

class InputReaderTest {

    @Test
    void testReadDoubleValidInput() {
        String data = "3.14\n";
        Scanner scanner = new Scanner(
                new ByteArrayInputStream(data.getBytes()));
        InputReader inputReader = new InputReader(scanner);

        double value = inputReader.readDouble();
        Assertions.assertEquals(3.14, value, 0.0001);
    }
    @Test
    void testReadDoubleInvalidInputOnly() {
        String data = "abc\n";
        Scanner scanner = new Scanner(
                new ByteArrayInputStream(data.getBytes()));
        InputReader inputReader = new InputReader(scanner);

        Assertions.assertThrows(
                IllegalArgumentException.class, inputReader::readDouble);
    }

    @Test
    void testReadIntValidInput() {
        String data = "42\n";
        Scanner scanner = new Scanner(
                new ByteArrayInputStream(data.getBytes()));
        InputReader inputReader = new InputReader(scanner);

        int value = inputReader.readInt();
        Assertions.assertEquals(42, value);
    }

    @Test
    void testReadIntInvalidInputOnly() {
        String data = "forty-two\n";
        Scanner scanner = new Scanner(
                new ByteArrayInputStream(data.getBytes()));
        InputReader inputReader = new InputReader(scanner);

        Assertions.assertThrows(
                IllegalArgumentException.class, inputReader::readInt);
    }
}
