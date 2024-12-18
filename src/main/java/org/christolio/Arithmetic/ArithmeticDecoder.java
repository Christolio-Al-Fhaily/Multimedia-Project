package org.christolio.Arithmetic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ArithmeticDecoder {
    public int[] decode(ArithmeticEncodedData encodedData) {
        int dataSize = encodedData.getDataSize();
        FrequencyTable freqTable = encodedData.getFrequencyMap();
        BigDecimal encodedValue = encodedData.getEncodedValue().stripTrailingZeros();

        int[] decodedData = new int[dataSize];

        Map<Integer, BigDecimal> cumulative = freqTable.getCumulativeProbabilities();
        Map<Integer, BigDecimal> probabilities = new HashMap<>();
        for (int symbol : cumulative.keySet()) {
            probabilities.put(symbol, freqTable.getProbability(symbol));
        }

        BigDecimal low = BigDecimal.ZERO;
        BigDecimal high = BigDecimal.ONE;

        for (int i = 0; i < dataSize; i++) {
            BigDecimal range = high.subtract(low);

            for (Map.Entry<Integer, BigDecimal> entry : cumulative.entrySet()) {
                int symbol = entry.getKey();
                BigDecimal symbolLow = low.add(range.multiply(entry.getValue()));
                BigDecimal symbolHigh = symbolLow.add(range.multiply(probabilities.get(symbol)));

                if (encodedValue.compareTo(symbolLow) >= 0 && encodedValue.compareTo(symbolHigh) < 0) {
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

