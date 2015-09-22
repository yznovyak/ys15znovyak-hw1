package ua.yandex.shad.tempseries;

public class TempSummaryStatistics {
    private double avg;
    private double dev;
    private double min;
    private double max;

    public TempSummaryStatistics(double avg, double dev, double min, double max) {
        this.avg = avg;
        this.dev = dev;
        this.min = min;
        this.max = max;
    }

    public double getAvg() { return avg; }
    public double getDev() { return dev; }
    public double getMin() { return min; }
    public double getMax() { return max; }
}
