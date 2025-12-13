package softcomputing.fuzzy.utils;

import java.util.HashMap;
import java.util.Map;

import softcomputing.fuzzy.operators.TNorm;

public class FuzzyRule {

    private String name;
    private Map<String, String> input; // {"Interest": "High", "Budget": "Medium"}
    private Map<String, String> output; // {"Recommendation": "Buy"}
    private Double consequentValue; // optional for sugeno rules

    public FuzzyRule(String name, Map<String, String> input, Map<String, String> output) {
        this.name = name;
        this.input = input;
        this.output = output;
        this.consequentValue = null;

    }

    // Sugeno-style rules  consequent is a numeric value.
    public FuzzyRule(String name, Map<String, String> input, double consequentValue) {
        this.name = name;
        this.input = input;
        this.output = new HashMap<>();
        this.consequentValue = consequentValue;
    }

    public Map<String, Map<String, Double>> apply(Map<String, Map<String, Double>> fuzzyInputs, TNorm tNorm) {
        double firingStrength = evaluateFiringStrength(fuzzyInputs, tNorm);
        Map<String, Map<String, Double>> result = new HashMap<>();

        // Apply firing strength to output
        for (Map.Entry<String, String> cons : output.entrySet()) {
            String outputVariable = cons.getKey();
            String linguisticValue = cons.getValue();

            Map<String, Double> outputMap = new HashMap<>();
            outputMap.put(linguisticValue, firingStrength);
            result.put(outputVariable, outputMap);
        }

        return result;
    }

    public double evaluateFiringStrength(Map<String, Map<String, Double>> fuzzyInputs, TNorm tNorm) {
        double firingStrength = -1.0;
        // consider a rule interest=high
        for (Map.Entry<String, String> antecedent : input.entrySet()) {
            String variable = antecedent.getKey(); // interest
            String linguisticValue = antecedent.getValue(); // high

            if (fuzzyInputs.containsKey(variable)) {
                Map<String, Double> membershipValues = fuzzyInputs.get(variable); // find it in fuzzy inputs
                if (membershipValues.containsKey(linguisticValue)) { // find the exact match
                    double membershipDegree = membershipValues.get(linguisticValue); // get its fuzzification value
                    if (firingStrength < 0) {
                        firingStrength = membershipDegree; // this makes firing strength > 0
                    } else {
                        firingStrength = tNorm.apply(firingStrength, membershipDegree); // so this applies the second
                                                                                        // part of the rule
                    }
                } else {
                    return 0.0;
                }
            } else {
                return 0.0;
            }
        }

        return firingStrength < 0 ? 0.0 : firingStrength;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getinput() {
        return input;
    }

    public Map<String, String> getoutput() {
        return output;
    }

    public Map<String, String> getConsequent() {
        return new HashMap<>(output);
    }

    public boolean isSugenoType() {
        return consequentValue != null;
    }

    public double getConsequentValue() {
        return consequentValue == null ? 0.0 : consequentValue.doubleValue();
    }

}
