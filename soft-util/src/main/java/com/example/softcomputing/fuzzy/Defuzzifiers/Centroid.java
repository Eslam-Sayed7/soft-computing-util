package com.example.softcomputing.fuzzy.Defuzzifiers;

import java.util.List;

import com.example.softcomputing.fuzzy.utils.FuzzySet;
import com.example.softcomputing.fuzzy.utils.Point;

public class Centroid implements Defuzzifier {

    private final int samplingPoints;

    public Centroid() {
        this.samplingPoints = 1000;
    }

    public Centroid(int samplingPoints) {
        this.samplingPoints = samplingPoints;
    }

    @Override
    public double defuzzify(FuzzySet fuzzySet) {
        List<Point> points = fuzzySet.getMembershipPoints();

        if (points.isEmpty()) {
            throw new IllegalArgumentException("FuzzySet has no membership points");
        }

        double minX = fuzzySet.getExtremumX(FuzzySet.ExtremumType.MIN);
        double maxX = fuzzySet.getExtremumX(FuzzySet.ExtremumType.MAX);

        if (minX == maxX) {
            return minX;
        }

        double stepSize = (maxX - minX) / samplingPoints;
        double numerator = 0.0;
        double denominator = 0.0;

        for (int i = 0; i <= samplingPoints; i++) {
            double x = minX + i * stepSize;
            double membershipValue = fuzzySet.getMembershipValue(x);

            numerator += x * membershipValue;
            denominator += membershipValue;
        }

        if (denominator == 0.0) {
            return (minX + maxX) / 2.0;
        }

        return numerator / denominator;
    }
}
