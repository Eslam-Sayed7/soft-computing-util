package com.example.softcomputing.fuzzy.utils;

import java.util.HashMap;
import java.util.Map;

import com.example.softcomputing.fuzzy.MembershipFunction;
import com.example.softcomputing.fuzzy.membershipFuns.Triangular;

/**
 * Converts normalized crisp inputs into linguistic membership maps for the recommender.
 */
public class RecommendationFuzzifier {

    private final Map<String, MembershipFunction> lowMF = new HashMap<>();
    private final Map<String, MembershipFunction> medMF = new HashMap<>();
    private final Map<String, MembershipFunction> highMF = new HashMap<>();

    public RecommendationFuzzifier() {
        // All inputs are expected normalized to [0,1]. Use same triangular shapes for each variable.
        // Low: peak towards 0..0.25, Medium centered at 0.5, High towards 0.75..1
        // We'll build MFs on the fly for commonly used variables
    }

    private void ensureMFs(String var) {
        if (lowMF.containsKey(var)) return;
        lowMF.put(var, new Triangular(new Point(0.0, 1.0), new Point(0.25, 1.0), new Point(0.5, 0.0)));
        medMF.put(var, new Triangular(new Point(0.25, 0.0), new Point(0.5, 1.0), new Point(0.75, 0.0)));
        highMF.put(var, new Triangular(new Point(0.5, 0.0), new Point(0.75, 1.0), new Point(1.0, 1.0)));
    }

    /**
     * Build fuzzy input map: variable -> (label->degree)
     * Expects crisp inputs normalized to [0,1]
     */
    public Map<String, Map<String, Double>> fuzzify(Map<String, Double> crispInputs) {
        Map<String, Map<String, Double>> fuzzy = new HashMap<>();

        for (Map.Entry<String, Double> e : crispInputs.entrySet()) {
            String var = e.getKey();
            double value = e.getValue();
            ensureMFs(var);

            Map<String, Double> lv = new HashMap<>();
            lv.put("Low", clamp(lowMF.get(var).apply(value)));
            lv.put("Medium", clamp(medMF.get(var).apply(value)));
            lv.put("High", clamp(highMF.get(var).apply(value)));

            fuzzy.put(var, lv);
        }

        return fuzzy;
    }

    private double clamp(double v) {
        if (Double.isNaN(v)) return 0.0;
        if (v < 0.0) return 0.0;
        if (v > 1.0) return 1.0;
        return v;
    }
}
