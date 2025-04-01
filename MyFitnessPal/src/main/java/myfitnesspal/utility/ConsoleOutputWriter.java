package myfitnesspal.utility;

public final class ConsoleOutputWriter implements OutputWriter {
    @Override
    public void writeln(String message) {
        System.out.println(message);
    }
    @Override
    public void write(String message) {
        System.out.print(message);
    }
}
