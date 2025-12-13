package softcomputing.fuzzy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import softcomputing.fuzzy.utils.FuzzyRule;

public class RuleManager {

    private final List<FuzzyRule> rules;
    private final Map<String, Boolean> ruleEnabled;
    private final Map<String, Double> ruleWeights;

    public RuleManager() {
        this.rules = new ArrayList<>();
        this.ruleEnabled = new HashMap<>();
        this.ruleWeights = new HashMap<>();
    }

    public void addRule(FuzzyRule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("Rule cannot be null");
        }
        rules.add(rule);
        ruleEnabled.put(rule.getName(), true);
        ruleWeights.put(rule.getName(), 1.0);
    }

    public boolean removeRule(String ruleName) {
        boolean removed = rules.removeIf(r -> r.getName().equals(ruleName));
        if (removed) {
            ruleEnabled.remove(ruleName);
            ruleWeights.remove(ruleName);
        }
        return removed;
    }

    public void setRuleEnabled(String ruleName, boolean enabled) {
        if (!ruleEnabled.containsKey(ruleName)) {
            throw new IllegalArgumentException("Rule not found: " + ruleName);
        }
        ruleEnabled.put(ruleName, enabled);
    }

    public void setRuleWeight(String ruleName, double weight) {
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be between 0.0 and 1.0");
        }
        if (!ruleWeights.containsKey(ruleName)) {
            throw new IllegalArgumentException("Rule not found: " + ruleName);
        }
        ruleWeights.put(ruleName, weight);
    }

    public List<FuzzyRule> getAllRules() {
        return new ArrayList<>(rules);
    }

    public List<FuzzyRule> getActiveRules() {
        return rules.stream()
                .filter(r -> ruleEnabled.getOrDefault(r.getName(), true))
                .collect(Collectors.toList());
    }

    public boolean isRuleEnabled(String ruleName) {
        return ruleEnabled.getOrDefault(ruleName, false);
    }

    public double getRuleWeight(String ruleName) {
        return ruleWeights.getOrDefault(ruleName, 1.0);
    }

    public boolean hasRule(String ruleName) {
        return ruleEnabled.containsKey(ruleName);
    }

    public int getRuleCount() {
        return rules.size();
    }

    public int getActiveRuleCount() {
        return (int) rules.stream()
                .filter(r -> ruleEnabled.getOrDefault(r.getName(), true))
                .count();
    }

    public void clearAllRules() {
        rules.clear();
        ruleEnabled.clear();
        ruleWeights.clear();
    }
}