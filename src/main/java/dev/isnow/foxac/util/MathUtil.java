package dev.isnow.foxac.util;

import lombok.experimental.UtilityClass;
import lombok.var;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class MathUtil {

    /**
     @author Elevated
     @credits github.com/Frequency
     */
    public static double getGcd(final double a, final double b) {
        try {
            if (a < b) {
                return getGcd(b, a);
            }

            if (Math.abs(b) < 0.001) {
                return a;
            } else {
                return getGcd(b, a - Math.floor(a / b) * b);
            }
        } catch (StackOverflowError e) {
            return 0;
        }
    }

    /**
     @author Elevated
     @credits github.com/Frequency
     */
    public Number getMode(Collection<? extends Number> samples) {
        Map<Number, Integer> frequencies = new HashMap<>();

        samples.forEach(i -> frequencies.put(i, frequencies.getOrDefault(i, 0) + 1));

        Number mode = null;
        int highest = 0;

        for (var entry : frequencies.entrySet()) {
            if (entry.getValue() > highest) {
                mode = entry.getKey();
                highest = entry.getValue();
            }
        }

        return mode;
    }

    /**
     * pasted from minecraft source code
     */
    public static float wrapAngleTo180_float(float value) {
        value = value % 360.0F;

        if (value >= 180.0F) {
            value -= 360.0F;
        }

        if (value < -180.0F) {
            value += 360.0F;
        }

        return value;
    }

}
