import org.christolio.Arithmetic.ArithmeticDecoder;
import org.christolio.Arithmetic.ArithmeticEncodedData;
import org.christolio.Arithmetic.ArithmeticEncoder;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class ArithmeticEncoderDecoderTest {

    @Test
    public void testArithmeticEncoder() {
        int[] message = {1, 2, 3, 3, 4};
        ArithmeticEncoder encoder = new ArithmeticEncoder();
        ArithmeticEncodedData encodedData = encoder.encode(message);
        assertEquals(0.06752, encodedData.getEncodedValue().doubleValue(),10e-5);
        ArithmeticDecoder decoder = new ArithmeticDecoder();
        int[] decodedMessage = decoder.decode(encodedData);
        assertArrayEquals(message, decodedMessage);
    }
}
