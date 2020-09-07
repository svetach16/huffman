package org.home.huffman;

import java.io.IOException;
import java.io.InputStream;

public class BitReader {
    private final InputStream inputStream;
    private int sink;
    private int position;

    public BitReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    int read() throws IOException {
        if (position % 8 == 0 && sink != -1) {
            sink = inputStream.read();
            position = 0;
        }

        return sink != -1 ? (sink >> position++) & 1 : -1;
    }
}
