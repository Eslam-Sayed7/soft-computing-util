package com.example.softcomputing.fuzzy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.softcomputing.fuzzy.Defuzzifiers.Centroid;
import com.example.softcomputing.fuzzy.Fuzzfication.BasicFuzzifier;
import com.example.softcomputing.fuzzy.inference.MamdaniInferenceEngine;
import com.example.softcomputing.fuzzy.membershipFuns.Triangular;
import com.example.softcomputing.fuzzy.operators.MaxSNorm;
import com.example.softcomputing.fuzzy.operators.MinTNorm;
import com.example.softcomputing.fuzzy.utils.FuzzyRule;
import com.example.softcomputing.fuzzy.utils.InputVariable;
import com.example.softcomputing.fuzzy.utils.OutputVariable;
import com.example.softcomputing.fuzzy.utils.Point;;

public class FuzzyQDemo {

    public static void main(String[] args) {
        System.out.println("===  Question 3 for the exam ===\n");

        InputVariable size = new InputVariable("Size", 0, 100);
        /**
         * 
         * size: small S {0, 0, 100}, large L {0, 100, 100} in range [0 .. 100]
         * weight: light G {0, 0, 100}, Heavy V {0, 100, 100} in range [0 .. 100]
         * quality: bad B {0, 0, 5}, medium M {0, 5, 10}, good G {5, 10, 10} in range [0
         * .. 10]
         */
        size.addTerm("S", new Triangular(
                new Point(0, 1),
                new Point(0, 1),
                new Point(100, 0)));

        size.addTerm("L", new Triangular(
                new Point(0, 0),
                new Point(100, 1),
                new Point(100, 1)));

        InputVariable weight = new InputVariable("Weight", 0, 100);

        weight.addTerm("G", new Triangular(
                new Point(0, 1),
                new Point(0, 1),
                new Point(100, 0)));

        weight.addTerm("V", new Triangular(
                new Point(0, 0),
                new Point(100, 1),
                new Point(100, 1)));

        OutputVariable quality = new OutputVariable("Quality", 0, 10);
        quality.addTerm("B", (0.0 + 0.0 + 5.0) / 3.0);
        quality.addTerm("M", (0.0 + 5.0 + 10.0) / 3.0);
        quality.addTerm("G", (5.0 + 10.0 + 10.0) / 3.0);

        BasicFuzzifier fuzzifier = new BasicFuzzifier();
        fuzzifier.addInputVariable(size);
        fuzzifier.addInputVariable(weight);

        List<FuzzyRule> rules = new ArrayList<>();

        rules.add(new FuzzyRule(
                "R1",
                Map.of("Size", "S", "Weight", "G"),
                Map.of("Quality", "B")));

        rules.add(new FuzzyRule(
                "R2",
                Map.of("Size", "S", "Weight", "V"),
                Map.of("Quality", "M")));

        rules.add(new FuzzyRule(
                "R3",
                Map.of("Size", "L", "Weight", "G"),
                Map.of("Quality", "M")));

        rules.add(new FuzzyRule(
                "R4",
                Map.of("Size", "L", "Weight", "V"),
                Map.of("Quality", "G")));

        MamdaniInferenceEngine inferenceEngine = new MamdaniInferenceEngine(
                rules,
                new MinTNorm(),
                new MaxSNorm());

        FuzzyController controller = FuzzyController.builder()
                .withFuzzifier(fuzzifier)
                .withDefuzzifier(new Centroid(1000))
                .withInferenceEngine(inferenceEngine)
                .addOutputVariable(quality)
                .build();

        double sizeValue = 20.0;
        double weightValue = 25.0;

        Map<String, Double> inputs = Map.of(
                "Size", sizeValue,
                "Weight", weightValue);

        List<String> features = Arrays.asList("Size", "Weight");

        System.out.printf("Inputs: size=%.2f, weight=%.2f\n\n", sizeValue, weightValue);
        double result = controller.evaluate(inputs, features);

        // ========== 8. DISPLAY RESULTS ==========

        System.out.println("(1) Fuzzification");
        Map<String, Map<String, Double>> fuzzyInputs = controller.getLastFuzzyInputs();
        fuzzyInputs.forEach((var, terms) -> {
            System.out.println(var + ":");
            terms.forEach((term, degree) -> System.out.printf("  %s = %.4f\n", term, degree));
        });

        System.out.println("\n(2) Inference");
        MinTNorm tNorm = new MinTNorm();
        for (FuzzyRule rule : rules) {
            double firingStrength = rule.evaluateFiringStrength(fuzzyInputs, tNorm);
            String outputLabel = rule.getConsequent().values().iterator().next();
            System.out.printf("%s: Value = %.4f -> %s\n",
                    rule.getName(), firingStrength, outputLabel);
        }

        System.out.println("\n(3) Aggregation ");
        Map<String, Map<String, Double>> aggregated = controller.getLastAggregatedOutput();
        aggregated.forEach((var, terms) -> {
            System.out.println(var + ":");
            terms.forEach((term, degree) -> System.out.printf("  %s = %.4f (centroid = %.4f)\n",
                    term, degree, quality.getCrispValue(term)));
        });

        System.out.println("\n(4) Defuzzification");
        System.out.printf("Defuzzified Output (Quality): %.4f\n", result);
    }
}