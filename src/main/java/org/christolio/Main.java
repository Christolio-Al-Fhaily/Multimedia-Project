package org.christolio;

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
        // Get the image dimensions
        ArithmeticImageEncoder encoder = new ArithmeticImageEncoder(500);
        ArithmeticImageEncodedData encodedImage = encoder.encodeImage(image);
        ArithmeticImageDecoder decoder = new ArithmeticImageDecoder();
        BufferedImage decodedImage = decoder.decode(encodedImage);
        File outputImageFile = new File("reconstructed_image.png");
        ImageIO.write(decodedImage, "PNG", outputImageFile);
        System.out.println("Image successfully reconstructed and saved.");
    }
}