package com.example.softcomputing.fuzzy;

import java.util.Map;
import java.util.HashMap;

public class FuzzyRule {
    
    private String name;
    private Map<String, String> antecedents; // e.g., {"Interest": "High", "Budget": "Medium"}
    private Map<String, String> consequent;  // e.g., {"Recommendation": "Buy"}

    public FuzzyRule(String name, Map<String, String> antecedents, Map<String, String> consequent) {
        this.name = name;
        this.antecedents = antecedents;
        this.consequent = consequent;
    }

    /**
     * Evaluates the rule's firing strength using MIN operator (Mamdani)
     * @param fuzzyInputs - Fuzzified input values
     * @return firing strength (0-1)
     */
    public double evaluateFiringStrength(Map<String, Map<String, Double>> fuzzyInputs) {
        double firingStrength = 1.0;

        // Apply MIN operation across all antecedents
        for (Map.Entry<String, String> antecedent : antecedents.entrySet()) {
            String variable = antecedent.getKey();
            String linguisticValue = antecedent.getValue();

            if (fuzzyInputs.containsKey(variable)) {
                Map<String, Double> membershipValues = fuzzyInputs.get(variable);
                if (membershipValues.containsKey(linguisticValue)) {
                    double membershipDegree = membershipValues.get(linguisticValue);
                    firingStrength = Math.min(firingStrength, membershipDegree);
                } else {
                    return 0.0; // If linguistic value not found, rule doesn't fire
                }
            } else {
                return 0.0; // If variable not found, rule doesn't fire
            }
        }

        return firingStrength;
    }

    /**
     * Applies the rule and returns the consequent with firing strength
     */
    public Map<String, Map<String, Double>> apply(Map<String, Map<String, Double>> fuzzyInputs) {
        double firingStrength = evaluateFiringStrength(fuzzyInputs);
        Map<String, Map<String, Double>> result = new HashMap<>();

        // Apply firing strength to consequent
        for (Map.Entry<String, String> cons : consequent.entrySet()) {
            String outputVariable = cons.getKey();
            String linguisticValue = cons.getValue();

            Map<String, Double> outputMap = new HashMap<>();
            outputMap.put(linguisticValue, firingStrength);
            result.put(outputVariable, outputMap);
        }

        return result;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getAntecedents() {
        return antecedents;
    }

    public Map<String, String> getConsequent() {
        return consequent;
    }
}
