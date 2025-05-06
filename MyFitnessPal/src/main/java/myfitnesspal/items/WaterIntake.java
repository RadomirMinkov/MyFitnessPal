package myfitnesspal.items;

import java.time.LocalDate;

public record WaterIntake(
        LocalDate date,
        MeasurementType measurementType,
        double amount
) implements Trackable {

    @Override
    public String toFileString() {
        return "WATER;" + date + ";" + measurementType.name() + ";" + amount;
    }

    @Override
    public String toString() {
        return "Water on " + date + ": "
                + amount + " " + measurementType.label();
    }

}
