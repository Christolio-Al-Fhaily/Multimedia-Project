package org.christolio.Arithmetic;

public class ArithmeticEncodedData {
    private double encodedValue;
    private FrequencyTable frequencyTable;
    private int dataSize;

    public ArithmeticEncodedData(double encodedValue, FrequencyTable frequencyTable, int dataSize) {
        this.encodedValue = encodedValue;
        this.frequencyTable = frequencyTable;
        this.dataSize = dataSize;
    }

    public double getEncodedValue() {
        return encodedValue;
    }

    public void setEncodedValue(double encodedValue) {
        this.encodedValue = encodedValue;
    }

    public FrequencyTable getFrequencyMap() {
        return frequencyTable;
    }

    public void setFrequencyMap(FrequencyTable frequencyTable) {
        this.frequencyTable = frequencyTable;
    }

    public int getDataSize() {
        return dataSize;
    }

    public void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }
}
