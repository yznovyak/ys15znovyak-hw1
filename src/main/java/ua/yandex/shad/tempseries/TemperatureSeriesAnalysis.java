package ua.yandex.shad.tempseries;

import java.util.function.Predicate;
import java.lang.Math;

public class TemperatureSeriesAnalysis {
    private int size;
    private double[] series;

    public TemperatureSeriesAnalysis() {
        this.series = new double[0];
        this.size = 0;
    }

    public TemperatureSeriesAnalysis(double[] series) {
        this.series = series.clone();
        this.size = this.series.length;
    }

    private void validateInternalState() {
        if (size == 0)
            throw new IllegalArgumentException("temperature series is empty");
    }

    public double average(){
        validateInternalState();

        double sum = 0;
        for (int i = 0; i < size; i++)
            sum += series[i];
        return sum;
    }

    public double deviation(){
        validateInternalState();

        double avg = average();
        double sum2 = 0;
        for (double temp : this.series)
            sum2 += (temp - avg)*(temp - avg);
        return Math.sqrt(sum2 / this.size);
    }

    public double min(){
        validateInternalState();

        double res = 0;
        for (int i = 0; i < size; i++)
            res = Math.min(res, series[i]);
        return res;
    }

    public double max(){
        validateInternalState();

        double res = 0;
        for (int i = 0; i < size; i++)
            res = Math.max(res, series[i]);
        return res;
    }

    public double findTempClosestToZero(){
        return findTempClosestToValue(0.0);
    }

    public double findTempClosestToValue(double tempValue){
        validateInternalState();

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

    private double[] filter(Predicate<Double> shouldInclude) {
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
    public double[] findTempsLessThen(double tempValue){
        int count = 0;
        for (int i = 0; i < size; i++)
            if (series[i] < tempValue)
                count++;

        double[] res = new double[count];
        int p = 0;
        for (int i = 0; i < size; i++)
            if (series[i] < tempValue) {
                res[p] = series[i];
                p++;
            }

        return res;
    }

    // TODO(yznovyak): rename Then -> Than.
    public double[] findTempsGreaterThen(double tempValue){
        return filter(temp -> temp > tempValue);
    }

    public TempSummaryStatistics summaryStatistics(){
        validateInternalState();

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

    public int addTemps(double ... temps){
        return 0;
    }
}
