package net.treset.adaptiveview.tools;

public class MathTools {
    public static long longArrayAverage(Long[] values)
    {
        if (values == null || values.length == 0) {
            return 0;
        }

        long sum = 0;
        for (long e : values) {
            sum += e;
        }
        return sum / values.length;
    }
}
