package org.christolio.SIC;

import org.christolio.Arithmetic.Image.ArithmeticImageDecoder;
import org.christolio.Arithmetic.Image.ArithmeticImageEncodedData;
import org.christolio.Arithmetic.Image.ArithmeticImageEncoder;
import org.christolio.PQ.PredictiveQuantizer;
import org.christolio.SIC.IO.SICReader;
import org.christolio.SIC.IO.SICWriter;
import org.christolio.SIC.Metrics.Metrics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Super Inefficient Codec
 */
public class SIC {

    private PredictiveQuantizer quantizer;
    private ArithmeticImageEncoder encoder;
    private ArithmeticImageDecoder decoder;
    private int numLevels;
    private int chunkSize;

    public SIC(int numLevels, int chunkSize) {
        quantizer = new PredictiveQuantizer(numLevels);
        encoder = new ArithmeticImageEncoder(chunkSize);
        decoder = new ArithmeticImageDecoder();
        this.numLevels = numLevels;
        this.chunkSize = chunkSize;
    }

    public Metrics encode(File imageFile) throws IOException, ExecutionException, InterruptedException {
        String filePath = imageFile.getPath();
        verifyPNG(filePath);
        BufferedImage image = ImageIO.read(Objects.requireNonNull(imageFile));
        BufferedImage quantizedImage = quantizer.quantizeGreenChannel(image);
        ArithmeticImageEncodedData encodedData = encoder.encodeImage(quantizedImage);
        long compressedSize = SICWriter.write(encodedData, imageFile.getParentFile().getPath(), "encoded_" + imageFile.getName().substring(0, imageFile.getName().lastIndexOf(".")));
        return new Metrics(imageFile.length(), compressedSize, image);
    }

    public BufferedImage decode(File inputFile) throws ExecutionException, InterruptedException, IOException {
        ArithmeticImageEncodedData encodedData = SICReader.read(inputFile.getPath());
        BufferedImage decodedImage = decoder.decode(encodedData);
        BufferedImage dequantizedImage = quantizer.decodeGreenChannel(decodedImage);
        ImageIO.write(dequantizedImage, "PNG", new File(inputFile.getParentFile().getPath() + "\\decoded_" + inputFile.getName().substring(inputFile.getName().indexOf("_")+1,inputFile.getName().lastIndexOf(".")) + ".png"));
        return dequantizedImage;
    }

    public int getNumLevels() {
        return numLevels;
    }

    public void setNumLevels(int numLevels) {
        this.numLevels = numLevels;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    private void verifyPNG(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        String extension = filePath.substring(lastDotIndex + 1);
        if (lastDotIndex == -1 || lastDotIndex == filePath.length() - 1 || !extension.equals("png")) {
            throw new RuntimeException("File must be png");
        }
    }
}
