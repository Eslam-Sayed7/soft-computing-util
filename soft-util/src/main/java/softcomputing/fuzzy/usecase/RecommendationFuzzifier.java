package softcomputing.fuzzy.usecase;

import java.util.HashMap;
import java.util.Map;

/**
 * Lightweight fuzzifier for the recommendation example.
 * Produces simple High/Med/Low memberships for numeric inputs and
 * boolean-style membership for interest/categoryMatch.
 */
public class RecommendationFuzzifier {

    public Map<String, Map<String, Double>> fuzzify(Map<String, Double> crisp) {
        Map<String, Map<String, Double>> out = new HashMap<>();

        // interest: treated as binary 0 or 1
        double interest = crisp.getOrDefault("interest", 0.0);
        Map<String, Double> interestMap = new HashMap<>();
        if (interest >= 0.9) {
            interestMap.put("High", 1.0);
            interestMap.put("Medium", 0.0);
            interestMap.put("Low", 0.0);
        } else if (interest > 0.0) {
            interestMap.put("High", 0.5);
            interestMap.put("Medium", 0.5);
            interestMap.put("Low", 0.0);
        } else {
            interestMap.put("High", 0.0);
            interestMap.put("Medium", 0.0);
            interestMap.put("Low", 1.0);
        }
        out.put("interest", interestMap);

        // categoryMatch: similar to interest (0..1)
        double cat = crisp.getOrDefault("categoryMatch", 0.0);
        Map<String, Double> catMap = new HashMap<>();
        if (cat >= 0.9) {
            catMap.put("High", 1.0);
            catMap.put("Medium", 0.0);
            catMap.put("Low", 0.0);
        } else if (cat > 0.0) {
            catMap.put("High", 0.5);
            catMap.put("Medium", 0.5);
            catMap.put("Low", 0.0);
        } else {
            catMap.put("High", 0.0);
            catMap.put("Medium", 0.0);
            catMap.put("Low", 1.0);
        }
        out.put("categoryMatch", catMap);

        // budget, rating, popularity: numeric in [0..1] -> Low/Medium/High
        fuzzifyTri(crisp.getOrDefault("budget", 0.0), out, "budget");
        fuzzifyTri(crisp.getOrDefault("rating", 0.0), out, "rating");
        fuzzifyTri(crisp.getOrDefault("popularity", 0.0), out, "popularity");

        return out;
    }

    private void fuzzifyTri(double x, Map<String, Map<String, Double>> out, String name) {
        Map<String, Double> map = new HashMap<>();

        // Low: 1 at 0, 0 at 0.5
        double low = clamp(1.0 - (x / 0.5));
        // High: 0 at 0.5, 1 at 1.0
        double high = clamp((x - 0.5) / 0.5);
        // Medium: triangular peak at 0.5
        double med = clamp(1.0 - Math.abs(x - 0.5) / 0.5);

        map.put("Low", low);
        map.put("Medium", med);
        map.put("High", high);

        out.put(name, map);
    }

    private double clamp(double v) {
        if (Double.isNaN(v) || v <= 0.0) return 0.0;
        if (v >= 1.0) return 1.0;
        return v;
    }
}
