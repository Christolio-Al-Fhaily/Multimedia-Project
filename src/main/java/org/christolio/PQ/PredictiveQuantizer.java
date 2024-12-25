package org.christolio.PQ;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PredictiveQuantizer {

    private int numLevels;

    public PredictiveQuantizer(int numLevels) {
        if (numLevels > 0)
            this.numLevels = numLevels;
        else throw new RuntimeException("Quantization levels must be > 0");
    }

    /**
     * Quantize the green channel using predictive quantization
     *
     * @return An image with the quantized green channel and
     * untouched alpha, red and blue channels.
     */
    public BufferedImage quantizeGreenChannel(BufferedImage imageToQuantize) {
        int width = imageToQuantize.getWidth();
        int height = imageToQuantize.getHeight();
        int[][] greenChannel = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int pixel = imageToQuantize.getRGB(x, y);

                int green = pixel >> 8 & 0xFF;

                greenChannel[y][x] = green;
            }
        }

        BufferedImage quantizedImage = copyBufferedImage(imageToQuantize);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int quantizedValue = predictAndQuantize(i, j, greenChannel);

                // Get the current RGB value of the pixel
                int rgb = quantizedImage.getRGB(j, i);

                // Clear the existing green channel (bits 8-15) by ANDing with a mask
                rgb = rgb & 0xFFFF00FF;

                // Set the new green value by ORing it into the correct position (bits 8-15)
                rgb = rgb | (quantizedValue << 8);

                // Update the pixel with the modified RGB value
                quantizedImage.setRGB(j, i, rgb);
            }
        }
        return quantizedImage;
    }

    /**
     * Calculate the error between the pixel value and the prediction
     *
     * @param i   The row of the current pixel
     * @param j   The column of the current pixel
     * @param img The channel as a two-dimensional array.
     * @return The prediction error
     */

    private int predictAndQuantize(int i, int j, int[][] img) {
        double minResidual = -255.0;
        double maxResidual = 255.0;

        double residual = calculateResidual(i, j, img);

        // Normalize the residual to fit within the range of quantization levels
        double normalized = (residual - minResidual) / (maxResidual - minResidual);
        int quantizedResidual = (int) Math.round(normalized * (numLevels - 1));

        // Ensure it's within the quantization range
        quantizedResidual = Math.max(0, Math.min(quantizedResidual, numLevels - 1));

        return quantizedResidual;
    }

    private double calculateResidual(int i, int j, int[][] img) {
        double predictedValue = predict8Neighbors(i, j, img);
        int actualValue = img[i][j];
        return actualValue - predictedValue;
    }

    /**
     * This function predicts the value of the pixel at position (i,j) by averaging its 8-neighbors
     *
     * @param i       The row of the current pixel
     * @param j       The column of the current pixel
     * @param channel The channel as a two-dimensional array.
     * @return The pridicted value of the pixel
     */
    private double predict8Neighbors(int i, int j, int[][] channel) {
        int height = channel.length;
        int width = channel[0].length;
        int[] neighbors = new int[8];
        int index = 0;

        // Top-left, top, top-right
        if (i > 0 && j > 0) neighbors[index++] = channel[i - 1][j - 1];
        if (i > 0) neighbors[index++] = channel[i - 1][j];
        if (i > 0 && j < width - 1) neighbors[index++] = channel[i - 1][j + 1];

        // Left, right
        if (j > 0) neighbors[index++] = channel[i][j - 1];
        if (j < width - 1) neighbors[index++] = channel[i][j + 1];

        // Bottom-left, bottom, bottom-right
        if (i < height - 1 && j > 0) neighbors[index++] = channel[i + 1][j - 1];
        if (i < height - 1) neighbors[index++] = channel[i + 1][j];
        if (i < height - 1 && j < width - 1) neighbors[index++] = channel[i + 1][j + 1];

        // Calculate the average of the neighbors
        double sum = 0;
        for (int value : neighbors) sum += value;

        return sum / neighbors.length;
    }

    private BufferedImage copyBufferedImage(BufferedImage original) {
        // Create a new BufferedImage with the same width, height, and image type
        BufferedImage copy = new BufferedImage(
                original.getWidth(),
                original.getHeight(),
                original.getType()
        );

        // Get the Graphics2D object of the copy and draw the original image onto it
        Graphics2D g = copy.createGraphics();
        g.drawImage(original, 0, 0, null);
        g.dispose(); // Dispose the Graphics2D object

        return copy;
    }
}
