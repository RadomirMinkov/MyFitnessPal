package myfitnesspal.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class FileManager {
    private FileManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static List<String> loadRawLines(String fileName) {
        List<String> lines = new ArrayList<>();
        File file = new File(fileName);

        if (!file.exists()) {
            return lines;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println(
                    "Error reading from file: "
                            + fileName + " - " + e.getMessage());
        }

        return lines;
    }

    public static void saveRawLines(String fileName, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println(
                    "Error writing to file: "
                            + fileName + " - "
                            + e.getMessage());
        }
    }
}
