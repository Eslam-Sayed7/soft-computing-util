package com.example.softcomputing.fuzzy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.softcomputing.fuzzy.Defuzzifiers.Centroid;
import com.example.softcomputing.fuzzy.Defuzzifiers.Defuzzifier;
import com.example.softcomputing.fuzzy.Fuzzfication.BasicFuzzifier;
import com.example.softcomputing.fuzzy.Fuzzfication.Fuzzifier;
import com.example.softcomputing.fuzzy.inference.InferenceEngineInterface;
import com.example.softcomputing.fuzzy.inference.MamdaniInferenceEngine;
import com.example.softcomputing.fuzzy.utils.FuzzyRule;
import com.example.softcomputing.fuzzy.utils.FuzzySet;
import com.example.softcomputing.fuzzy.utils.InputDomain;
import com.example.softcomputing.fuzzy.utils.InputHandlingStrategy;
import com.example.softcomputing.fuzzy.utils.OutputVariable;

public class FuzzyController {

    // Core components
    private Fuzzifier fuzzifier;
    private Defuzzifier defuzzifier;
    private InferenceEngineInterface inferenceEngine;
    private RuleManager ruleManager;

    // Input/Output configuration
    private Map<String, Double> defaultInputValues;
    private Map<String, InputDomain> inputDomains;
    private Map<String, OutputVariable> outputVariables;

    // Debugging
    private Map<String, Map<String, Double>> lastFuzzyInputs;
    private Map<String, Map<String, Double>> lastAggregatedOutput;
    private double lastCrispOutput;
    private InputHandlingStrategy inputHandlingStrategy;

    private FuzzyController() {
        this.ruleManager = new RuleManager();
        this.defaultInputValues = new HashMap<>();
        this.inputDomains = new HashMap<>();
        this.outputVariables = new HashMap<>();
        this.inputHandlingStrategy = InputHandlingStrategy.CLAMP_TO_DOMAIN;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private FuzzyController controller = new FuzzyController();

        public Builder withFuzzifier(Fuzzifier fuzzifier) {
            controller.fuzzifier = fuzzifier;
            return this;
        }

        public Builder withDefuzzifier(Defuzzifier defuzzifier) {
            controller.defuzzifier = defuzzifier;
            return this;
        }

        public Builder withInferenceEngine(InferenceEngineInterface inferenceEngine) {
            controller.inferenceEngine = inferenceEngine;
            return this;
        }

        public Builder withInputHandlingStrategy(InputHandlingStrategy strategy) {
            controller.inputHandlingStrategy = strategy;
            return this;
        }

        public Builder addInputDomain(String variableName, double min, double max) {
            controller.inputDomains.put(variableName, new InputDomain(min, max));
            return this;
        }

        public Builder addDefaultInput(String variableName, double defaultValue) {
            controller.defaultInputValues.put(variableName, defaultValue);
            return this;
        }

        public Builder addOutputVariable(OutputVariable outputVariable) {
            controller.outputVariables.put(outputVariable.getName(), outputVariable);
            return this;
        }

        public Builder addRule(FuzzyRule rule) {
            controller.addRule(rule);
            return this;
        }

        public FuzzyController build() {
            if (controller.fuzzifier == null) {
                throw new IllegalStateException("Fuzzifier must be configured");
            }
            if (controller.defuzzifier == null) {
                controller.defuzzifier = (Defuzzifier) new Centroid(1000);
            }
            if (controller.inferenceEngine == null) {
                throw new IllegalStateException("InferenceEngine must be configured");
            }
            return controller;
        }
    }

    // delegation to rule manager

    public void addRule(FuzzyRule rule) {
        ruleManager.addRule(rule);
        updateInferenceEngine();
    }

    public boolean removeRule(String ruleName) {
        boolean removed = ruleManager.removeRule(ruleName);
        if (removed) {
            updateInferenceEngine();
        }
        return removed;
    }

    public void setRuleEnabled(String ruleName, boolean enabled) {
        ruleManager.setRuleEnabled(ruleName, enabled);
        updateInferenceEngine();
    }

    public void setRuleWeight(String ruleName, double weight) {
        ruleManager.setRuleWeight(ruleName, weight);
    }

    public List<FuzzyRule> getAllRules() {
        return ruleManager.getAllRules();
    }

    public List<FuzzyRule> getActiveRules() {
        return ruleManager.getActiveRules();
    }

    public boolean isRuleEnabled(String ruleName) {
        return ruleManager.isRuleEnabled(ruleName);
    }

    public double getRuleWeight(String ruleName) {
        return ruleManager.getRuleWeight(ruleName);
    }

    public RuleManager getRuleManager() {
        return ruleManager;
    }

    private void updateInferenceEngine() {
        if (inferenceEngine instanceof MamdaniInferenceEngine) {
            ((MamdaniInferenceEngine) inferenceEngine).setRules(getActiveRules());
        }
    }

    // ========== INPUT HANDLING & VALIDATION ==========

    private Map<String, Double> handleInputs(Map<String, Double> crispInputs, List<String> features) {
        Map<String, Double> processedInputs = new HashMap<>();

        for (String feature : features) {
            Double value = crispInputs.get(feature);

            if (value == null) {
                value = handleMissingInput(feature);
            }

            processedInputs.put(feature, value);
        }

        return processedInputs;
    }

