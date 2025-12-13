package softcomputing.fuzzy.Defuzzifiers;

import java.util.List;
import java.util.Map;

import softcomputing.fuzzy.utils.FuzzySet;
import softcomputing.fuzzy.utils.Point;


public class WeightedMeanDefuzzifier implements Defuzzifier {

    @Override
    public double defuzzify(FuzzySet fuzzySet) {
        Map<String, Double> membershipValues = fuzzySet.getMembershipValues();

        if (membershipValues == null || membershipValues.isEmpty()) {
            return 0.0;
        }

        List<Point> points = fuzzySet.getMembershipPoints();
        int n = membershipValues.size();

        if (points.isEmpty()) {
            return 0.0;
        }

        double numerator = 0.0;
        double denominator = 0.0;

        // Expectation: last n points correspond to (crispValue, membershipDegree)
        int start = Math.max(0, points.size() - n);
        for (int i = start; i < points.size(); i++) {
            Point p = points.get(i);
            double x = p.getX();
            double y = p.getY();
            numerator += x * y;
            denominator += y;
        }

        if (denominator == 0.0) {
            double sumx = 0.0;
            for (Point p : points) sumx += p.getX();
            return sumx / points.size();
        }

        return numerator / denominator;
    }
}
