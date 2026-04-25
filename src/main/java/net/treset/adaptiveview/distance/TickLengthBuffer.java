package net.treset.adaptiveview.distance;

public class TickLengthBuffer {
    private final long[] buffer;
    private int index = 0;
    private int count = 0;

    public TickLengthBuffer(int capacity) {
        this.buffer = new long[capacity];
    }

    public void add(long value) {
        buffer[index] = value;
        index = (index + 1) % buffer.length;
        if (count < buffer.length) count++;
    }

    public double averageMillis() {
        if (count == 0) return 0d;
        long sum = 0;
        for (int i = 0; i < count; i++) {
            sum += buffer[i];
        }
        return (sum / (double) count) / 1_000_000d;
    }

    public void reset() {
        index = 0;
        count = 0;
    }
}
