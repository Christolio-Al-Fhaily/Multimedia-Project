package org.christolio.Arithmetic.Codec;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        List<Map.Entry<Integer, BigDecimal>> cumulativeEntries = new ArrayList<>(cumulative.entrySet());
        cumulativeEntries.sort(Map.Entry.comparingByValue());

        for (int i = 0; i < dataSize; i++) {
            BigDecimal range = high.subtract(low);

            int left = 0, right = cumulativeEntries.size() - 1;
            while (left <= right) {
                int mid = (left + right) / 2;
                BigDecimal symbolLow = low.add(range.multiply(cumulativeEntries.get(mid).getValue()));
                BigDecimal symbolHigh = symbolLow.add(range.multiply(probabilities.get(cumulativeEntries.get(mid).getKey())));

                if (encodedValue.compareTo(symbolLow) >= 0 && encodedValue.compareTo(symbolHigh) < 0) {
                    decodedData[i] = cumulativeEntries.get(mid).getKey();
                    low = symbolLow;
                    high = symbolHigh;
                    break;
                } else if (encodedValue.compareTo(symbolHigh) < 0) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }

        return decodedData;
    }
}

