package dev.isnow.foxac.util;

import lombok.Getter;

import java.util.List;

/**
 * @author Salers
 * made on dev.notonweed.ptsd.util
 */

@Getter

public class ClickingStats {

    private double entropy, std, kurtosis, variance, skewness, cps;
    private int outliers, duplicates;

    public ClickingStats(final List<Integer> delays) {
        entropy = MathUtil.getEntropy(delays);
        std = MathUtil.getStandardDeviation(delays);
        kurtosis = MathUtil.getKurtosis(delays);
        variance = MathUtil.getVariance(delays);
        skewness = MathUtil.getSkewness(delays);
        cps = MathUtil.getCps(delays);
        outliers = MathUtil.getOutliers(delays).getKey().size() + MathUtil.getOutliers(delays).getValue().size();
        duplicates = MathUtil.getDuplicates(delays);

    }
}
