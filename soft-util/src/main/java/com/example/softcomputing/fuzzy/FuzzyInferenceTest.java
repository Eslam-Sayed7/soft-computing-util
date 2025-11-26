package com.example.softcomputing.fuzzy;

import java.util.*;

/**
 * Test class demonstrating Fuzzy Inference Engine usage
 * Example: Product Recommendation System based on Interest and Budget
 */
public class FuzzyInferenceTest {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Fuzzy Inference Engine Test");
        System.out.println("========================================\n");

        // Step 1: Create fuzzy rules for product recommendation
        List<FuzzyRule> rules = createRules();

        // Step 2: Initialize the Inference Engine
        InferenceEngine inferenceEngine = new InferenceEngine(rules);

        // Step 3: Create fuzzified inputs (simulating the output from Fuzzifier)
        Map<String, Map<String, Double>> fuzzyInputs = createFuzzyInputs();

        // Step 4: Display the rules
        System.out.println("Fuzzy Rules:");
        for (FuzzyRule rule : rules) {
            System.out.println("  " + rule.getName());
            System.out.println("    IF " + rule.getAntecedents() + " THEN " + rule.getConsequent());
        }
        System.out.println();

        // Step 5: Run inference and display detailed results
        inferenceEngine.printInferenceDetails(fuzzyInputs);

        // Step 6: Test with different input scenarios
        System.out.println("\n========================================");
        System.out.println("Testing Different Scenarios");
        System.out.println("========================================\n");

        testScenario1(inferenceEngine);
        testScenario2(inferenceEngine);
        testScenario3(inferenceEngine);
    }

    /**
     * Creates a set of fuzzy rules for product recommendation
     */
    private static List<FuzzyRule> createRules() {
        List<FuzzyRule> rules = new ArrayList<>();

        // Rule 1: IF Interest is High AND Budget is High THEN Recommendation is StronglyRecommend
        Map<String, String> rule1Antecedents = new HashMap<>();
        rule1Antecedents.put("Interest", "High");
        rule1Antecedents.put("Budget", "High");
        Map<String, String> rule1Consequent = new HashMap<>();
        rule1Consequent.put("Recommendation", "StronglyRecommend");
        rules.add(new FuzzyRule("Rule1", rule1Antecedents, rule1Consequent));

        // Rule 2: IF Interest is High AND Budget is Medium THEN Recommendation is Recommend
        Map<String, String> rule2Antecedents = new HashMap<>();
        rule2Antecedents.put("Interest", "High");
        rule2Antecedents.put("Budget", "Medium");
        Map<String, String> rule2Consequent = new HashMap<>();
        rule2Consequent.put("Recommendation", "Recommend");
        rules.add(new FuzzyRule("Rule2", rule2Antecedents, rule2Consequent));

        // Rule 3: IF Interest is Medium AND Budget is High THEN Recommendation is Recommend
        Map<String, String> rule3Antecedents = new HashMap<>();
        rule3Antecedents.put("Interest", "Medium");
        rule3Antecedents.put("Budget", "High");
        Map<String, String> rule3Consequent = new HashMap<>();
        rule3Consequent.put("Recommendation", "Recommend");
        rules.add(new FuzzyRule("Rule3", rule3Antecedents, rule3Consequent));

        // Rule 4: IF Interest is Medium AND Budget is Medium THEN Recommendation is Consider
        Map<String, String> rule4Antecedents = new HashMap<>();
        rule4Antecedents.put("Interest", "Medium");
        rule4Antecedents.put("Budget", "Medium");
        Map<String, String> rule4Consequent = new HashMap<>();
        rule4Consequent.put("Recommendation", "Consider");
        rules.add(new FuzzyRule("Rule4", rule4Antecedents, rule4Consequent));

        // Rule 5: IF Interest is Low OR Budget is Low THEN Recommendation is DoNotRecommend
        Map<String, String> rule5Antecedents = new HashMap<>();
        rule5Antecedents.put("Interest", "Low");
        rule5Antecedents.put("Budget", "Low");
        Map<String, String> rule5Consequent = new HashMap<>();
        rule5Consequent.put("Recommendation", "DoNotRecommend");
        rules.add(new FuzzyRule("Rule5", rule5Antecedents, rule5Consequent));

        return rules;
    }

    /**
     * Creates sample fuzzified inputs
     * This simulates the output from a Fuzzifier
     */
    private static Map<String, Map<String, Double>> createFuzzyInputs() {
        Map<String, Map<String, Double>> fuzzyInputs = new HashMap<>();

        // Interest fuzzy values (e.g., from crisp input Interest = 7.5 out of 10)
        Map<String, Double> interestValues = new HashMap<>();
        interestValues.put("Low", 0.0);
        interestValues.put("Medium", 0.6);
        interestValues.put("High", 0.4);
        fuzzyInputs.put("Interest", interestValues);

        // Budget fuzzy values (e.g., from crisp input Budget = 8.0 out of 10)
        Map<String, Double> budgetValues = new HashMap<>();
        budgetValues.put("Low", 0.0);
        budgetValues.put("Medium", 0.8);
        budgetValues.put("High", 0.2);
        fuzzyInputs.put("Budget", budgetValues);

        return fuzzyInputs;
    }

    /**
     * Test Scenario 1: High Interest and High Budget
     */
    private static void testScenario1(InferenceEngine engine) {
        System.out.println("Scenario 1: High Interest, High Budget");
        Map<String, Map<String, Double>> inputs = new HashMap<>();

        Map<String, Double> interest = new HashMap<>();
        interest.put("Low", 0.0);
        interest.put("Medium", 0.1);
        interest.put("High", 0.9);
        inputs.put("Interest", interest);

        Map<String, Double> budget = new HashMap<>();
        budget.put("Low", 0.0);
        budget.put("Medium", 0.2);
        budget.put("High", 0.8);
        inputs.put("Budget", budget);

        Map<String, Map<String, Double>> output = engine.infer(inputs);
        System.out.println("Output: " + output);
        System.out.println();
    }

    /**
     * Test Scenario 2: Medium Interest and Medium Budget
     */
    private static void testScenario2(InferenceEngine engine) {
        System.out.println("Scenario 2: Medium Interest, Medium Budget");
        Map<String, Map<String, Double>> inputs = new HashMap<>();

        Map<String, Double> interest = new HashMap<>();
        interest.put("Low", 0.0);
        interest.put("Medium", 1.0);
        interest.put("High", 0.0);
        inputs.put("Interest", interest);

        Map<String, Double> budget = new HashMap<>();
        budget.put("Low", 0.0);
        budget.put("Medium", 1.0);
        budget.put("High", 0.0);
        inputs.put("Budget", budget);

        Map<String, Map<String, Double>> output = engine.infer(inputs);
        System.out.println("Output: " + output);
        System.out.println();
    }

    /**
     * Test Scenario 3: Low Interest and Low Budget
     */
    private static void testScenario3(InferenceEngine engine) {
        System.out.println("Scenario 3: Low Interest, Low Budget");
        Map<String, Map<String, Double>> inputs = new HashMap<>();

        Map<String, Double> interest = new HashMap<>();
        interest.put("Low", 0.9);
        interest.put("Medium", 0.1);
        interest.put("High", 0.0);
        inputs.put("Interest", interest);

        Map<String, Double> budget = new HashMap<>();
        budget.put("Low", 0.8);
        budget.put("Medium", 0.2);
        budget.put("High", 0.0);
        inputs.put("Budget", budget);

        Map<String, Map<String, Double>> output = engine.infer(inputs);
        System.out.println("Output: " + output);
        System.out.println();
    }
}

