package org.christolio;

import org.christolio.Arithmetic.ArithmeticDecoder;
import org.christolio.Arithmetic.ArithmeticEncodedData;
import org.christolio.Arithmetic.ArithmeticEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedImage baseImage = ImageIO.read(Objects.requireNonNull(Main.class.getResource("/testImage.png")));
        BufferedImage image = baseImage.getSubimage(0, 0, baseImage.getWidth() / 8, baseImage.getHeight() / 8);
        File outputSubImageFile = new File("sub_image.png");
        ImageIO.write(image, "PNG", outputSubImageFile);
        // Get the image dimensions
        int width = image.getWidth();
        int height = image.getHeight();

        // Create a one-dimensional array to store the channel values
        int[] alphaArray = new int[width * height];
        int[] redArray = new int[width * height];
        int[] greenArray = new int[width * height];
        int[] blueArray = new int[width * height];

        // Flatten the image by reading pixel values row by row
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the red value of the pixel
                int pixel = image.getRGB(x, y);

                int alpha = (pixel >> 24) & 0xFF;
                int red = pixel >> 16 & 0xFF;
                int green = pixel >> 8 & 0xFF;
                int blue = pixel & 0xFF;

                // Store the red in the array
                alphaArray[y * width + x] = alpha;
                redArray[y * width + x] = red;
                greenArray[y * width + x] = green;
                blueArray[y * width + x] = blue;
            }
        }


        ArithmeticEncoder encoder = new ArithmeticEncoder();
        ArithmeticEncodedData encodedData = encoder.encode(redArray);
        System.out.println(encodedData.getEncodedValue().scale());
        System.out.println(encodedData.getEncodedValue().toString());
        ArithmeticDecoder decoder = new ArithmeticDecoder();
        int[] message = decoder.decode(encodedData);
        // Reconstruct the image from the flattened array
        for (int i = 0; i < message.length; i++){
            message[i] = (message[i] << 16) | (greenArray[i] << 8) | blueArray[i];
        }
        BufferedImage reconstructedImage = reconstructImage(message, width, height);

        // Save the reconstructed image to a file
        File outputImageFile = new File("reconstructed_image.png");
        ImageIO.write(reconstructedImage, "PNG", outputImageFile);

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