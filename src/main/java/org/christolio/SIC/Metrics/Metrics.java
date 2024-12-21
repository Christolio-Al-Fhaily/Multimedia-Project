package org.christolio.SIC.Metrics;

import java.awt.image.BufferedImage;

public class Metrics {

    private double compressionRatio;
    private double psnr;

    public Metrics(long originalSize, long compressedSize, BufferedImage original, BufferedImage compressed) {
        this.compressionRatio = calculateCompressionRation(originalSize, compressedSize);
        this.psnr = calculatePSNR(original, compressed);
    }

    private double calculateCompressionRation(long originalSize, long compressedSize) {
        return (double) originalSize / compressedSize;
    }

    private double calculatePSNR(BufferedImage original, BufferedImage compressed) {
        if (original.getWidth() != compressed.getWidth() || original.getHeight() != compressed.getHeight()) {
            throw new IllegalArgumentException("Images must have the same dimensions.");
        }

        int width = original.getWidth();
        int height = original.getHeight();
        long mseAlpha = 0, mseRed = 0, mseGreen = 0, mseBlue = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Get ARGB values for each pixel
                int argbOriginal = original.getRGB(x, y);
                int argbAltered = compressed.getRGB(x, y);

                int alphaOriginal = (argbOriginal >> 24) & 0xFF;
                int redOriginal = (argbOriginal >> 16) & 0xFF;
                int greenOriginal = (argbOriginal >> 8) & 0xFF;
                int blueOriginal = argbOriginal & 0xFF;

                int alphaAltered = (argbAltered >> 24) & 0xFF;
                int redAltered = (argbAltered >> 16) & 0xFF;
                int greenAltered = (argbAltered >> 8) & 0xFF;
                int blueAltered = argbAltered & 0xFF;

                // Accumulate squared differences
                mseAlpha += Math.pow(alphaOriginal - alphaAltered, 2);
                mseRed += Math.pow(redOriginal - redAltered, 2);
                mseGreen += Math.pow(greenOriginal - greenAltered, 2);
                mseBlue += Math.pow(blueOriginal - blueAltered, 2);
            }
        }

        // Calculate total MSE for all channels
        long totalPixels = width * height;
        double mse = (mseAlpha + mseRed + mseGreen + mseBlue) / (4.0 * totalPixels);

        if (mse == 0) {
            // No difference between images
            return Double.POSITIVE_INFINITY;
        }

        // Calculate PSNR
        double maxPixelValue = 255.0;
        return 10 * Math.log10((maxPixelValue * maxPixelValue) / mse);
    }

    public double getCompressionRatio() {
        return compressionRatio;
    }

    public double getPsnr() {
        return psnr;
    }

    @Override
    public String toString() {
        return "CR: " + compressionRatio + "\nPSNR: " + psnr;
    }
}
