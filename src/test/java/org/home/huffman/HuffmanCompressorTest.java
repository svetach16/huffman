package org.home.huffman;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class HuffmanCompressorTest {
    @Test
    public void testCompression(@TempDir Path tempDir) throws IOException {
        final Path input = tempDir.resolve("input");
        final Path output = tempDir.resolve("output.huf");

        try (final OutputStream outputStream = Files.newOutputStream(input)) {
            outputStream.write(new byte[]{'b', 'a', 'c', 'a'});
        }

        HuffmanCompressor.encode(input, output);

        final byte[] actual = Files.readAllBytes(output);
        final byte[] expected = {
                2,
                'a',
                0, 0, 0, 0, 0, 0, 0, 2,
                'b',
                0, 0, 0, 0, 0, 0, 0, 1,
                'c',
                0, 0, 0, 0, 0, 0, 0, 1,
                0x19
        };

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testDecompression(@TempDir Path tempDir) throws IOException {
        final Path input = tempDir.resolve("input.huf");
        final Path output = tempDir.resolve("output");

        try (final OutputStream outputStream = Files.newOutputStream(input)) {
            outputStream.write(new byte[]{
                    2,
                    'a',
                    0, 0, 0, 0, 0, 0, 0, 2,
                    'b',
                    0, 0, 0, 0, 0, 0, 0, 1,
                    'c',
                    0, 0, 0, 0, 0, 0, 0, 1,
                    0x19
            });
        }

        HuffmanCompressor.decode(input, output);

        final byte[] actual = Files.readAllBytes(output);
        final byte[] expected = {'b', 'a', 'c', 'a'};

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testDecompressionBadInput(@TempDir Path tempDir) throws IOException {
        final Path input = tempDir.resolve("input.huf");
        final Path output = tempDir.resolve("output");

        try (final OutputStream outputStream = Files.newOutputStream(input)) {
            outputStream.write(new byte[]{
                    2,
                    'a',
                    0, 0, 0, 0, 0, 0, 0, 2,
                    'b',
                    0, 0, 0, 0, 0, 0, 0, 1,
                    'c',
                    0, 0, 0, 0, 0, 0, 0, 1
            });
        }

        assertThrows(
                EOFException.class,
                () -> HuffmanCompressor.decode(input, output),
                "Unexpected EOF when we decoded the bits"
        );
    }

    @Test
    public void testDecompressionBadTable(@TempDir Path tempDir) throws IOException {
        final Path input = tempDir.resolve("input.huf");
        final Path output = tempDir.resolve("output");

        try (final OutputStream outputStream = Files.newOutputStream(input)) {
            outputStream.write(new byte[]{
                    2,
                    'a',
                    0, 0, 0, 0, 0, 0, 0, 2,
                    'b',
                    0, 0, 0, 0, 0, 0, 0, 1,
                    'c',
                    0, 0, 0, 0, 0, 0, 0
            });
        }

        assertThrows(
                EOFException.class,
                () -> HuffmanCompressor.decode(input, output)
        );
    }

    @Test
    public void testCompressionAllElementsOfTableASCII(@TempDir Path tempDir) throws IOException {
        final Path input = tempDir.resolve("input");
        final Path output = tempDir.resolve("output.huf");
        final Path result = tempDir.resolve("result");

        byte[] allBytes = new byte[256];

        for (int i = 0; i < 256; i++) {
            allBytes[i] = (byte) i;
        }

        try (final OutputStream outputStream = Files.newOutputStream(input)) {
            outputStream.write(allBytes);
        }

        HuffmanCompressor.encode(input, output);
        HuffmanCompressor.decode(output, result);

        final byte[] actual = Files.readAllBytes(result);

        Assertions.assertArrayEquals(allBytes, actual);
    }
}
