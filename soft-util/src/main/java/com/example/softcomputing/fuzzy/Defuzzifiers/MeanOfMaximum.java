package com.example.softcomputing.fuzzy.Defuzzifiers;

import com.example.softcomputing.fuzzy.Defuzzifier;
import com.example.softcomputing.fuzzy.utils.FuzzySet;
import com.example.softcomputing.fuzzy.utils.Point;

import java.util.List;

public class MeanOfMaximum implements Defuzzifier {
    private final int samplingPoints;

    public MeanOfMaximum() {
        this.samplingPoints = 1000;
    }

    public MeanOfMaximum(int samplingPoints) {
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
        double maxMembership = 0.0;

        for (int i = 0; i <= samplingPoints; i++) {
            double x = minX + i * stepSize;
            double membershipValue = fuzzySet.getMembershipValue(x);
            if (membershipValue > maxMembership) {
                maxMembership = membershipValue;
            }
        }

        if (maxMembership == 0.0) {
            return (minX + maxX) / 2.0;
        }

        double sum = 0.0;
        int count = 0;

        for (int i = 0; i <= samplingPoints; i++) {
            double x = minX + i * stepSize;
            double membershipValue = fuzzySet.getMembershipValue(x);

            if (Math.abs(membershipValue - maxMembership) < 1e-10) {
                sum += x;
                count++;
            }
        }

        if (count == 0) {
            return (minX + maxX) / 2.0;
        }

        return sum / count;
    }
}

