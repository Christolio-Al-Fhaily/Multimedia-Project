package org.christolio.Arithmetic;

import java.util.HashMap;
import java.util.Map;

public class ArithmeticDecoder {
    public int[] decode(ArithmeticEncodedData encodedData) {
        int dataSize = encodedData.getDataSize();
        FrequencyTable freqTable = encodedData.getFrequencyMap();
        double encodedValue = encodedData.getEncodedValue();

        int[] decodedData = new int[dataSize];

        Map<Integer, Double> cumulative = freqTable.getCumulativeProbabilities();
        Map<Integer, Double> probabilities = new HashMap<>();
        for (int symbol : cumulative.keySet()) {
            probabilities.put(symbol, freqTable.getProbability(symbol));
        }

        double low = 0.0;
        double high = 1.0;

        for (int i = 0; i < dataSize; i++) {
            double range = high - low;

            for (Map.Entry<Integer, Double> entry : cumulative.entrySet()) {
                int symbol = entry.getKey();
                double symbolLow = low + range * entry.getValue();
                double symbolHigh = symbolLow + range * probabilities.get(symbol);

                if (encodedValue >= symbolLow && encodedValue < symbolHigh) {
                    decodedData[i] = symbol;
                    low = symbolLow;
                    high = symbolHigh;
                    break;
                }
            }
        }
        return decodedData;
    }
}

