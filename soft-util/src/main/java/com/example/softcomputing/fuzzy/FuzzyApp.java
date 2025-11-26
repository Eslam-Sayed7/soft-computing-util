package com.example.softcomputing.fuzzy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.softcomputing.fuzzy.Defuzzifiers.Centroid;
import com.example.softcomputing.fuzzy.operators.MaxSNorm;
import com.example.softcomputing.fuzzy.operators.MinTNorm;

/**
 * Small demonstration application that builds a FuzzyController using the
 * builder / fluent API and runs both a Mamdani and a Sugeno scenario.
 */
public class FuzzyApp {

    public static void main(String[] args) {
        System.out.println("=== FuzzyApp Demo ===");

        // Prepare sample fuzzified inputs (simulating a Fuzzifier output)
        Map<String, Map<String, Double>> fuzzyInputs = sampleFuzzyInputs();

        // --- Mamdani example ------------------------------------------------
        List<FuzzyRule> mamdaniRules = createMamdaniRules();
        MamdaniInferenceEngine mamdaniEngine = new MamdaniInferenceEngine(mamdaniRules, new MinTNorm(), new MaxSNorm());

        FuzzyController mamdaniController = FuzzyController.builder()
                .withInferenceEngine(mamdaniEngine)
                .withDefuzzifier(new Centroid())
                .build();

        double mamdaniCrisp = mamdaniController.run(fuzzyInputs);
        System.out.println("Mamdani -> defuzzified output: " + mamdaniCrisp);

        // --- Sugeno example -------------------------------------------------
        List<SugenoRule> sugenoRules = createSugenoRules();
        SugenoInferenceEngine sugenoEngine = new SugenoInferenceEngine(sugenoRules, new MinTNorm());

        FuzzyController sugenoController = FuzzyController.builder()
                .withInferenceEngine(sugenoEngine)
                .build();

        // Sugeno returns a crisp result via inferAndDefuzzify
        double sugenoCrisp = sugenoController.getInferenceEngine().inferAndDefuzzify(fuzzyInputs, new Centroid());
        System.out.println("Sugeno -> inferred crisp output: " + sugenoCrisp);

        System.out.println("=== Demo finished ===");
    }

    private static Map<String, Map<String, Double>> sampleFuzzyInputs() {
        Map<String, Map<String, Double>> fuzzyInputs = new HashMap<>();

        Map<String, Double> interest = new HashMap<>();
        interest.put("Low", 0.0);
        interest.put("Medium", 0.6);
        interest.put("High", 0.4);
        fuzzyInputs.put("Interest", interest);

        Map<String, Double> budget = new HashMap<>();
        budget.put("Low", 0.0);
        budget.put("Medium", 0.8);
        budget.put("High", 0.2);
        fuzzyInputs.put("Budget", budget);

        return fuzzyInputs;
    }

    private static List<FuzzyRule> createMamdaniRules() {
        List<FuzzyRule> rules = new ArrayList<>();

        Map<String, String> a1 = new HashMap<>();
        a1.put("Interest", "High");
        a1.put("Budget", "High");
        Map<String, String> c1 = new HashMap<>();
        c1.put("Recommendation", "StronglyRecommend");
        rules.add(new FuzzyRule("R1", a1, c1));

        Map<String, String> a2 = new HashMap<>();
        a2.put("Interest", "High");
        a2.put("Budget", "Medium");
        Map<String, String> c2 = new HashMap<>();
        c2.put("Recommendation", "Recommend");
        rules.add(new FuzzyRule("R2", a2, c2));

        Map<String, String> a3 = new HashMap<>();
        a3.put("Interest", "Medium");
        a3.put("Budget", "Medium");
        Map<String, String> c3 = new HashMap<>();
        c3.put("Recommendation", "Consider");
        rules.add(new FuzzyRule("R3", a3, c3));

        return rules;
    }

    private static List<SugenoRule> createSugenoRules() {
        List<SugenoRule> rules = new ArrayList<>();

        Map<String, String> a1 = new HashMap<>();
        a1.put("Interest", "High");
        a1.put("Budget", "High");
        rules.add(new SugenoRule("S1", a1, 1.0));

        Map<String, String> a2 = new HashMap<>();
        a2.put("Interest", "High");
        a2.put("Budget", "Medium");
        rules.add(new SugenoRule("S2", a2, 0.8));

        Map<String, String> a3 = new HashMap<>();
        a3.put("Interest", "Medium");
        a3.put("Budget", "Medium");
        rules.add(new SugenoRule("S3", a3, 0.5));

        return rules;
    }
}
