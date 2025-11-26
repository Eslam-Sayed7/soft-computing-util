package com.example.softcomputing.fuzzy;

import java.util.List;

import com.example.softcomputing.fuzzy.operators.TNorm;

/** Simple zero-order Sugeno inference engine.
 * It computes a weighted average of rule consequents using the rule firing strengths.
 */
public class SugenoInferenceEngine implements InferenceEngineInterface {

    private List<SugenoRule> rules;
    private TNorm tNorm;

    public SugenoInferenceEngine(List<SugenoRule> rules, TNorm tNorm) {
        this.rules = rules;
        this.tNorm = tNorm;
    }

    @Override
    public java.util.Map<String, java.util.Map<String, Double>> infer(java.util.Map<String, java.util.Map<String, Double>> fuzzyInputs) {
        // Sugeno engines typically return a crisp result; leave fuzzy mapping empty
        return new java.util.HashMap<>();
    }

    @Override
    public double inferAndDefuzzify(java.util.Map<String, java.util.Map<String, Double>> fuzzyInputs, Defuzzifier defuzzifier) {
        double numerator = 0.0;
        double denominator = 0.0;

        for (SugenoRule rule : rules) {
            double fs = rule.evaluateFiringStrength(fuzzyInputs, tNorm);
            numerator += fs * rule.getConsequentValue();
            denominator += fs;
        }

        return denominator == 0.0 ? 0.0 : numerator / denominator;
    }

}
