package org.christolio.Arithmetic.Image;

import me.tongfei.progressbar.ProgressBar;
import org.christolio.Arithmetic.ArithmeticDecoder;
import org.christolio.Arithmetic.ArithmeticEncodedData;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class ArithmeticImageDecoder {

    private ArithmeticDecoder decoder = new ArithmeticDecoder();

    public BufferedImage decode(ArithmeticImageEncodedData encodedData) {
        int width = encodedData.getWidth();
        int height = encodedData.getHeight();
        int chunkSize = encodedData.getChunkSize();
        int numberOfChunksPerChannel = (int) Math.ceil((double) (width * height) / chunkSize);

        int[] alphaArray = new int[numberOfChunksPerChannel * chunkSize];
        int[] redArray = new int[numberOfChunksPerChannel * chunkSize];
        int[] greenArray = new int[numberOfChunksPerChannel * chunkSize];
        int[] blueArray = new int[numberOfChunksPerChannel * chunkSize];

        List<ArithmeticEncodedData> encodedChunks = encodedData.getEncodedChunks();
        ProgressBar progressBar = new ProgressBar("Decoding Chunks", encodedChunks.size());

        int count = 0;
        int channelCount = 0;
        for (ArithmeticEncodedData encodedChunk : encodedChunks) {
            int[] chunk = decoder.decode(encodedChunk);
            int startIndex = count * chunkSize;

            switch (channelCount) {
                case 0:
                    alphaArray = insertSubarray(alphaArray, chunk, startIndex);
                    break;
                case 1:
                    redArray = insertSubarray(redArray, chunk, startIndex);
                    break;
                case 2:
                    greenArray = insertSubarray(greenArray, chunk, startIndex);
                    break;
                case 3:
                    blueArray = insertSubarray(blueArray, chunk, startIndex);
                    break;
            }

            count++;
            if (count == numberOfChunksPerChannel) {
                count = 0;
                channelCount++;
            }
            progressBar.step();
        }
        progressBar.close();
        int[] alphaChannel = Arrays.copyOfRange(alphaArray, 0, width * height);
        int[] redChannel = Arrays.copyOfRange(redArray, 0, width * height);
        int[] greenChannel = Arrays.copyOfRange(greenArray, 0, width * height);
        int[] blueChannel = Arrays.copyOfRange(blueArray, 0, width * height);
        return reconstructImage(alphaChannel, redChannel, greenChannel, blueChannel, width, height);
    }

    private int[] insertSubarray(int[] first, int[] second, int index) {
        // Create a new array to hold the result
        int[] result = new int[first.length + second.length];

        // Copy elements before the index from the first array
        System.arraycopy(first, 0, result, 0, index);

        // Copy the second array (subarray) into the result
        System.arraycopy(second, 0, result, index, second.length);

        // Copy the remaining elements from the first array
        System.arraycopy(first, index, result, index + second.length, first.length - index);

        return result;
    }


    private BufferedImage reconstructImage(int[] a, int[] r, int[] g, int[] b, int width, int height) {
        // Create a new BufferedImage with the given width and height
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Iterate through each pixel and combine the individual channels into an ARGB value
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Calculate the index of the current pixel
                int index = y * width + x;

                // Combine alpha, red, green, and blue channels into a single ARGB value
                int argb = (a[index] << 24) | (r[index] << 16) | (g[index] << 8) | b[index];

                // Set the pixel at the specified (x, y) position
                image.setRGB(x, y, argb);
            }
        }

        return image;
    }
}
