package org.home.huffman;

import java.io.IOException;
import java.io.OutputStream;

public class BitWriter {
    private final OutputStream outputStream;
    private int sink;
    private int position;

    public BitWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    void write(int bit) throws IOException {
        sink |= bit << position++;

        if (position == 8) {
            outputStream.write(sink);
            sink = position = 0;
        }
    }

    void flush() throws IOException {
        if (position == 0) {
            return;
        }

        outputStream.write(sink);
        position = 0;
    }
}
