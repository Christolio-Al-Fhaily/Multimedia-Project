package org.christolio.Arithmetic.Image;

import me.tongfei.progressbar.ProgressBar;
import org.christolio.Arithmetic.Codec.ArithmeticEncodedData;
import org.christolio.Arithmetic.Codec.ArithmeticEncoder;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ArithmeticImageEncoder {

    private int chunkSize;
    private final ArithmeticEncoder encoder = new ArithmeticEncoder();
    private int numberOfChunksPerChannel;
    private ProgressBar progressBar;

    public ArithmeticImageEncoder(int chunkSize) {
        if (chunkSize > 0)
            this.chunkSize = chunkSize;
        else throw new RuntimeException("ChunkSize must be > 0");
    }

    public ArithmeticImageEncodedData encodeImage(BufferedImage imageToEncode) throws ExecutionException, InterruptedException {
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

        progressBar = ProgressBar.builder().setTaskName("Encoding channels").setInitialMax(4).showSpeed().build();

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<List<ArithmeticEncodedData>>> futures = new ArrayList<>();

        futures.add(executor.submit(() -> encodeChannel(alphaArray)));
        futures.add(executor.submit(() -> encodeChannel(redArray)));
        futures.add(executor.submit(() -> encodeChannel(greenArray)));
        futures.add(executor.submit(() -> encodeChannel(blueArray)));

        List<ArithmeticEncodedData> encodedAlphaChannel = futures.get(0).get();
        List<ArithmeticEncodedData> encodedRedChannel = futures.get(1).get();
        List<ArithmeticEncodedData> encodedGreenChannel = futures.get(2).get();
        List<ArithmeticEncodedData> encodedBlueChannel = futures.get(3).get();
        executor.shutdown();
        progressBar.close();


        ArithmeticImageEncodedData encodedImage = new ArithmeticImageEncodedData(width, height, chunkSize);
        encodedImage.addChunks(encodedAlphaChannel);
        encodedImage.addChunks(encodedRedChannel);
        encodedImage.addChunks(encodedGreenChannel);
        encodedImage.addChunks(encodedBlueChannel);
        System.out.println("Encoded " + encodedImage.getEncodedChunks().size() + " chunks");
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
        progressBar.step();
        return encodedChannel;
    }
}
