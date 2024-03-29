package dev.isnow.foxac.util;

import com.google.common.collect.Lists;
import dev.isnow.foxac.util.mc.MathHelper;
import dev.isnow.foxac.util.mc.Vec3;
import lombok.experimental.UtilityClass;
import lombok.var;
import org.bukkit.util.NumberConversions;

import java.util.*;

import static com.google.common.math.DoubleMath.log2;

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
     * @param data - The sample of clicks you want to get the cps from
     * @return - The cps using the average as a method of calculation
     */
    public double getCps(final Collection<? extends Number> data) {
        final double average = data.stream().mapToDouble(Number::doubleValue).average().orElse(0.0);

        return 20 / average;
    }

    /**
     * @param data - The set of data you want to find the variance from
     * @return - The variance of the numbers.
     * @See - https://en.wikipedia.org/wiki/Variance
     */
    public double getVariance(final Collection<? extends Number> data) {
        int count = 0;

        double sum = 0.0;
        double variance = 0.0;

        double average;

        // Increase the sum and the count to find the average and the standard deviation
        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;
        }

        average = sum / count;

        // Run the standard deviation formula
        for (final Number number : data) {
            variance += Math.pow(number.doubleValue() - average, 2.0);
        }

        return variance;
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



    public static double getEntropy(Collection<? extends Number> values) {
        double n = values.size();

        if (n < 2)
            return Double.NaN;

        Map<Integer, Integer> map = new HashMap<>();

        values.stream()
                .mapToInt(Number::intValue)
                .forEach(value -> map.put(value, map.computeIfAbsent(value, k -> 0) + 1));

        double entropy = map.values().stream()
                .mapToDouble(freq -> (double) freq / n)
                .map(probability -> probability * log2(probability))
                .sum();

        return -entropy;
    }

    /**
     * @param data - The set of numbers / data you want to find the standard deviation from.
     * @return - The standard deviation using the square root of the variance.
     * @See - https://en.wikipedia.org/wiki/Standard_deviation
     * @See - https://en.wikipedia.org/wiki/Variance
     */
    public double getStandardDeviation(final Collection<? extends Number> data) {
        final double variance = getVariance(data);

        // The standard deviation is the square root of variance. (sqrt(s^2))
        return Math.sqrt(variance);
    }

    /**
     * @param data - The set of numbers / data you want to find the skewness from
     * @return - The skewness running the standard skewness formula.
     * @See - https://en.wikipedia.org/wiki/Skewness
     */
    public double getSkewness(final Collection<? extends Number> data) {
        double sum = 0;
        int count = 0;

        final List<Double> numbers = Lists.newArrayList();

        // Get the sum of all the data and the amount via looping
        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;

            numbers.add(number.doubleValue());
        }

        // Sort the numbers to run the calculations in the next part
        Collections.sort(numbers);

        // Run the formula to get skewness
        final double mean = sum / count;
        final double median = (count % 2 != 0) ? numbers.get(count / 2) : (numbers.get((count - 1) / 2) + numbers.get(count / 2)) / 2;
        final double variance = getVariance(data);

        return 3 * (mean - median) / variance;
    }

    public static double magnitude(final double... points) {
        double sum = 0.0;

        for (final double point : points) {
            sum += point * point;
        }

        return Math.sqrt(sum);
    }

    public static int getDistinct(final Collection<? extends Number> collection) {
        Set<Object> set = new HashSet<>(collection);
        return set.size();
    }

    /**
     * @param - collection The collection of the numbers you want to get the duplicates from
     * @return - The duplicate amount
     */
    public static int getDuplicates(final Collection<? extends Number> collection) {
        return collection.size() - getDistinct(collection);
    }

    /**
     * @param - The collection of numbers you want analyze
     * @return - A pair of the high and low outliers
     * @See - https://en.wikipedia.org/wiki/Outlier
     */
    public Pair<List<Double>, List<Double>> getOutliers(final Collection<? extends Number> collection) {
        final List<Double> values = new ArrayList<>();

        for (final Number number : collection) {
            values.add(number.doubleValue());
        }

        final double q1 = getMedian(values.subList(0, values.size() / 2));
        final double q3 = getMedian(values.subList(values.size() / 2, values.size()));

        final double iqr = Math.abs(q1 - q3);
        final double lowThreshold = q1 - 1.5 * iqr, highThreshold = q3 + 1.5 * iqr;

        final Pair<List<Double>, List<Double>> tuple = new Pair<>(new ArrayList<>(), new ArrayList<>());

        for (final Double value : values) {
            if (value < lowThreshold) {
                tuple.getKey().add(value);
            } else if (value > highThreshold) {
                tuple.getValue().add(value);
            }
        }

        return tuple;
    }

    /**
     * @param data - The set of numbers/data you want to get the kurtosis from
     * @return - The kurtosis using the standard kurtosis formula
     * @See - https://en.wikipedia.org/wiki/Kurtosis
     */
    public double getKurtosis(final Collection<? extends Number> data) {
        double sum = 0.0;
        int count = 0;

        for (Number number : data) {
            sum += number.doubleValue();
            ++count;
        }

        if (count < 3.0) {
            return 0.0;
        }

        final double efficiencyFirst = count * (count + 1.0) / ((count - 1.0) * (count - 2.0) * (count - 3.0));
        final double efficiencySecond = 3.0 * Math.pow(count - 1.0, 2.0) / ((count - 2.0) * (count - 3.0));
        final double average = sum / count;

        double variance = 0.0;
        double varianceSquared = 0.0;

        for (final Number number : data) {
            variance += Math.pow(average - number.doubleValue(), 2.0);
            varianceSquared += Math.pow(average - number.doubleValue(), 4.0);
        }

        return efficiencyFirst * (varianceSquared / Math.pow(variance / sum, 2.0)) - efficiencySecond;
    }

    /**
     * @param data - The data you want the median from
     * @return - The middle number of that data
     * @See - https://en.wikipedia.org/wiki/Median
     */
    private double getMedian(final List<Double> data) {
        if (data.size() % 2 == 0) {
            return (data.get(data.size() / 2) + data.get(data.size() / 2 - 1)) / 2;
        } else {
            return data.get(data.size() / 2);
        }
    }




    public int getPingTicks(final long ping, final int extraTicks) {
        return NumberConversions.floor(ping / 50.0D) + extraTicks;
    }


