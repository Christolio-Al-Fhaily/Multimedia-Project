package org.christolio;

import org.christolio.Arithmetic.Image.ArithmeticImageDecoder;
import org.christolio.Arithmetic.Image.ArithmeticImageEncodedData;
import org.christolio.Arithmetic.Image.ArithmeticImageEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedImage baseImage = ImageIO.read(Objects.requireNonNull(Main.class.getResource("/testImage.png")));
        BufferedImage image = baseImage.getSubimage(0, 0, baseImage.getWidth() / 4, baseImage.getHeight() / 4);
        File outputSubImageFile = new File("sub_image.png");
        ImageIO.write(image, "PNG", outputSubImageFile);
        // Get the image dimensions
        ArithmeticImageEncoder encoder = new ArithmeticImageEncoder(2000);
        ArithmeticImageEncodedData encodedImage = encoder.encodeImage(image);
        ArithmeticImageDecoder decoder = new ArithmeticImageDecoder();
        BufferedImage decodedImage = decoder.decode(encodedImage);
        File outputImageFile = new File("reconstructed_image.png");
        ImageIO.write(decodedImage, "PNG", outputImageFile);
        System.out.println("Image successfully reconstructed and saved.");
    }

    public static BufferedImage reconstructImage(int[] flattenedArray, int width, int height) {
        // Create a new BufferedImage with the given width and height
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Set the pixel data for the image from the flattened array
        image.setRGB(0, 0, width, height, flattenedArray, 0, width);

        return image;
    }
}