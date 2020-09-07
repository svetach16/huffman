package org.home.huffman;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class BitWriterTest {
    @Test
    public void testEmptyFile(@TempDir Path tempDir) throws IOException {
        final Path path = tempDir.resolve("file");

        try (final OutputStream outputStream = new FileOutputStream(path.toFile())) {
            final BitWriter bitWriter = new BitWriter(outputStream);

            for (int i = 0; i < 7; i++) {
                bitWriter.write(1);
            }
        }

        final byte[] bytes = Files.readAllBytes(path);

        Assertions.assertEquals(0, bytes.length);
    }

    @Test
    public void testPartialWrite(@TempDir Path tempDir) throws IOException {
        final Path path = tempDir.resolve("file");

        try (final OutputStream outputStream = new FileOutputStream(path.toFile())) {
            final BitWriter bitWriter = new BitWriter(outputStream);

            for (int i = 0; i < 7; i++) {
                bitWriter.write(1);
                bitWriter.write(0);
            }
        }

        final byte[] bytes = Files.readAllBytes(path);

        Assertions.assertEquals(1, bytes.length);
        Assertions.assertEquals(0x55, bytes[0]);
    }

    @Test
    public void testPartialWriteAndFlush(@TempDir Path tempDir) throws IOException {
        final Path path = tempDir.resolve("file");

        try (final OutputStream outputStream = new FileOutputStream(path.toFile())) {
            final BitWriter bitWriter = new BitWriter(outputStream);

            for (int i = 0; i < 7; i++) {
                bitWriter.write(1);
                bitWriter.write(0);
            }

            bitWriter.flush();
        }

        final byte[] bytes = Files.readAllBytes(path);

        Assertions.assertEquals(2, bytes.length);
        Assertions.assertEquals(0x55, bytes[0]);
        Assertions.assertEquals(0x15, bytes[1]);
    }

    @Test
    public void testDoubleFlush(@TempDir Path tempDir) throws IOException {
        final Path path = tempDir.resolve("file");

        try (final OutputStream outputStream = new FileOutputStream(path.toFile())) {
            final BitWriter bitWriter = new BitWriter(outputStream);

            for (int i = 0; i < 3; i++) {
                bitWriter.write(1);
                bitWriter.write(0);
            }

            bitWriter.flush();
            bitWriter.flush();
        }

        final byte[] bytes = Files.readAllBytes(path);

        Assertions.assertEquals(1, bytes.length);
        Assertions.assertEquals(0x15, bytes[0]);
    }

}
