package utils;

public class MathUtils {
    public static double linearInterpolate(double min, double max, double fraction) {
        if (fraction < 0 || fraction > 1) {
            throw new IllegalArgumentException("Fraction invalid!");
        }
        return min + fraction * (max - min);
    }
}
