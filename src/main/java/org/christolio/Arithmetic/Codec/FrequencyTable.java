package org.christolio.Arithmetic.Codec;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class FrequencyTable implements Serializable {
    private Map<Integer, Integer> frequencies = new HashMap<>();
    private int dataSize = 0;
    private final int SCALE = 5;

    public FrequencyTable(Map<Integer, Integer> frequencies, int total) {
        this.frequencies = frequencies;
        this.dataSize = total;
    }

    public FrequencyTable(int[] data) {
        for (int symbol : data) {
            addSymbol(symbol);
        }
    }

    private void addSymbol(int symbol) {
        frequencies.put(symbol, frequencies.getOrDefault(symbol, 0) + 1);
        dataSize++;
    }

    public int getFrequency(int symbol) {
        return frequencies.getOrDefault(symbol, 0);
    }

    public int getDataSize() {
        return dataSize;
    }

    public BigDecimal getProbability(int symbol) {
        BigDecimal frequency = BigDecimal.valueOf(getFrequency(symbol));
        return frequency.divide(BigDecimal.valueOf(dataSize), SCALE, RoundingMode.HALF_UP).stripTrailingZeros();
    }

    public Map<Integer, BigDecimal> getCumulativeProbabilities() {
        Map<Integer, BigDecimal> cumulative = new HashMap<>();
        BigDecimal cumulativeProbability = BigDecimal.ZERO;
        for (Map.Entry<Integer, Integer> entry : frequencies.entrySet()) {
            cumulative.put(entry.getKey(), cumulativeProbability);
            cumulativeProbability = cumulativeProbability.add(getProbability(entry.getKey())).stripTrailingZeros();
        }
        return cumulative;
    }
}
