package org.christolio;

import org.christolio.Arithmetic.IO.SICReader;
import org.christolio.Arithmetic.IO.SICWriter;
import org.christolio.Arithmetic.Image.ArithmeticImageDecoder;
import org.christolio.Arithmetic.Image.ArithmeticImageEncodedData;
import org.christolio.Arithmetic.Image.ArithmeticImageEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        BufferedImage baseImage = ImageIO.read(Objects.requireNonNull(Main.class.getResource("/testImage.png")));
        BufferedImage image = baseImage.getSubimage(0, 0, baseImage.getWidth(), baseImage.getHeight());
        File outputSubImageFile = new File("sub_image.png");
        ImageIO.write(image, "PNG", outputSubImageFile);
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
}