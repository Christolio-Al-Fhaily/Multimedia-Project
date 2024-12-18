package org.christolio.Arithmetic.Codec;

import java.io.Serializable;
import java.math.BigDecimal;

public class ArithmeticEncodedData implements Serializable {
    private BigDecimal encodedValue;
    private FrequencyTable frequencyTable;
    private int dataSize;

    public ArithmeticEncodedData(BigDecimal encodedValue, FrequencyTable frequencyTable, int dataSize) {
        this.encodedValue = encodedValue;
        this.frequencyTable = frequencyTable;
        this.dataSize = dataSize;
    }

    public BigDecimal getEncodedValue() {
        return encodedValue;
    }

    public void setEncodedValue(BigDecimal encodedValue) {
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
