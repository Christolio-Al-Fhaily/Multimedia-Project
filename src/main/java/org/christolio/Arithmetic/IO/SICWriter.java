package org.christolio.Arithmetic.IO;

import org.christolio.Arithmetic.Image.ArithmeticImageEncodedData;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Super Inefficient Coded (SIC) writer class
 * to write the encoded image to a file
 */
public class SICWriter {

    public static void write(ArithmeticImageEncodedData encodedImageData, String outputPath, String fileName){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputPath + "/" + fileName + ".sic"))) {
            oos.writeObject(encodedImageData);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
