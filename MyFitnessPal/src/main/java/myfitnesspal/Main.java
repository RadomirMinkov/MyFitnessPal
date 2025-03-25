package myfitnesspal;

public final class Main {
    private Main() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }
}
