package org.christolio.Arithmetic;

import java.util.Map;

public class ArithmeticEncoder {
    private double low = 0.0;
    private double high = 1.0;

    public ArithmeticEncodedData encode(int[] data) {
        FrequencyTable freqTable = new FrequencyTable(data);
        Map<Integer, Double> cumulative = freqTable.getCumulativeProbabilities();

        for (int symbol : data) {
            double range = high - low;
            high = low + range * (cumulative.get(symbol) + freqTable.getProbability(symbol));
            low = low + range * cumulative.get(symbol);
        }

        return new ArithmeticEncodedData((low + high) / 2, freqTable, data.length);
    }
}

