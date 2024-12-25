package org.christolio;

import org.christolio.Arithmetic.Image.ArithmeticImageDecoder;
import org.christolio.Arithmetic.Image.ArithmeticImageEncodedData;
import org.christolio.Arithmetic.Image.ArithmeticImageEncoder;
import org.christolio.Optimization.SICOptimizer;
import org.christolio.PQ.PredictiveQuantizer;
import org.christolio.SIC.IO.SICReader;
import org.christolio.SIC.IO.SICWriter;
import org.christolio.SIC.SIC;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        testArithmetic();
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

    public static void testSIC() throws IOException, ExecutionException, InterruptedException {
        SIC sic = new SIC(16, 500);
        File imageFile = new File("C:\\Users\\Christolio\\Desktop\\testImage.png");
        sic.encode(imageFile);
        File file = new File("C:\\Users\\Christolio\\Desktop\\encoded_testImage.sic");
        sic.decode(file);
    }

    public static void testSICOptimizer() throws IOException, ExecutionException, InterruptedException {
        File imageFile = new File("C:\\Users\\Christolio\\Desktop\\testImage.png");
        File outputFile = new File("C:\\Users\\Christolio\\Desktop\\encoded_testImage.sic");
        File outputCSV = new File("C:\\Users\\Christolio\\Desktop\\optimizer_results.csv");
        SICOptimizer optimizer = new SICOptimizer(500, 2000, 500, 16, 64, 16, outputCSV);
        optimizer.optimize(imageFile, outputFile);
    }
}