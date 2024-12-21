package org.christolio.Optimization;

import org.christolio.SIC.Metrics.Metrics;
import org.christolio.SIC.SIC;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SICOptimizer {
    private int minChunkSize, maxChunkSize, chunkSizeStep, minNumLevels, maxNumLevels, numLevelsStep;
    private CSVWriter csvWriter;
    private SIC sic;

    public SICOptimizer(int minChunkSize, int maxChunkSize, int chunkSizeStep, int minNumLevels, int maxNumLevels, int numLevelsStep, File outputCSVFile) throws IOException {
        this.minChunkSize = minChunkSize;
        this.maxChunkSize = maxChunkSize;
        this.chunkSizeStep = chunkSizeStep;
        this.minNumLevels = minNumLevels;
        this.maxNumLevels = maxNumLevels;
        this.numLevelsStep = numLevelsStep;
        csvWriter = new CSVWriter(outputCSVFile, new String[]{"numLevels", "chunkSize", "encTime", "decTime", "CR", "PSNR"});
    }

    public void optimize(File image, File outputFile) throws IOException, ExecutionException, InterruptedException {
        for (int j = minChunkSize; j <= maxChunkSize; j += chunkSizeStep) {
            for (int i = minNumLevels; i <= maxNumLevels; i += numLevelsStep) {
                System.out.println("New round: " + i + " levels, " + j + " chunkSize");
                sic = new SIC(i, j);
                double startTime = System.currentTimeMillis();
                Metrics metrics = sic.encode(image);
                double endTime = System.currentTimeMillis();
                double encTime = (endTime - startTime) / 1000.0;
                startTime = System.currentTimeMillis();
                sic.decode(outputFile);
                endTime = System.currentTimeMillis();
                double decTime = (endTime - startTime) / 1000.0;
                csvWriter.insertRow(new String[]{String.valueOf(i), String.valueOf(j), String.valueOf(encTime), String.valueOf(decTime), String.valueOf(metrics.getCompressionRatio()), String.valueOf(metrics.getPsnr())});
            }
        }
    }

}