    private double handleMissingInput(String feature) {
        switch (inputHandlingStrategy) {
            case USE_DEFAULT:
                if (defaultInputValues.containsKey(feature)) {
                    return defaultInputValues.get(feature);
                }
                throw new IllegalArgumentException(
                        "Missing input for feature: " + feature + " and no default provided");

            case THROW_ERROR:
                throw new IllegalArgumentException("Missing input for feature: " + feature);

            case CLAMP_TO_DOMAIN:
                throw new IllegalArgumentException("Missing input for feature: " + feature + " and no domain defined");

            default:
                throw new IllegalStateException("Unknown input handling strategy");
        }
    }

    public double evaluate(Map<String, Double> crispInputs, List<String> features) {
        Map<String, Double> processedInputs = handleInputs(crispInputs, features);

        Map<String, Map<String, Double>> fuzzyInputMap;
        if (fuzzifier instanceof BasicFuzzifier) {
            fuzzyInputMap = ((BasicFuzzifier) fuzzifier).fuzzifyToMap(processedInputs, features);
        } else {
            FuzzySet fuzzySet = fuzzifier.fuzzify(processedInputs, features);
            fuzzyInputMap = convertFuzzySetToMap(fuzzySet, features);
        }
        this.lastFuzzyInputs = new HashMap<>(fuzzyInputMap);

        Map<String, Map<String, Double>> aggregatedOutput = inferenceEngine.infer(fuzzyInputMap);
        this.lastAggregatedOutput = new HashMap<>(aggregatedOutput);

        double crispOutput = defuzzifyOutput(aggregatedOutput);
        this.lastCrispOutput = crispOutput;

        return crispOutput;
    }

    private double defuzzifyOutput(Map<String, Map<String, Double>> aggregatedOutput) {
        if (aggregatedOutput == null || aggregatedOutput.isEmpty()) {
            return 0.0;
        }

        // Get the first output variable
        Map.Entry<String, Map<String, Double>> firstOutput = aggregatedOutput.entrySet().iterator().next();
        String outputVarName = firstOutput.getKey();
        Map<String, Double> linguisticValues = firstOutput.getValue();

        // Get the output variable configuration
        OutputVariable outputVar = outputVariables.get(outputVarName);
        if (outputVar == null) {
            throw new IllegalStateException(
                    "No OutputVariable configured for: " + outputVarName +
                            ". Add it using builder.addOutputVariable()");
        }

        FuzzySet outputSet = buildOutputFuzzySet(linguisticValues, outputVar);
        return defuzzifier.defuzzify(outputSet);
    }

    /**
     * Builds a FuzzySet for defuzzification using the output variable's mapping
     */
    private FuzzySet buildOutputFuzzySet(Map<String, Double> linguisticValues, OutputVariable outputVar) {
        FuzzySet outputSet = new FuzzySet("output");
        outputSet.setMembershipValues(linguisticValues);

        for (Map.Entry<String, Double> entry : linguisticValues.entrySet()) {
            String linguisticTerm = entry.getKey();
            double membershipDegree = entry.getValue();

            Double crispValue = outputVar.getCrispValue(linguisticTerm);
            if (crispValue == null) {
                throw new IllegalStateException(
                        "Linguistic term '" + linguisticTerm +
                                "' not mapped in OutputVariable '" + outputVar.getName() + "'");
            }

            outputSet.addPoint(crispValue, membershipDegree);
        }

        return outputSet;
    }

    private Map<String, Map<String, Double>> convertFuzzySetToMap(FuzzySet fuzzySet, List<String> features) {
        Map<String, Map<String, Double>> result = new HashMap<>();
        Map<String, Double> membershipValues = fuzzySet.getMembershipValues();

        if (!membershipValues.isEmpty()) {
            for (String feature : features) {
                result.put(feature, new HashMap<>(membershipValues));
            }
        } else {
            if (!features.isEmpty()) {
                result.put(features.get(0), membershipValues);
            }
        }

        return result;
    }

    // ========== GETTERS & SETTERS ==========

    public Map<String, Map<String, Double>> getLastFuzzyInputs() {
        return lastFuzzyInputs != null ? new HashMap<>(lastFuzzyInputs) : new HashMap<>();
    }

    public Map<String, Map<String, Double>> getLastAggregatedOutput() {
        return lastAggregatedOutput != null ? new HashMap<>(lastAggregatedOutput) : new HashMap<>();
    }

    public double getLastCrispOutput() {
        return lastCrispOutput;
    }

    public void setFuzzifier(Fuzzifier fuzzifier) {
        this.fuzzifier = fuzzifier;
    }

    public void setDefuzzifier(Defuzzifier defuzzifier) {
        this.defuzzifier = defuzzifier;
    }

    public void setInferenceEngine(InferenceEngineInterface inferenceEngine) {
        this.inferenceEngine = inferenceEngine;
    }

    public Fuzzifier getFuzzifier() {
        return fuzzifier;
    }

    public Defuzzifier getDefuzzifier() {
        return defuzzifier;
    }

    public InferenceEngineInterface getInferenceEngine() {
        return inferenceEngine;
    }
}