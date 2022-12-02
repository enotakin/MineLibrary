package com.enotakin.library.api.util;

public final class Mth {

    public static final double PI_2 = Math.PI * 2D;

    private Mth() {
    }

    public static int square(int value) {
        return value * value;
    }

    public static float square(float value) {
        return value * value;
    }

    public static double square(double value) {
        return value * value;
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        } else {
            return Math.min(value, max);
        }
    }

    public static float clamp(float value, float min, float max) {
        if (value < min) {
            return min;
        } else {
            return Math.min(value, max);
        }
    }

    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        } else {
            return Math.min(value, max);
        }
    }

    public static long floor(double value) {
        long result = (long) value;
        return value < (double) result ? result - 1L : result;
    }

    public static int floor(float value) {
        int result = (int) value;
        return value < (float) result ? result - 1 : result;
    }

    public static int ceil(float value) {
        return (int) Math.ceil(value);
    }

    public static long ceil(double value) {
        return (long) Math.ceil(value);
    }

    public static double round(double value, int length) {
        if (length < 0) {
            length = 0;
        }

        long factor = (long) Math.pow(10, length);

        value = value * factor;
        long roundedValue = Math.round(value);
        return (double) roundedValue / factor;
    }

}
