package myfitnesspal.utility;

import java.util.Scanner;

public final class ScannerInputProvider implements InputProvider {
    private final Scanner scanner;

    public ScannerInputProvider(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public String readLine() {
        return scanner.nextLine();
    }
}
