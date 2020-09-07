package org.home.huffman;

import org.home.huffman.HuffmanTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class HuffmanTreeTest {
    @Test
    public void protocolTest() {
        final Map<Byte, Long> frequencies = Map.of(
                (byte) 1, 1L,
                (byte) 4, 3L,
                (byte) 3, 1L,
                (byte) 2, 3L,
                (byte) 5, 2L
        );
        final HuffmanTree tree = HuffmanTree.buildHuffmanTree(frequencies);
        final HuffmanTree expected =
                new HuffmanTree(
                        new HuffmanTree(
                                new HuffmanTree((byte) 4, 3),
                                new HuffmanTree((byte) 2, 3),
                                (byte) 4, 6
                        ),
                        new HuffmanTree(
                                new HuffmanTree((byte) 5, 2),
                                new HuffmanTree(
                                        new HuffmanTree((byte) 3, 1),
                                        new HuffmanTree((byte) 1, 1),
                                        (byte) 3, 2
                                ),
                                (byte) 5, 4
                        ),
                        (byte) 5, 10
                );

        Assertions.assertEquals(expected, tree);
    }

    @Test
    public void pathTest() {
        final Map<Byte, Long> frequencies = Map.of(
                (byte) 1, 1L,
                (byte) 4, 3L,
                (byte) 3, 1L,
                (byte) 2, 3L,
                (byte) 5, 2L
        );
        final HuffmanTree tree = HuffmanTree.buildHuffmanTree(frequencies);
        final Map<Byte, String> paths = tree.getPaths();

        Assertions.assertEquals(5, paths.size());
        Assertions.assertEquals("01", paths.get((byte)5));
        Assertions.assertEquals("000", paths.get((byte)1));
        Assertions.assertEquals("10", paths.get((byte)2));
        Assertions.assertEquals("001", paths.get((byte)3));
        Assertions.assertEquals("11", paths.get((byte)4));
    }
}
