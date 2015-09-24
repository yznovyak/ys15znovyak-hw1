package ua.yandex.shad.tempseries;

import java.util.InputMismatchException;

public class TemperatureSeriesAnalysis {
    public static final double MIN_TEMPERATURE = -273;

    // Doubles that differ by this much are considered equal.
    private static final double EPSILON = 1e-9;

    private int size;
    private double[] series;

    public TemperatureSeriesAnalysis() {
        this.series = new double[0];
        this.size = 0;
    }

    public TemperatureSeriesAnalysis(double[] series) {
        this();
        addTemps(series);
    }

    public double average() {
        return summaryStatistics().getAvg();
    }

    public double deviation() {
        return summaryStatistics().getDev();
    }

    public double min() {
        return summaryStatistics().getMin();
    }

    public double max() {
        return summaryStatistics().getMax();
    }

    public double findTempClosestToZero() {
        return findTempClosestToValue(0.0);
    }

    public double findTempClosestToValue(double tempValue) {
        if (size == 0) {
            throw new IllegalArgumentException("temperature series is empty");
        }

        double minDiff = Double.POSITIVE_INFINITY;
        double closest = Double.NaN;
        for (int i = 0; i < size; i++) {
            double current = series[i];
            double diff = Math.abs(tempValue - current);
            if (Math.abs(diff - minDiff) < EPSILON
                && closest < tempValue && current > tempValue) {
                closest = current;
            }
            if (diff < minDiff) {
                minDiff = diff;
                closest = current;
            }
        }

        return closest;
    }

    // filter returns all elements of series that are strictly between
    // lowerBound and upperBound.
    private double[] filter(double lowerBound, double upperBound) {
        if (size == 0) {
            throw new IllegalArgumentException("temperature series is empty");
        }

        int count = 0;
        for (int i = 0; i < size; i++) {
            if (lowerBound < series[i] && series[i] < upperBound) {
                count++;
            }
        }

        double[] res = new double[count];
        int p = 0;
        for (int i = 0; i < size; i++) {
            if (lowerBound < series[i] && series[i] < upperBound) {
                res[p] = series[i];
                p++;
            }
        }

        return res;
    }

    public double[] findTempsLessThan(double tempValue) {
        return filter(Double.NEGATIVE_INFINITY, tempValue);
    }

    public double[] findTempsGreaterThan(double tempValue) {
        return filter(tempValue, Double.POSITIVE_INFINITY);
    }

    public TempSummaryStatistics summaryStatistics() {
        if (size == 0) {
            throw new IllegalArgumentException("temperature series is empty");
        }

        double sumTemp = 0;  // Sum of all temperatures.
        double sumTempSqr = 0;  // Sum of squares of all temperatures.
        double minTemp = Double.POSITIVE_INFINITY;
        double maxTemp = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < size; i++) {
            double temp = series[i];
            sumTemp += temp;
            sumTempSqr += temp * temp;
            minTemp = Math.min(minTemp, temp);
            maxTemp = Math.max(maxTemp, temp);
        }

        double avgTemp = sumTemp / size;
        double varTemp = sumTempSqr / size - avgTemp * avgTemp;  // Variance
        double stddevTemp = Math.sqrt(varTemp);  // Std. Deviation

        return new TempSummaryStatistics(avgTemp, stddevTemp,
                                         minTemp, maxTemp);
    }

    public int addTemps(double ... newTemps) {
        // Verify that all temps in newTemps aren't below MIN_TEMPERATURE.
        for (double temp : newTemps) {
            if (temp < MIN_TEMPERATURE) {
                throw new InputMismatchException("Series can't contain "
                                                 + "temperature less than "
                                                 + "absolute zero.");
            }
        }

        int capacity = series.length;
        if (capacity == 0) {
            // Doubling series length doesn't make sense if initial
            // series has 0 capacity.  So we make it big enough to
            // hold exactly newTemps.
            capacity = newTemps.length;
        } else {
            // Doubling capacity until enough to store new elements.
            int needCapacity = size + newTemps.length;
            while (capacity < needCapacity) {
                capacity *= 2;
            }
        }

        if (capacity > series.length) {
            // Existing capacity is not enough to contain new items,
            // so re-allocate series into a bigger array and copy
            // existing and new elements there.
            double[] newSeries = new double[capacity];
            System.arraycopy(series, 0, newSeries, 0, size);
            series = newSeries;
        }

        // Add new temps to the end of current series.
        System.arraycopy(newTemps, 0, series, size, newTemps.length);
        size += newTemps.length;

        return size;
    }
}
