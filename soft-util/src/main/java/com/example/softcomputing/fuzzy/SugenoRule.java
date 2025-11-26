package com.example.softcomputing.fuzzy;

import java.util.Map;

import com.example.softcomputing.fuzzy.operators.TNorm;

/** A simple Sugeno rule where the consequent is a numeric value (zero-order Sugeno).
 * antecedents: same shape as FuzzyRule
 * consequentValue: numeric output that will be weighted by firing strength
 */
public class SugenoRule {

    private String name;
    private Map<String, String> antecedents;
    private double consequentValue;

    public SugenoRule(String name, Map<String, String> antecedents, double consequentValue) {
        this.name = name;
        this.antecedents = antecedents;
        this.consequentValue = consequentValue;
    }

    public double evaluateFiringStrength(Map<String, Map<String, Double>> fuzzyInputs, TNorm tNorm) {
        double firingStrength = -1.0;

        for (Map.Entry<String, String> antecedent : antecedents.entrySet()) {
            String variable = antecedent.getKey();
            String linguisticValue = antecedent.getValue();

            if (fuzzyInputs.containsKey(variable)) {
                Map<String, Double> membershipValues = fuzzyInputs.get(variable);
                if (membershipValues.containsKey(linguisticValue)) {
                    double membershipDegree = membershipValues.get(linguisticValue);
                    if (firingStrength < 0) firingStrength = membershipDegree;
                    else firingStrength = tNorm.apply(firingStrength, membershipDegree);
                } else {
                    return 0.0;
                }
            } else {
                return 0.0;
            }
        }

        return firingStrength < 0 ? 0.0 : firingStrength;
    }

    public double getConsequentValue() {
        return consequentValue;
    }

    public String getName() {
        return name;
    }
}
