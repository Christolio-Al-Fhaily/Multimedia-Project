package org.christolio.Arithmetic;

import me.tongfei.progressbar.ProgressBar;

import java.math.BigDecimal;
import java.util.Map;

public class ArithmeticEncoder {

    public ArithmeticEncodedData encode(int[] data) {
        FrequencyTable freqTable = new FrequencyTable(data);
        Map<Integer, BigDecimal> cumulative = freqTable.getCumulativeProbabilities();

        BigDecimal low = BigDecimal.ZERO;
        BigDecimal high = BigDecimal.ONE;

        ProgressBar progressBar = new ProgressBar("Encoding", data.length);
        for (int symbol : data) {
            progressBar.step();
            BigDecimal range = high.subtract(low);
            high = low.add(range.multiply(cumulative.get(symbol).add(freqTable.getProbability(symbol))));
            low = low.add(range.multiply(cumulative.get(symbol)));
        }
        progressBar.close();
        return new ArithmeticEncodedData(low.stripTrailingZeros(), freqTable, data.length);
    }
}

