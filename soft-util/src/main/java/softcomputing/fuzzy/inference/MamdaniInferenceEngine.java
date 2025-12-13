package softcomputing.fuzzy.inference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import softcomputing.fuzzy.operators.SNorm;
import softcomputing.fuzzy.operators.TNorm;
import softcomputing.fuzzy.utils.FuzzyRule;

public class MamdaniInferenceEngine implements InferenceEngineInterface {

    private List<FuzzyRule> rules;
    private TNorm tNorm;
    private SNorm sNorm;

    public MamdaniInferenceEngine(List<FuzzyRule> rules, TNorm tNorm, SNorm sNorm) {
        this.rules = rules;
        this.tNorm = tNorm;
        this.sNorm = sNorm;
    }

    @Override
    public Map<String, Map<String, Double>> infer(Map<String, Map<String, Double>> fuzzyInputs) {
        Map<String, Map<String, Double>> aggregatedOutput = new HashMap<>();

        for (FuzzyRule rule : rules) {
            // Get rule output based on firing strength
            Map<String, Map<String, Double>> ruleOutput = rule.apply(fuzzyInputs, tNorm);

            // Aggregate outputs
            for (Map.Entry<String, Map<String, Double>> entry : ruleOutput.entrySet()) {
                String outputVariable = entry.getKey();
                Map<String, Double> linguisticValues = entry.getValue();

                aggregatedOutput.putIfAbsent(outputVariable, new HashMap<>());
                Map<String, Double> currentOutput = aggregatedOutput.get(outputVariable);

                // Combine values
                for (Map.Entry<String, Double> lvEntry : linguisticValues.entrySet()) {
                    String linguisticValue = lvEntry.getKey();
                    Double firingStrength = lvEntry.getValue();

                    double existing = currentOutput.getOrDefault(linguisticValue, 0.0);
                    double combined = sNorm.apply(existing, firingStrength);
                    currentOutput.put(linguisticValue, combined);
                }
            }
        }

        return aggregatedOutput;
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

    public SNorm getsNorm() {
        return sNorm;
    }
}