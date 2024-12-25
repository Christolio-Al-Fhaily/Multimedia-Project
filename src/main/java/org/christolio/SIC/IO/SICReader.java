package org.christolio.SIC.IO;

import org.christolio.Arithmetic.Image.ArithmeticImageEncodedData;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * Super Inefficient Coded (SIC) reader class
 * to read the encoded image from a file
 */
public class SICReader {

    public static ArithmeticImageEncodedData read(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        String extension = filePath.substring(lastDotIndex + 1);
        if (lastDotIndex == -1 || lastDotIndex == filePath.length() - 1 || !extension.equals("sic")) {
            throw new RuntimeException("File must be .sic");
        }
        try (FileInputStream fileIn = new FileInputStream(filePath);
             BufferedInputStream bufferedIn = new BufferedInputStream(fileIn);
             GZIPInputStream gzipIn = new GZIPInputStream(bufferedIn);
             ObjectInputStream objectIn = new ObjectInputStream(gzipIn)) {
            System.out.println("Reading...");
            return (ArithmeticImageEncodedData) objectIn.readObject();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
