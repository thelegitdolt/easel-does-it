package com.dolthhaven.easeldoesit.other.util;

import it.unimi.dsi.fastutil.Function;

import java.util.List;

@SuppressWarnings("unused")
public class MathUtil {
    public static int base4From2(int digit1, int digit2) {
        return digit1 * 4 + digit2;
    }

    public static int base4ExceptTheNumbersAre1234InsteadOf0123(int digit1, int digit2) {
        return base4From2(digit1 - 1, digit2 - 1);
    }

    public static int base5From2(int digit1, int digit2) {
        return digit1 * 5 + digit2;
    }

    public static double normalizeScroll(double velocity) {
        double sign = velocity > 0 ? 1 : -1;
        if (Math.abs(velocity) > 30) {
            velocity = 30 * sign;
        }

        double newVal = 2 / (1 + Math.exp(-Math.abs(velocity) / 3) ) + 3;
        return newVal * sign;
    }

    public static <U> U decisionTree(List<U> choices, boolean... branches) {
        int index = 0;
        for (boolean branch : branches) {
            if (branch) break;
            index++;
        }
        return choices.get(index);
    }
}