package org.home.huffman;

import java.util.*;
import java.util.stream.Collectors;

public class HuffmanTree {
    private final HuffmanTree right;
    private final HuffmanTree left;
    private final byte symbol;
    private final long freq;

    public HuffmanTree(HuffmanTree right, HuffmanTree left, byte symbol, long freq) {
        this.right = right;
        this.left = left;
        this.symbol = symbol;
        this.freq = freq;
    }

    public HuffmanTree(byte symbol, long freq) {
        this.symbol = symbol;
        this.freq = freq;
        this.right = null;
        this.left = null;
    }

    public static HuffmanTree buildHuffmanTree(Map<Byte, Long> frequencies) {
        final PriorityQueue<HuffmanTree> queue = new PriorityQueue<>(
                Comparator.comparing(HuffmanTree::getFreq).thenComparing(HuffmanTree::getSymbol)
        );
        final List<HuffmanTree> trees = frequencies.entrySet().stream()
                .map(it -> new HuffmanTree(it.getKey(), it.getValue()))
                .collect(Collectors.toList());

        queue.addAll(trees);

        while (queue.size() > 1) {
            final HuffmanTree left = queue.remove();
            final HuffmanTree right = queue.remove();

            final HuffmanTree tree = new HuffmanTree(right, left, (byte)Math.max(left.symbol, right.symbol), left.freq + right.freq);

            queue.add(tree);
        }

        return queue.remove();
    }

    public Map<Byte, String> getPaths() {
        final Map<Byte, String> paths = new HashMap<>();

        getPaths(this, paths, new char[256], 0);

        return paths;
    }

    public HuffmanTree getRight() {
        return right;
    }

    public HuffmanTree getLeft() {
        return left;
    }

    public byte getSymbol() {
        return symbol;
    }

    public long getFreq() {
        return freq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HuffmanTree tree = (HuffmanTree) o;
        return symbol == tree.symbol &&
                freq == tree.freq &&
                Objects.equals(right, tree.right) &&
                Objects.equals(left, tree.left);
    }

    @Override
    public int hashCode() {
        return Objects.hash(right, left, symbol, freq);
    }

    @Override
    public String toString() {
        return "org.home.huffman.HuffmanTree{" +
                "right=" + right +
                ", left=" + left +
                ", symbol=" + symbol +
                ", freq=" + freq +
                '}';
    }

    private static void getPaths(HuffmanTree huffmanTree, Map<Byte, String> paths, char[] currentPath, int level) {
        if (huffmanTree == null) {
            return;
        }

        if (huffmanTree.right == null && huffmanTree.left == null) {
            paths.put(huffmanTree.symbol, String.valueOf(Arrays.copyOfRange(currentPath, 0, level)));
        }

        currentPath[level] = '0';
        getPaths(huffmanTree.left, paths, currentPath, level + 1);

        currentPath[level] = '1';
        getPaths(huffmanTree.right, paths, currentPath, level + 1);
    }
}
