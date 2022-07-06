package dev.isnow.foxac.util;

import dev.isnow.foxac.util.mc.MathHelper;
import dev.isnow.foxac.util.mc.Vec3;
import lombok.experimental.UtilityClass;
import lombok.var;
import org.bukkit.util.NumberConversions;

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

    public double hypot(final double x, final double z) {
        return Math.sqrt(x * x + z * z);
    }

    /**
     * pasted from minecraft source code
     */
    public Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3((double) (f1 * f2), (double) f3, (double) (f * f2));
    }


    public int getPingTicks(final long ping, final int extraTicks) {
        return NumberConversions.floor(ping / 50.0D) + extraTicks;
    }
}
