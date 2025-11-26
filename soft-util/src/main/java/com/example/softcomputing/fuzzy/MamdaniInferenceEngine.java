package com.example.softcomputing.fuzzy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.softcomputing.fuzzy.operators.SNorm;
import com.example.softcomputing.fuzzy.operators.TNorm;

/**
 * Mamdani-style inference engine that supports pluggable T-Norm and S-Norm operators.
 */
public class MamdaniInferenceEngine implements InferenceEngineInterface {

    private List<FuzzyRule> rules;
    private TNorm tNorm;
    private SNorm sNorm;

    public MamdaniInferenceEngine(List<FuzzyRule> rules, TNorm tNorm, SNorm sNorm) {
        this.rules = rules;
        this.tNorm = tNorm;
        this.sNorm = sNorm;
    }

    @Override
    public Map<String, Map<String, Double>> infer(Map<String, Map<String, Double>> fuzzyInputs) {
        Map<String, Map<String, Double>> aggregatedOutput = new HashMap<>();

        for (FuzzyRule rule : rules) {
            Map<String, Map<String, Double>> ruleOutput = rule.apply(fuzzyInputs, tNorm);

            for (Map.Entry<String, Map<String, Double>> entry : ruleOutput.entrySet()) {
                String outputVariable = entry.getKey();
                Map<String, Double> linguisticValues = entry.getValue();

                aggregatedOutput.putIfAbsent(outputVariable, new HashMap<>());
                Map<String, Double> currentOutput = aggregatedOutput.get(outputVariable);

                for (Map.Entry<String, Double> lvEntry : linguisticValues.entrySet()) {
                    String linguisticValue = lvEntry.getKey();
                    Double firingStrength = lvEntry.getValue();

                    double existing = currentOutput.getOrDefault(linguisticValue, 0.0);
                    double combined = sNorm.apply(existing, firingStrength);
                    currentOutput.put(linguisticValue, combined);
                }
            }
        }

        return aggregatedOutput;
    }

    @Override
    public double inferAndDefuzzify(Map<String, Map<String, Double>> fuzzyInputs, Defuzzifier defuzzifier) {
        Map<String, Map<String, Double>> fuzzyOutput = infer(fuzzyInputs);
        // For simplicity, assume single output variable and delegate to defuzzifier
        if (fuzzyOutput.isEmpty()) return 0.0;

        // pick first output variable map for defuzzification
        Map.Entry<String, Map<String, Double>> first = fuzzyOutput.entrySet().iterator().next();
        // The Defuzzifier interface expects a FuzzySet; we need conversion. For now, if defuzzifier expects FuzzySet,
        // caller should wrap or provide appropriate implementation. Here we call defuzzifier with null to indicate
        // that actual integration depends on the project's FuzzySet implementation.
        // As a minimal bridge return 0.0 to indicate this should be handled by updating Defuzzifier implementation.
        return defuzzifier == null ? 0.0 : defuzzifier.defuzzify(null);
    }

    public List<FuzzyRule> getRules() {
        return rules;
    }

    public void setRules(List<FuzzyRule> rules) {
        this.rules = rules;
    }

    public TNorm gettNorm() {
        return tNorm;
    }

    public SNorm getsNorm() {
        return sNorm;
    }
}
