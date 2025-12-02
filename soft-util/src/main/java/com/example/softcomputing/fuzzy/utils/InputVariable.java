package com.example.softcomputing.fuzzy.utils;

import java.util.HashMap;
import java.util.Map;

import com.example.softcomputing.fuzzy.membershipFuns.MembershipFunction;

public class InputVariable {
    private String name;
    private Map<String, MembershipFunction> linguisticTerms;
    private double minValue;
    private double maxValue;

    public InputVariable(String name, double minValue, double maxValue) {
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.linguisticTerms = new HashMap<>();
    }

    public void addTerm(String linguisticTerm, MembershipFunction membershipFunction) {
        linguisticTerms.put(linguisticTerm, membershipFunction);
    }

    public Map<String, Double> fuzzify(double crispValue) {
        Map<String, Double> membershipDegrees = new HashMap<>();

        for (Map.Entry<String, MembershipFunction> entry : linguisticTerms.entrySet()) {
            String term = entry.getKey();
            MembershipFunction mf = entry.getValue();
            double degree = mf.apply(crispValue);
            membershipDegrees.put(term, degree);
        }

        return membershipDegrees;
    }

    public String getName() {
        return name;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public Map<String, MembershipFunction> getLinguisticTerms() {
        return new HashMap<>(linguisticTerms);
    }

    public boolean hasTerm(String linguisticTerm) {
        return linguisticTerms.containsKey(linguisticTerm);
    }
}