package main.filehandling;

import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

public class FileHandler {

    public static void saveResult(String result) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("src/resources/result_output/results.txt", true))) {
            writer.write(result);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving results: " + e.getMessage());
        }
    }
}
