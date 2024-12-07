package org.christolio.Arithmetic;

import java.util.HashMap;
import java.util.Map;

public class FrequencyTable {
    private Map<Integer, Integer> frequencies = new HashMap<>();
    private int dataSize = 0;

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

    public double getProbability(int symbol) {
        return (double) getFrequency(symbol) / dataSize;
    }

    public Map<Integer, Double> getCumulativeProbabilities() {
        Map<Integer, Double> cumulative = new HashMap<>();
        double cumulativeProbability = 0.0;
        for (Map.Entry<Integer, Integer> entry : frequencies.entrySet()) {
            cumulative.put(entry.getKey(), cumulativeProbability);
            cumulativeProbability += getProbability(entry.getKey());
        }
        return cumulative;
    }
}
