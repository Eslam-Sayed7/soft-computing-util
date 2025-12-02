package com.example.softcomputing.fuzzy.inference;

import java.util.HashMap;
import java.util.List;

import com.example.softcomputing.fuzzy.operators.TNorm;
import com.example.softcomputing.fuzzy.utils.FuzzyRule;

public class SugenoInferenceEngine implements InferenceEngineInterface {

    private List<FuzzyRule> rules;
    private TNorm tNorm;

    public SugenoInferenceEngine(List<FuzzyRule> rules, TNorm tNorm) {
        this.rules = rules;
        this.tNorm = tNorm;
    }

    @Override
    public java.util.Map<String, java.util.Map<String, Double>> infer(
            java.util.Map<String, java.util.Map<String, Double>> fuzzyInputs) {
        return new HashMap<>();
    }

    public double computeWeightedAverage(java.util.Map<String, java.util.Map<String, Double>> fuzzyInputs) {
        double numerator = 0.0;
        double denominator = 0.0;

        for (FuzzyRule rule : rules) {
            if (!rule.isSugenoType()) {
                throw new IllegalStateException("SugenoInferenceEngine requires Sugeno-type rules");
            }

            double firingStrength = rule.evaluateFiringStrength(fuzzyInputs, tNorm);
            numerator += firingStrength * rule.getConsequentValue();
            denominator += firingStrength;
        }

        return denominator == 0.0 ? 0.0 : numerator / denominator;
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
}