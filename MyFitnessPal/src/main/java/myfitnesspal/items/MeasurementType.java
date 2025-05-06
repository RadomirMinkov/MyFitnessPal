package myfitnesspal.items;

public enum MeasurementType {
    GRAM("g"),
    MILLILITER("ml"),
    PIECE("pcs");

    private final String label;

    MeasurementType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }

    public static MeasurementType fromChoice(int choice) {
        return switch (choice) {
            case 1 -> GRAM;
            case 2 -> MILLILITER;
            case 3 -> PIECE;
            default -> throw new
                    IllegalArgumentException("Invalid measurement type choice");
        };
    }
}
