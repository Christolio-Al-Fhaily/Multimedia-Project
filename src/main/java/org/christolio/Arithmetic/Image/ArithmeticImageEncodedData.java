package org.christolio.Arithmetic.Image;

import org.christolio.Arithmetic.ArithmeticEncodedData;

import java.util.ArrayList;
import java.util.List;

public class ArithmeticImageEncodedData {
    private int width;
    private int height;
    private int chunkSize;
    private List<ArithmeticEncodedData> encodedChunks = new ArrayList<>();

    public ArithmeticImageEncodedData(int width, int height, int chunkSize) {
        this.width = width;
        this.height = height;
        this.chunkSize = chunkSize;
    }

    public void addChunk(ArithmeticEncodedData data){
        encodedChunks.add(data);
    }

    public void addChunks(List<ArithmeticEncodedData> chunks){
        chunks.forEach(this::addChunk);
    }
    public void save(){
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public List<ArithmeticEncodedData> getEncodedChunks() {
        return encodedChunks;
    }

    public void setEncodedChunks(List<ArithmeticEncodedData> encodedChunks) {
        this.encodedChunks = encodedChunks;
    }
}
