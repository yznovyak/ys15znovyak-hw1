package ua.yandex.shad.tempseries;

import java.lang.Math;
import java.util.InputMismatchException;

public class TemperatureSeriesAnalysis {
    public static final double MIN_TEMPERATURE = -273;

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

    public double average(){
        return summaryStatistics().getAvg();
    }

    public double deviation(){
        return summaryStatistics().getDev();
    }

    public double min(){
        return summaryStatistics().getMin();
    }

    public double max(){
        return summaryStatistics().getMax();
    }

    public double findTempClosestToZero(){
        return findTempClosestToValue(0.0);
    }

    public double findTempClosestToValue(double tempValue){
        if (size == 0)
            throw new IllegalArgumentException("temperature series is empty");

        double minDiff = Double.POSITIVE_INFINITY;
        double closest = Double.NaN;
        for (int i = 0; i < size; i++) {
            double diff = Math.abs(tempValue - series[i]);
            if (diff == minDiff && closest < tempValue && series[i] > tempValue) {
                closest = series[i];
            }
            if (diff < minDiff) {
                minDiff = diff;
                closest = series[i];
            }
        }

        return closest;
    }

    private interface TempPredicate {
        public boolean test(double currentTemp);
    }

    private double[] filter(TempPredicate shouldInclude) {
        int count = 0;
        for (int i = 0; i < size; i++)
            if (shouldInclude.test(series[i]))
                count++;

        double[] res = new double[count];
        int p = 0;
        for (int i = 0; i < size; i++)
            if (shouldInclude.test(series[i])) {
                res[p] = series[i];
                p++;
            }

        return res;
    }

    // TODO(yznovyak): rename Then -> Than.
    public double[] findTempsLessThen(final double tempValue){
        TempPredicate lessThanPredicate = new TempPredicate() {
            public boolean test(double currentTemp) {
                return currentTemp < tempValue;
            }
        };
        return filter(lessThanPredicate);
    }

    // TODO(yznovyak): rename Then -> Than.
    public double[] findTempsGreaterThen(final double tempValue){
        TempPredicate greaterThanPredicate = new TempPredicate() {
            public boolean test(double currentTemp) {
                return currentTemp > tempValue;
            }
        };
        return filter(greaterThanPredicate);
    }

    public TempSummaryStatistics summaryStatistics(){
        if (size == 0)
            throw new IllegalArgumentException("temperature series is empty");

        double sumTemp = 0;  // Sum of all temperatures.
        double sumTemp2 = 0;  // Sum of squares of all temperatures.
        double minTemp = Double.NEGATIVE_INFINITY;
        double maxTemp = Double.POSITIVE_INFINITY;

        for (int i = 0; i < size; i++) {
            double temp = series[i];
            sumTemp += temp;
            sumTemp2 += temp * temp;
            minTemp = Math.min(minTemp, temp);
            maxTemp = Math.max(maxTemp, temp);
        }

        double avgTemp = sumTemp / size;
        double varTemp = sumTemp2 / size - avgTemp * avgTemp;

        return new TempSummaryStatistics(avgTemp, varTemp, minTemp, maxTemp);
    }

    public int addTemps(double ... newTemps){
        // Verify newTemps
        for (double temp : newTemps)
            if (temp < MIN_TEMPERATURE)
                throw new InputMismatchException("Series can't contain temperature less than absolute zero.");

        int capacity = series.length;
        if (capacity == 0) {
            // Doubling series length doesn't make sense if initial
            // series has 0 capacity.  So we make it big enough to
            // hold exactly newTemps.
            capacity = newTemps.length;
        } else {
            // Doubling until enough.
            int needCapacity = size + newTemps.length;
            while (capacity < needCapacity)
                capacity *= 2;
        }

        if (capacity > series.length) {
            // Have to re-allocate series into bigger array.
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
