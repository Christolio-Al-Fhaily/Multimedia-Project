package org.christolio;

import org.christolio.Arithmetic.IO.SICReader;
import org.christolio.Arithmetic.IO.SICWriter;
import org.christolio.Arithmetic.Image.ArithmeticImageDecoder;
import org.christolio.Arithmetic.Image.ArithmeticImageEncodedData;
import org.christolio.Arithmetic.Image.ArithmeticImageEncoder;
import org.christolio.PQ.PredictiveQuantizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        testQuantization();
    }

    public static void testArithmetic() throws IOException, ExecutionException, InterruptedException {
        BufferedImage image = ImageIO.read(Objects.requireNonNull(Main.class.getResource("/testImage.png")));
        ArithmeticImageEncoder encoder = new ArithmeticImageEncoder(500);
        ArithmeticImageEncodedData encodedImage = encoder.encodeImage(image);
        SICWriter.write(encodedImage, "D:\\University\\Year 5\\Sem 9\\Cours\\Multimedia\\Projet\\ImageCompression", "encodedImage");
        ArithmeticImageEncodedData savedEncodedImage = SICReader.read("D:\\University\\Year 5\\Sem 9\\Cours\\Multimedia\\Projet\\ImageCompression\\encodedImage.sic");
        ArithmeticImageDecoder decoder = new ArithmeticImageDecoder();
        BufferedImage decodedImage = decoder.decode(savedEncodedImage);
        File outputImageFile = new File("reconstructed_image.png");
        ImageIO.write(decodedImage, "PNG", outputImageFile);
        System.out.println("Image successfully reconstructed and saved.");
    }

    public static void testQuantization() throws IOException {
        BufferedImage image = ImageIO.read(Objects.requireNonNull(Main.class.getResource("/testImage.png")));
        PredictiveQuantizer quantizer = new PredictiveQuantizer(16);
        BufferedImage quantizedImage = quantizer.quantizeGreenChannel(image);
        File outputImageFile = new File("quantized_image.png");
        ImageIO.write(quantizedImage, "PNG", outputImageFile);
    }
}