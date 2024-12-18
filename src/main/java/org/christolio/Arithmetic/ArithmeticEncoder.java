package org.christolio.Arithmetic;

import java.math.BigDecimal;
import java.util.Map;

public class ArithmeticEncoder {

    public ArithmeticEncodedData encode(int[] data) {
        FrequencyTable freqTable = new FrequencyTable(data);
        Map<Integer, BigDecimal> cumulative = freqTable.getCumulativeProbabilities();

        BigDecimal low = BigDecimal.ZERO;
        BigDecimal high = BigDecimal.ONE;
        for (int symbol : data) {
            BigDecimal range = high.subtract(low);
            high = low.add(range.multiply(cumulative.get(symbol).add(freqTable.getProbability(symbol))));
            low = low.add(range.multiply(cumulative.get(symbol)));
        }
        return new ArithmeticEncodedData(low.stripTrailingZeros(), freqTable, data.length);
    }
}

