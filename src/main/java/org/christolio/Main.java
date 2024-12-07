package org.christolio;

import org.christolio.Arithmetic.ArithmeticDecoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedImage testImage = ImageIO.read(Objects.requireNonNull(Main.class.getResource("/testImage.png")));
        int[] message = new int[20];
        SortedMap<Integer,Double> map = new TreeMap<>();
        map.put(0,0.2);
        map.put(1,0.3);
        map.put(2,0.1);
        map.put(3,0.2);
        map.put(4,0.1);
        map.put(5,0.1);
        ArithmeticDecoder dec = new ArithmeticDecoder();
    }
}