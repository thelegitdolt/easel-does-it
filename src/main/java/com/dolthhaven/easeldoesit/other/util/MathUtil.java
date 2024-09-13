package com.dolthhaven.easeldoesit.other.util;

@SuppressWarnings("unused")
public class MathUtil {
    public static int base4From2(int digit1, int digit2) {
        return digit1 * 4 + digit2;
    }

    public static int base4ExceptTheNumbersAre1234InsteadOf0123(int digit1, int digit2) {
        return base4From2(digit1 - 1, digit2 - 1);
    }

    public static int[] decodeBase4(int base4) {
        int first = base4 % 4;
        int second = base4 / 4;
        return new int[]{second, first};
    }
}