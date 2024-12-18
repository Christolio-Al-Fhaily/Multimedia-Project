package org.christolio.Arithmetic.Image;

import me.tongfei.progressbar.ProgressBar;
import org.christolio.Arithmetic.Codec.ArithmeticDecoder;
import org.christolio.Arithmetic.Codec.ArithmeticEncodedData;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ArithmeticImageDecoder {

    private ArithmeticDecoder decoder = new ArithmeticDecoder();
    private int chunkSize;
    private int numberOfChunksPerChannel;
    private int width, height;
    private ProgressBar progressBar;
    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    public BufferedImage decode(ArithmeticImageEncodedData encodedData) throws ExecutionException, InterruptedException {
        width = encodedData.getWidth();
        height = encodedData.getHeight();
        chunkSize = encodedData.getChunkSize();
        numberOfChunksPerChannel = (int) Math.ceil((double) (width * height) / chunkSize);

        List<ArithmeticEncodedData> encodedChunks = encodedData.getEncodedChunks();

        int alphaStopIndex = numberOfChunksPerChannel;
        int redStopIndex = 2 * numberOfChunksPerChannel;
        int greenStopIndex = 3 * numberOfChunksPerChannel;
        int blueStopIndex = 4 * numberOfChunksPerChannel;

        progressBar = ProgressBar.builder().setTaskName("Decoding chunks").setInitialMax(encodedChunks.size()).showSpeed().build();

        List<Future<int[]>> futures = new ArrayList<>();

        futures.add(executor.submit(() -> decodeChannel(encodedChunks, 0, alphaStopIndex)));
        futures.add(executor.submit(() -> decodeChannel(encodedChunks, alphaStopIndex , redStopIndex)));
        futures.add(executor.submit(() -> decodeChannel(encodedChunks, redStopIndex , greenStopIndex)));
        futures.add(executor.submit(() -> decodeChannel(encodedChunks, greenStopIndex , blueStopIndex)));

        // Wait for all decoding tasks to finish
        int[] alphaChannel = futures.get(0).get();
        int[] redChannel = futures.get(1).get();
        int[] greenChannel = futures.get(2).get();
        int[] blueChannel = futures.get(3).get();

        executor.shutdown();

        progressBar.close();

        return reconstructImage(alphaChannel, redChannel, greenChannel, blueChannel, width, height);
    }

    private int[] decodeChannel(List<ArithmeticEncodedData> encodedChunks, int startIndex, int stopIndex) throws InterruptedException, ExecutionException {
        int totalChunks = stopIndex - startIndex;
        int[] decodedChannel = new int[chunkSize * totalChunks];

        // Create a thread pool
        List<Future<int[]>> futures = new ArrayList<>();

        // Submit tasks for each chunk to the thread pool
        for (int i = startIndex; i < stopIndex; i++) {
            final int chunkIndex = i; // Capture index for the lambda expression
            futures.add(executor.submit(() -> decoder.decode(encodedChunks.get(chunkIndex))));
        }

        // Collect results and merge them into the decodedChannel array
        for (int i = 0; i < futures.size(); i++) {
            int[] chunk = futures.get(i).get();
            int index = i * chunkSize;

            // Copy the decoded chunk into the decodedChannel array
            System.arraycopy(chunk, 0, decodedChannel, index, chunk.length);
            progressBar.step();
        }

        return Arrays.copyOfRange(decodedChannel, 0, width * height);
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
