package com.example.softcomputing.fuzzy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.softcomputing.fuzzy.utils.FuzzySet;

/**
 * A modular FuzzyController configured via the Builder pattern. Allows swapping
 * of fuzzifier, defuzzifier and inference engine (which itself can be configured
 * with different t-norm/s-norm implementations).
 */
public class FuzzyController {

    private Fuzzifier fuzzifier;
    private Defuzzifier defuzzifier;
    private InferenceEngineInterface inferenceEngine;

    private FuzzyController() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Fuzzifier fuzzifier;
        private Defuzzifier defuzzifier;
        private InferenceEngineInterface inferenceEngine;

        public Builder withFuzzifier(Fuzzifier fuzzifier) {
            this.fuzzifier = fuzzifier;
            return this;
        }

        public Builder withDefuzzifier(Defuzzifier defuzzifier) {
            this.defuzzifier = defuzzifier;
            return this;
        }

        public Builder withInferenceEngine(InferenceEngineInterface inferenceEngine) {
            this.inferenceEngine = inferenceEngine;
            return this;
        }

        public FuzzyController build() {
            FuzzyController controller = new FuzzyController();
            controller.fuzzifier = this.fuzzifier;
            controller.defuzzifier = this.defuzzifier;
            controller.inferenceEngine = this.inferenceEngine;
            return controller;
        }
    }

    public Fuzzifier getFuzzifier() { return fuzzifier; }
    public Defuzzifier getDefuzzifier() { return defuzzifier; }
    public InferenceEngineInterface getInferenceEngine() { return inferenceEngine; }

    // Fluent instance setters so callers can configure controller directly
    public FuzzyController withFuzzifier(Fuzzifier fuzzifier) {
        this.fuzzifier = fuzzifier;
        return this;
    }

    public FuzzyController withDefuzzifier(Defuzzifier defuzzifier) {
        this.defuzzifier = defuzzifier;
        return this;
    }

    public FuzzyController withInferenceEngine(InferenceEngineInterface inferenceEngine) {
        this.inferenceEngine = inferenceEngine;
        return this;
    }

    /**
     * Run fuzzification phase and return the produced FuzzySet.
     */
    public FuzzySet runFuzzification(Map<String, Double> crispInputs, List<String> features) {
        if (fuzzifier == null) throw new IllegalStateException("Fuzzifier not configured");
        return fuzzifier.fuzzify(crispInputs, features);
    }

    /**
     * Run inference phase given an already-fuzzified input map.
     */
    public Map<String, Map<String, Double>> runInference(Map<String, Map<String, Double>> fuzzyInputs) {
        if (inferenceEngine == null) throw new IllegalStateException("Inference engine not configured");
        return inferenceEngine.infer(fuzzyInputs);
    }

    /**
     * Defuzzify fuzzy outputs (assumes single output variable for simplicity).
     */
    public double runDefuzzifyFromInference(Map<String, Map<String, Double>> fuzzyOutputs) {
        if (defuzzifier == null) throw new IllegalStateException("Defuzzifier not configured");

        if (fuzzyOutputs == null || fuzzyOutputs.isEmpty()) return 0.0;

        Map.Entry<String, Map<String, Double>> first = fuzzyOutputs.entrySet().iterator().next();
        Map<String, Double> lvMap = first.getValue();

        // Convert linguistic map to a simple FuzzySet by placing each linguistic value at an incremental x.
        FuzzySet fs = convertLinguisticMapToFuzzySet(lvMap);
        return defuzzifier.defuzzify(fs);
    }

    /**
     * Convenience full-run: fuzzify -> infer -> defuzzify. This method will try to
     * convert the produced FuzzySet into the map expected by the inference engine via
     * reflection (looks for getMembershipValues()). If conversion isn't possible the
     * method throws an IllegalStateException and suggests using the more specific
     * runXXX methods.
     */
    @SuppressWarnings("unchecked")
    public double run(Map<String, Double> crispInputs, List<String> features) {
        FuzzySet fs = runFuzzification(crispInputs, features);
        return run(fs);
    }

    /**
     * Run starting from a FuzzySet. Attempts to extract membership values and passes them to inference
     * then defuzzification.
     */
    @SuppressWarnings("unchecked")
    public double run(FuzzySet fs) {
        if (fs == null) throw new IllegalArgumentException("FuzzySet is null");

        try {
            Method m = fs.getClass().getMethod("getMembershipValues");
            Object membershipObj = m.invoke(fs);
            if (membershipObj instanceof Map) {
                Map<String, Double> membershipValues = (Map<String, Double>) membershipObj;
                Map<String, Map<String, Double>> fuzzyInputs = new HashMap<>();
                fuzzyInputs.put("input", membershipValues);

                // Use engine's convenience defuzzify path if it provides one
                return inferenceEngine.inferAndDefuzzify(fuzzyInputs, defuzzifier);
            }
        } catch (NoSuchMethodException nsme) {
            // fall through to error below
        } catch (Exception e) {
            throw new RuntimeException("Error while converting FuzzySet to membership map: " + e.getMessage(), e);
        }

        throw new IllegalStateException("Unable to convert FuzzySet into inference input map. Use runFuzzification() and runInference() overloads or update your FuzzySet/fuzzifier to expose membership values via getMembershipValues().");
    }

    /**
     * Run starting from a fuzzy input map: inference -> defuzzification.
     */
    public double run(Map<String, Map<String, Double>> fuzzyInputs) {
        Map<String, Map<String, Double>> fuzzyOutput = runInference(fuzzyInputs);
        return runDefuzzifyFromInference(fuzzyOutput);
    }

    private FuzzySet convertLinguisticMapToFuzzySet(Map<String, Double> lvMap) {
        FuzzySet fs = new FuzzySet("auto");
        int idx = 0;
        for (Map.Entry<String, Double> e : lvMap.entrySet()) {
            // x coordinate is an index; y is membership degree
            fs.addPoint(idx++, e.getValue());
        }
        return fs;
    }

}

