package org.christolio.SIC.IO;

import org.christolio.Arithmetic.Image.ArithmeticImageEncodedData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;

/**
 * Super Inefficient Coded (SIC) writer class
 * to write the encoded image to a file
 */
public class SICWriter {

    /**
     * Writes the encoded data to a file
     *
     * @param encodedImageData The data to save
     * @param outputPath       The save location
     * @param fileName         The name of the file
     * @return The size of the file in bytes
     */
    public static long write(ArithmeticImageEncodedData encodedImageData, String outputPath, String fileName) {
        File outputFile = new File(outputPath, fileName + ".sic");

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile))) {
            // Write the object to the file
            oos.writeObject(encodedImageData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the file size
        return outputFile.length();
    }

}
