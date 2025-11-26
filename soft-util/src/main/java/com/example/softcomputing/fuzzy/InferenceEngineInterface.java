package com.example.softcomputing.fuzzy;

public interface InferenceEngineInterface {

    /**
     * Performs inference and returns aggregated fuzzy output (for Mamdani-style engines).
     */
    java.util.Map<String, java.util.Map<String, Double>> infer(java.util.Map<String, java.util.Map<String, Double>> fuzzyInputs);

    /**
     * Performs inference and returns a defuzzified crisp value (engines can ignore defuzzifier if not needed).
     */
    double inferAndDefuzzify(java.util.Map<String, java.util.Map<String, Double>> fuzzyInputs, Defuzzifier defuzzifier);
}
