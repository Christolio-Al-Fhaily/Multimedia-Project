package org.christolio.Arithmetic.Image;

import me.tongfei.progressbar.ProgressBar;
import org.christolio.Arithmetic.Codec.ArithmeticEncodedData;
import org.christolio.Arithmetic.Codec.ArithmeticEncoder;
import org.christolio.Arithmetic.Codec.FrequencyTable;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class ArithmeticImageEncoder {

    private int chunkSize;
    private final ArithmeticEncoder encoder = new ArithmeticEncoder();
    private int numberOfChunksPerChannel;
    private ProgressBar progressBar;
    private FrequencyTable freqTable;
    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


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

        int[] redArray = new int[numberOfChunksPerChannel * chunkSize];
        int[] greenArray = new int[numberOfChunksPerChannel * chunkSize];
        int[] blueArray = new int[numberOfChunksPerChannel * chunkSize];

        Arrays.fill(redArray, 0);
        Arrays.fill(greenArray, 0);
        Arrays.fill(blueArray, 0);


        // Flatten the image by reading pixel values row by row
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int pixel = imageToEncode.getRGB(x, y);

                redArray[y * width + x] = pixel >> 16 & 0xFF;
                greenArray[y * width + x] = pixel >> 8 & 0xFF;
                blueArray[y * width + x] = pixel & 0xFF;
            }
        }
        freqTable = new FrequencyTable(concatenateChannels(redArray, greenArray, blueArray));
        progressBar = ProgressBar.builder().setTaskName("Encoding channels").setInitialMax(3).showSpeed().build();

        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());
        List<Future<List<ArithmeticEncodedData>>> futures = new ArrayList<>();

        futures.add(executor.submit(() -> encodeChannel(redArray)));
        futures.add(executor.submit(() -> encodeChannel(greenArray)));
        futures.add(executor.submit(() -> encodeChannel(blueArray)));

        List<ArithmeticEncodedData> encodedRedChannel = futures.get(0).get();
        List<ArithmeticEncodedData> encodedGreenChannel = futures.get(1).get();
        List<ArithmeticEncodedData> encodedBlueChannel = futures.get(2).get();

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        progressBar.close();


        ArithmeticImageEncodedData encodedImage = new ArithmeticImageEncodedData(width, height, chunkSize);
        encodedImage.addChunks(encodedRedChannel);
        encodedImage.addChunks(encodedGreenChannel);
        encodedImage.addChunks(encodedBlueChannel);
        System.out.println("Encoded " + encodedImage.getEncodedChunks().size() + " chunks");
        return encodedImage;
    }

    private List<ArithmeticEncodedData> encodeChannel(int[] channel) {
        if (channel.length == 0) return Collections.emptyList();

        List<ArithmeticEncodedData> encodedChannel = new ArrayList<>();
        List<Callable<ArithmeticEncodedData>> chunkTasks = new ArrayList<>();

        // Create tasks for each chunk
        for (int i = 0; i < numberOfChunksPerChannel; i++) {
            final int start = i * chunkSize;
            final int end = (i + 1) * chunkSize; // Ensure we don't go out of bounds
            chunkTasks.add(() -> {
                int[] chunk = Arrays.copyOfRange(channel, start, end);
                return encoder.encode(chunk);
            });
        }

        try {
            // Execute all tasks and wait for them to finish
            List<Future<ArithmeticEncodedData>> futures = executor.invokeAll(chunkTasks);

            // Collect the results once all tasks are done
            for (Future<ArithmeticEncodedData> future : futures) {
                encodedChannel.add(future.get());  // Blocking here but only once per task group
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        progressBar.step();
        return encodedChannel;
    }

    private int[] concatenateChannels(int[] array1, int[] array2, int[] array3) {
        // Calculate the total length of the concatenated array
        int totalLength = array1.length + array2.length + array3.length;

        // Create a new array to hold all the elements
        int[] result = new int[totalLength];

        // Copy elements from each array into the result array
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        System.arraycopy(array3, 0, result, array1.length + array2.length, array3.length);

        return result;
    }


    public FrequencyTable getFreqTable() {
        return freqTable;
    }

    public void setFreqTable(FrequencyTable freqTable) {
        this.freqTable = freqTable;
    }
}
