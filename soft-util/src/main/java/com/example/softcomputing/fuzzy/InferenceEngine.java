package com.example.softcomputing.fuzzy;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class InferenceEngine {

    private List<FuzzyRule> rules;

    public InferenceEngine(List<FuzzyRule> rules) {
        this.rules = rules;
    }

    /**
     * Performs inference by evaluating all rules and aggregating results using MAX operator
     * @param fuzzyInputs - Map of variable name to Map of linguistic value to membership degree
     *                      e.g., {"Interest": {"Low": 0.0, "Medium": 0.6, "High": 0.4},
     *                             "Budget": {"Low": 0.0, "Medium": 0.8, "High": 0.2}}
     * @return Aggregated fuzzy output
     */
    public Map<String, Map<String, Double>> infer(Map<String, Map<String, Double>> fuzzyInputs) {
        Map<String, Map<String, Double>> aggregatedOutput = new HashMap<>();

        // Evaluate each rule
        for (FuzzyRule rule : rules) {
            Map<String, Map<String, Double>> ruleOutput = rule.apply(fuzzyInputs);

            // Aggregate using MAX operator (union)
            for (Map.Entry<String, Map<String, Double>> entry : ruleOutput.entrySet()) {
                String outputVariable = entry.getKey();
                Map<String, Double> linguisticValues = entry.getValue();

                if (!aggregatedOutput.containsKey(outputVariable)) {
                    aggregatedOutput.put(outputVariable, new HashMap<>());
                }

                Map<String, Double> currentOutput = aggregatedOutput.get(outputVariable);

                for (Map.Entry<String, Double> lvEntry : linguisticValues.entrySet()) {
                    String linguisticValue = lvEntry.getKey();
                    Double firingStrength = lvEntry.getValue();

                    // Use MAX for aggregation
                    currentOutput.put(linguisticValue,
                        Math.max(currentOutput.getOrDefault(linguisticValue, 0.0), firingStrength));
                }
            }
        }

        return aggregatedOutput;
    }

    /**
     * Performs inference and returns defuzzified crisp output
     * @param fuzzyInputs - Fuzzified inputs
     * @param defuzzifier - Defuzzifier to convert fuzzy output to crisp value
     * @return Crisp output value
     */
    public double inferAndDefuzzify(Map<String, Map<String, Double>> fuzzyInputs, Defuzzifier defuzzifier) {
        Map<String, Map<String, Double>> fuzzyOutput = infer(fuzzyInputs);
        // This would need the defuzzifier to be fully implemented
        // For now, returning the inference result structure
        return 0.0; // Placeholder
    }

    public List<FuzzyRule> getRules() {
        return rules;
    }

    public void setRules(List<FuzzyRule> rules) {
        this.rules = rules;
    }

    /**
     * Prints the inference process for debugging
     */
    public void printInferenceDetails(Map<String, Map<String, Double>> fuzzyInputs) {
        System.out.println("=== Inference Engine Details ===");
        System.out.println("\nInput Fuzzy Sets:");
        for (Map.Entry<String, Map<String, Double>> entry : fuzzyInputs.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("\nRule Evaluation:");
        for (FuzzyRule rule : rules) {
            double firingStrength = rule.evaluateFiringStrength(fuzzyInputs);
            System.out.println("  " + rule.getName() + " - Firing Strength: " + firingStrength);
        }

        System.out.println("\nAggregated Output:");
        Map<String, Map<String, Double>> output = infer(fuzzyInputs);
        for (Map.Entry<String, Map<String, Double>> entry : output.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
    }
}
