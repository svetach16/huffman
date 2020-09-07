package org.home.huffman;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StreamUtilTest {

    @Test
    public void emptyStreamTest() throws IOException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[]{});
        final Map<Byte, Long> frequencies = StreamUtil.calculateFreq(stream);

        assertEquals(0, frequencies.size());
    }

    @Test
    public void simpleTest() throws IOException {
        final ByteArrayInputStream stream = new ByteArrayInputStream(new byte[]{0, 1, 0, 2, 3});
        final Map<Byte, Long> frequencies = StreamUtil.calculateFreq(stream);

        assertEquals(4, frequencies.size());
        assertEquals(2L, frequencies.get((byte)0));
        assertEquals(1L, frequencies.get((byte)1));
        assertEquals(1L, frequencies.get((byte)2));
        assertEquals(1L, frequencies.get((byte)3));
    }
}