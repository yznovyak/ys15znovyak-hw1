package ua.yandex.shad.tempseries;

import java.util.InputMismatchException;
import org.junit.Test;

import static org.junit.Assert.*;

public class TemperatureSeriesAnalysisTest {
    // All assertion of doubles will be done using this value.
    private static double EPSILON = 1e-9;

    // Tests for average().

    @Test(expected=IllegalArgumentException.class)
    public void testAverage_FailOnEmptySeries() {
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis();
        seriesAnalysis.average();  // Should fail.
    }

    @Test
    public void testAverage_CorrectOnSingleElementSeq() {
        double[] temperatureSeries = {1.5};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);
        double expResult = 1.5;
        double actualResult = seriesAnalysis.average();

        assertEquals(expResult, actualResult, EPSILON);
    }

    @Test
    public void testAverage_CorrectOnNonTrivialSeq() {
        double[] temperatureSeries = {1.0, 2.0, 3.0, 4.0};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);
        double expResult = 2.5;
        double actualResult = seriesAnalysis.average();

        assertEquals(expResult, actualResult, EPSILON);
    }

    // Tests for deviation().

    @Test(expected=IllegalArgumentException.class)
    public void testDeviation_FailOnEmptySeries() {
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis();
        seriesAnalysis.deviation();  // Should fail.
    }

    @Test
    public void testDeviation_ZeroOnSingleElementSeq() {
        double[] temperatureSeries = {1.5};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);
        double expResult = 0.0;
        double actualResult = seriesAnalysis.deviation();

        assertEquals(expResult, actualResult, EPSILON);
    }

    @Test
    public void testDeviation_CorrectOnNonTrivialSeq() {
        double[] temperatureSeries = {1, 2, 3, 4, 5, 6, 7};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);

        // Sequence: 1, 2, 3, 4, 5, 6, 7
        // Count: 7
        // Mean: 4
        // Differences: -3, -2, -1, 0, 1, 2, 3
        // Differences^2: 9, 4, 1, 0, 1, 4, 9
        // Sum of Differences^2: 28
        // Variance (Sum of Differences2 / Count): 4
        // Standard Deviation (The square root of the Variance): 2
        double expResult = 2;
        double actualResult = seriesAnalysis.deviation();

        assertEquals(expResult, actualResult, EPSILON);
    }

    // Tests for min().

    @Test(expected=IllegalArgumentException.class)
    public void testMin_FailOnEmptySeries() {
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis();
        seriesAnalysis.min();  // Should fail.
    }


    @Test
    public void testMin_CorrectOnSingleElementSeq() {
        double[] temperatureSeries = {1.5};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);
        double expResult = 1.5;
        double actualResult = seriesAnalysis.min();

        assertEquals(expResult, actualResult, EPSILON);
    }

    @Test
    public void testMin_CorrectOnNonTrivialSeq() {
        double[] temperatureSeries = {4, 7, 5, 1, 3, 6, 2};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);

        double expResult = 1;
        double actualResult = seriesAnalysis.min();

        assertEquals(expResult, actualResult, EPSILON);
    }

    // Tests for max().

    @Test(expected=IllegalArgumentException.class)
    public void testMax_FailOnEmptySeries() {
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis();
        seriesAnalysis.max();  // Should fail.
    }

    @Test
    public void testMax_CorrectOnSingleElementSeq() {
        double[] temperatureSeries = {1.5};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);
        double expResult = 1.5;
        double actualResult = seriesAnalysis.max();

        assertEquals(expResult, actualResult, EPSILON);
    }

    @Test
    public void testMax_CorrectOnNonTrivialSeq() {
        double[] temperatureSeries = {4, 7, 5, 1, 3, 6, 2};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);

        double expResult = 7;
        double actualResult = seriesAnalysis.max();

        assertEquals(expResult, actualResult, EPSILON);
    }

    // Tests for summaryStatistics().

    @Test(expected=IllegalArgumentException.class)
    public void testSummaryStatistics_FailOnEmptySeries() {
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis();
        seriesAnalysis.summaryStatistics();
    }

    @Test
    public void testSummaryStatistics_CorrectOnSingleElementSeq() {
        double[] temperatureSeries = {1.5};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);

        TempSummaryStatistics actual = seriesAnalysis.summaryStatistics();

        assertEquals(1.5, actual.getAvg(), EPSILON);
        assertEquals(0.0, actual.getDev(), EPSILON);
        assertEquals(1.5, actual.getMin(), EPSILON);
        assertEquals(1.5, actual.getMax(), EPSILON);
    }

    @Test
    public void testSummaryStatistics_CorrectOnNonTrivialSeq() {
        double[] temperatureSeries = {4, 7, 5, 1, 3, 6, 2};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);

        TempSummaryStatistics actual = seriesAnalysis.summaryStatistics();

        assertEquals(4.0, actual.getAvg(), EPSILON);
        assertEquals(2.0, actual.getDev(), EPSILON);
        assertEquals(1.0, actual.getMin(), EPSILON);
        assertEquals(7.0, actual.getMax(), EPSILON);
    }

    // Tests for findTempClosestToZero().

    @Test(expected=IllegalArgumentException.class)
    public void testFindTempClosestToZero_FailOnEmptySeries() {
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis();
        seriesAnalysis.findTempClosestToZero();  // Should fail.
    }

    @Test
    public void testFindTempClosestToZero_OneElement() {
        double[] temperatureSeries = {5};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);

        double expResult = 5;
        double actualResult = seriesAnalysis.findTempClosestToZero();

        assertEquals(expResult, actualResult, EPSILON);
    }

    @Test
    public void testFindTempClosestToZero_ClosestValueIsPreferred() {
        double[] temperatureSeries = {-4, 10, 5};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);

        double expResult = -4;
        double actualResult = seriesAnalysis.findTempClosestToZero();

        assertEquals(expResult, actualResult, EPSILON);
    }

    @Test
    public void testFindTempClosestToZero_PositiveIsChosenOverNegative() {
        double[] temperatureSeries = {-5, 10, 5};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);

        double expResult = 5;
        double actualResult = seriesAnalysis.findTempClosestToZero();

        assertEquals(expResult, actualResult, EPSILON);
    }

    @Test
    public void testFindTempClosestToZero_NegativeDoesNotOverridePositive() {
        double[] temperatureSeries = {5, 10, 7, -5};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);

        double expResult = 5;
        double actualResult = seriesAnalysis.findTempClosestToZero();

        assertEquals(expResult, actualResult, EPSILON);
    }

    // Tests for findTempClosestToValue().

    @Test(expected=IllegalArgumentException.class)
    public void testFindTempClosestToValue_FailOnEmptySeries() {
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis();
        seriesAnalysis.findTempClosestToValue(2.0);  // Should fail.
    }

    @Test
    public void testFindTempClosestToValue_OneElement() {
        double[] temperatureSeries = {5};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);

        double expResult = 5;
        double actualResult = seriesAnalysis.findTempClosestToValue(2.0);

        assertEquals(expResult, actualResult, EPSILON);
    }

    @Test
    public void testFindTempClosestToValue_ClosestValueIsPreferred() {
        double[] temperatureSeries = {-4, 10, 5};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);

        double expResult = 5;
        double actualResult = seriesAnalysis.findTempClosestToValue(1);

        assertEquals(expResult, actualResult, EPSILON);
    }

    @Test
    public void testFindTempClosestToValue_PositiveIsChosenOverNegative() {
        double[] temperatureSeries = {-3, 12, 7};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);

        double expResult = 7;
        double actualResult = seriesAnalysis.findTempClosestToValue(2);

        assertEquals(expResult, actualResult, EPSILON);
    }

    @Test
    public void testFindTempClosestToValue_NegativeDoesNotOverridePositive() {
        double[] temperatureSeries = {7, 12, 9, -3};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);

        double expResult = 7;
        double actualResult = seriesAnalysis.findTempClosestToValue(2);

        assertEquals(expResult, actualResult, EPSILON);
    }

    // Tests for addTemps().

    @Test(expected=IllegalArgumentException.class)
    public void testAddTemps_EmptyListDoesNotAddAnything() {
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis();
        seriesAnalysis.addTemps();

        seriesAnalysis.findTempClosestToValue(2.0);  // Should fail.
    }

    public void testAddTemps_DoNotFailOnAbsoluteZero() {
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis();

        final double minAllowedTemperature = -273.0;
        double[] temperatureSeries = {10, minAllowedTemperature, 20};
        seriesAnalysis.addTemps(temperatureSeries);

        assertEquals(minAllowedTemperature, seriesAnalysis.min(), EPSILON);
    }

    @Test(expected=InputMismatchException.class)
    public void testAddTemps_FailOnBelowAbsoluteZero() {
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis();

        final double maxNotAllowedTemperature = -273.1;
        double[] temperatureSeries = {10, maxNotAllowedTemperature, 20};
        seriesAnalysis.addTemps(temperatureSeries);
    }

    @Test
    public void testAddTemps_ReturnsCorrectSize() {
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis();

        double[] temperatureSeries = {10, 20};
        assertEquals(2, seriesAnalysis.addTemps(temperatureSeries));
        assertEquals(4, seriesAnalysis.addTemps(temperatureSeries));
        assertEquals(6, seriesAnalysis.addTemps(temperatureSeries));
    }

    // Tests for findTempsLessThan().

    @Test(expected=IllegalArgumentException.class)
    public void testFindTempsLessThan_FailOnEmptyList() {
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis();
        seriesAnalysis.findTempsLessThan(10.0);
    }

    @Test
    public void testFindTempsLessThan_CorrectOnNonTrivialArray() {
        double[] temperatureSeries = {4, 7, 5, 1, 3, 6, 2};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);

        double[] actualTemps = seriesAnalysis.findTempsLessThan(4.0);
        double[] expectedTemps = {1, 2, 3};

        // Sort actual and expected temps and compare them.
        assertEquals(expectedTemps.length, actualTemps.length);
        if (expectedTemps.length == actualTemps.length) {
            java.util.Arrays.sort(actualTemps);
            java.util.Arrays.sort(expectedTemps);
            for (int i = 0; i < expectedTemps.length; i++) {
                assertEquals(expectedTemps[i], actualTemps[i], EPSILON);
            }
        }
    }

    // Tests for findTempsGreaterThan().

    @Test(expected=IllegalArgumentException.class)
    public void testFindTempsGreaterThan_FailOnEmptyList() {
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis();
        seriesAnalysis.findTempsGreaterThan(10.0);
    }

    @Test
    public void testFindTempsGreaterThan_CorrectOnNonTrivialArray() {
        double[] temperatureSeries = {4, 7, 5, 1, 3, 6, 2};
        TemperatureSeriesAnalysis seriesAnalysis =
            new TemperatureSeriesAnalysis(temperatureSeries);

        double[] actualTemps = seriesAnalysis.findTempsGreaterThan(4.0);
        double[] expectedTemps = {5, 6, 7};

        // Sort actual and expected temps and compare them.
        assertEquals(expectedTemps.length, actualTemps.length);
        if (expectedTemps.length == actualTemps.length) {
            java.util.Arrays.sort(actualTemps);
            java.util.Arrays.sort(expectedTemps);
            for (int i = 0; i < expectedTemps.length; i++) {
                assertEquals(expectedTemps[i], actualTemps[i], EPSILON);
            }
        }
    }
}
