package org.christolio.SIC.IO;

import me.tongfei.progressbar.ProgressBar;
import org.christolio.Arithmetic.Image.ArithmeticImageEncodedData;

import java.io.*;
import java.util.zip.GZIPOutputStream;

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
        try (FileOutputStream fileOut = new FileOutputStream(outputFile);
             BufferedOutputStream bufferedOut = new BufferedOutputStream(fileOut);
             GZIPOutputStream gzipOut = new GZIPOutputStream(bufferedOut);
             ObjectOutputStream objectOut = new ObjectOutputStream(gzipOut)) {
            System.out.println("Saving...");
            // Write the object to the file
            objectOut.writeObject(encodedImageData);
            System.out.println("File saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the file size
        return outputFile.length();
    }

}
