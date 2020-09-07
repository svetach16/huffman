package org.home.huffman;

import java.io.*;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamUtil {
    private StreamUtil() {
    }

    static void writeFreqTable(Map<Byte, Long> statistics, OutputStream output) throws IOException {
        final DataOutputStream dataOutputStream = new DataOutputStream(output);

        dataOutputStream.writeByte(statistics.size() - 1);

        for (Map.Entry<Byte, Long> entry : statistics.entrySet()) {
            dataOutputStream.writeByte(entry.getKey());
            dataOutputStream.writeLong(entry.getValue());
        }
    }


    static Map<Byte, Long> calculateFreq(InputStream stream) throws IOException {
        final BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);
        final long[] array = new long[256];
        int tmp;

        while ((tmp = bufferedInputStream.read()) != -1) {
            array[tmp]++;
        }

        return IntStream.range(0, array.length)
                .filter(it -> array[it] != 0)
                .boxed()
                .collect(Collectors.toMap(it -> (byte)(int)it, it -> array[it]));
    }
}
