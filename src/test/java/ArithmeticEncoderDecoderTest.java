import org.christolio.Arithmetic.Codec.ArithmeticDecoder;
import org.christolio.Arithmetic.Codec.ArithmeticEncodedData;
import org.christolio.Arithmetic.Codec.ArithmeticEncoder;
import org.junit.Test;

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
