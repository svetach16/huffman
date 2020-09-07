package org.home.huffman;

import org.home.huffman.BitReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BitReaderTest {
    @Test
    public void testEmptyStream() throws IOException {
        final InputStream inputStream = new ByteArrayInputStream(new byte[]{});
        final BitReader bitReader = new BitReader(inputStream);

        Assertions.assertEquals(-1, bitReader.read());
    }

    @Test
    public void testReader() throws IOException {
        final BitReader bitReader = new BitReader(new ByteArrayInputStream(new byte[]{(byte) 0xF0, (byte) 0x0A}));
        final int[] actual = IntStream.iterate(
                bitReader.read(),
                it -> it != -1,
                it -> {
                    try {
                        return bitReader.read();
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
        ).toArray();
        final int[] expected = new int[]{0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0};

        Assertions.assertArrayEquals(expected, actual);
    }
}
