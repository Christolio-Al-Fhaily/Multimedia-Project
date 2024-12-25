package org.christolio.Optimization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVWriter {
    private File csvFile;

    public CSVWriter(File csvFile, String[] headers) throws IOException {
        this.csvFile = csvFile;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(csvFile.getPath())))) {
            writer.write(String.join(",", headers));
            // Write headers
            writer.newLine();
            System.out.println("CSV file created and data inserted successfully.");
        }
    }

    public void insertRow(String[] data) {
        // Create the CSV file and write data
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile.getPath(),true))) {
            // Write data rows
            writer.write(String.join(",", data));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


