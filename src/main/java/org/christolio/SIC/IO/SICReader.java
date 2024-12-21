package org.christolio.SIC.IO;

import org.christolio.Arithmetic.Image.ArithmeticImageEncodedData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

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
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (ArithmeticImageEncodedData) ois.readObject();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
