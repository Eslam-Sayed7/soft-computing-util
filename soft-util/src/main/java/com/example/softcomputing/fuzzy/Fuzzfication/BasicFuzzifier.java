package com.example.softcomputing.fuzzy.Fuzzfication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.softcomputing.fuzzy.utils.FuzzySet;
import com.example.softcomputing.fuzzy.utils.InputVariable;

public class BasicFuzzifier implements Fuzzifier {

    private Map<String, InputVariable> inputVariables;

    public BasicFuzzifier() {
        this.inputVariables = new HashMap<>();
    }

    public void addInputVariable(InputVariable inputVariable) {
        inputVariables.put(inputVariable.getName(), inputVariable);
    }

    @Override
    public FuzzySet fuzzify(Map<String, Double> crispInput, List<String> features) {
        FuzzySet fuzzySet = new FuzzySet("fuzzified_input");
        Map<String, Double> allMembershipValues = new HashMap<>();

        for (String feature : features) {
            Double crispValue = crispInput.get(feature);

            if (crispValue == null) {
                throw new IllegalArgumentException("Missing crisp input for feature: " + feature);
            }

            InputVariable inputVar = inputVariables.get(feature);

            if (inputVar == null) {
                throw new IllegalStateException(
                        "No InputVariable configured for feature: " + feature +
                                ". Add it using fuzzifier.addInputVariable()");
            }

            // Fuzzify the crisp value
            Map<String, Double> membershipDegrees = inputVar.fuzzify(crispValue);

            for (Map.Entry<String, Double> entry : membershipDegrees.entrySet()) {
                String linguisticTerm = entry.getKey();
                Double degree = entry.getValue();
                allMembershipValues.put(linguisticTerm, degree);
            }
        }

        fuzzySet.setMembershipValues(allMembershipValues);

        return fuzzySet;
    }

    public Map<String, Map<String, Double>> fuzzifyToMap(Map<String, Double> crispInput, List<String> features) {
        Map<String, Map<String, Double>> fuzzyInputs = new HashMap<>();

        for (String feature : features) {
            Double crispValue = crispInput.get(feature);

            if (crispValue == null) {
                throw new IllegalArgumentException("Missing crisp input for feature: " + feature);
            }

            InputVariable inputVar = inputVariables.get(feature);

            if (inputVar == null) {
                throw new IllegalStateException(
                        "No InputVariable configured for feature: " + feature);
            }

            Map<String, Double> membershipDegrees = inputVar.fuzzify(crispValue);
            fuzzyInputs.put(feature, membershipDegrees);
        }

        return fuzzyInputs;
    }

    public Map<String, InputVariable> getInputVariables() {
        return new HashMap<>(inputVariables);
    }

    public InputVariable getInputVariable(String name) {
        return inputVariables.get(name);
    }

    public boolean hasInputVariable(String name) {
        return inputVariables.containsKey(name);
    }
}