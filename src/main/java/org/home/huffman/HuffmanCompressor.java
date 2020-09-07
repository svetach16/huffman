package org.home.huffman;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class HuffmanCompressor {
    private HuffmanCompressor() {
    }

    public static void encode(Path input, Path output) throws IOException {
        final Map<Byte, Long> frequencies;

        try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(input))) {
            frequencies = StreamUtil.calculateFreq(inputStream);
        }

        if (frequencies.isEmpty()) return;

        try (DataInputStream inputStream = new DataInputStream(new BufferedInputStream(Files.newInputStream(input)));
             OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(output))) {

            final HuffmanTree huffmanTree = HuffmanTree.buildHuffmanTree(frequencies);
            final Map<Byte, String> paths = huffmanTree.getPaths();

            StreamUtil.writeFreqTable(frequencies, outputStream);
            final BitWriter bitWriter = new BitWriter(outputStream);
            int tmp;

            while ((tmp = inputStream.read()) != -1) {
                final String path = paths.get((byte) tmp);

                for (int i = 0; i < path.length(); i++) {
                    bitWriter.write(path.charAt(i) == '0' ? 0 : 1);
                }
            }

            bitWriter.flush();
        }
    }

    public static void decode(Path input, Path output) throws IOException {
        try (DataInputStream inputStream = new DataInputStream(new BufferedInputStream(Files.newInputStream(input)));
             OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(output))) {
            int countCharters = inputStream.read();

            if (countCharters == -1) return;

            final Map<Byte, Long> frequenciesByte = new HashMap<>();

            for (int i = countCharters; i >= 0; i--) {
                byte characters = inputStream.readByte();
                long freq = inputStream.readLong();

                frequenciesByte.put(characters, freq);
            }

            final HuffmanTree huffmanTree = HuffmanTree.buildHuffmanTree(frequenciesByte);
            final BitReader bitReader = new BitReader(inputStream);

            for (int i = 0; i < huffmanTree.getFreq(); i++) {
                HuffmanTree tmp = huffmanTree;

                while (tmp.getLeft() != null && tmp.getRight() != null) {
                    final int bit = bitReader.read();

                    switch (bit) {
                        case 0:
                            tmp = tmp.getLeft();
                            break;
                        case 1:
                            tmp = tmp.getRight();
                            break;
                        default:
                            throw new EOFException("Unexpected EOF while decoding");
                    }
                }

                outputStream.write(tmp.getSymbol());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3 || (!"-d".equals(args[0]) && !"-e".equals(args[0]))) {
            System.err.println("Wrong cmd format, must be \n\"huffman.jar -e input output\" \nor\n\"huffman.jar -d input output\"");
            return;
        }

        final Path input = Paths.get(args[1]);
        final Path output = Paths.get(args[2]);

        if (!input.toFile().exists()) {
            System.err.println("Input does not exist");
            return;
        }

        switch (args[0]) {
            case "-e":
                HuffmanCompressor.encode(input, output);
                break;
            case "-d":
                HuffmanCompressor.decode(input, output);
                break;
        }
    }
}

