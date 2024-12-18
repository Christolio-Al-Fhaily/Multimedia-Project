package org.christolio.Arithmetic.Image;

import me.tongfei.progressbar.ProgressBar;
import org.christolio.Arithmetic.ArithmeticEncodedData;
import org.christolio.Arithmetic.ArithmeticEncoder;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArithmeticImageEncoder {

    private int chunkSize;
    private ArithmeticEncoder encoder = new ArithmeticEncoder();
    private int numberOfChunksPerChannel;

    public ArithmeticImageEncoder(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public ArithmeticImageEncodedData encodeImage(BufferedImage imageToEncode) {
        int width = imageToEncode.getWidth();
        int height = imageToEncode.getHeight();
        if (chunkSize > width * height)
            chunkSize = width * height;
        numberOfChunksPerChannel = (int) Math.ceil((double) (width * height) / chunkSize);

        int[] alphaArray = new int[numberOfChunksPerChannel * chunkSize];
        int[] redArray = new int[numberOfChunksPerChannel * chunkSize];
        int[] greenArray = new int[numberOfChunksPerChannel * chunkSize];
        int[] blueArray = new int[numberOfChunksPerChannel * chunkSize];

        Arrays.fill(alphaArray, 0);
        Arrays.fill(redArray, 0);
        Arrays.fill(greenArray, 0);
        Arrays.fill(blueArray, 0);


        // Flatten the image by reading pixel values row by row
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get the red value of the pixel
                int pixel = imageToEncode.getRGB(x, y);

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

        ProgressBar progressBar = new ProgressBar("Encoding channels...", 4);
        List<ArithmeticEncodedData> encodedAlphaChannel = encodeChannel(alphaArray);
        progressBar.step();
        List<ArithmeticEncodedData> encodedRedChannel = encodeChannel(redArray);
        progressBar.step();
        List<ArithmeticEncodedData> encodedGreenChannel = encodeChannel(greenArray);
        progressBar.step();
        List<ArithmeticEncodedData> encodedBlueChannel = encodeChannel(blueArray);
        progressBar.step();
        progressBar.close();


        ArithmeticImageEncodedData encodedImage = new ArithmeticImageEncodedData(width, height, chunkSize);
        encodedImage.addChunks(encodedAlphaChannel);
        encodedImage.addChunks(encodedRedChannel);
        encodedImage.addChunks(encodedGreenChannel);
        encodedImage.addChunks(encodedBlueChannel);
        return encodedImage;
    }

    private List<ArithmeticEncodedData> encodeChannel(int[] channel) {
        if (channel.length == 0)
            return Collections.emptyList();
        List<ArithmeticEncodedData> encodedChannel = new ArrayList<>();
        for (int i = 0; i < numberOfChunksPerChannel; i++) {
            ArithmeticEncodedData encodedChunk = encoder.encode(Arrays.copyOfRange(channel, i * chunkSize, (i + 1) * chunkSize));
            encodedChannel.add(encodedChunk);
        }
        return encodedChannel;
    }
}
