package com.example.softcomputing.fuzzy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.softcomputing.fuzzy.operators.MaxSNorm;
import com.example.softcomputing.fuzzy.operators.MinTNorm;

/**
 * Runnable app that simulates the Question 3 scenario (size, weight -> quality)
 */
public class FuzzyQDemo {

    public static void main(String[] args) {
        System.out.println("=== FuzzyQ3App: Question 3 scenario ===");

        // Problem inputs
        double size = 20.0;
        double weight = 25.0;

        // Fuzzification (manual for this small scenario)
        double muSizeSmall = Math.max(0.0, -size / 100.0 + 1.0); // S {0,0,100}
        double muSizeLarge = Math.max(0.0, size / 100.0); // L {0,100,100}

        double muWeightLight = Math.max(0.0, -weight / 100.0 + 1.0); // G {0,0,100}
        double muWeightHeavy = Math.max(0.0, weight / 100.0); // V {0,100,100}

        System.out.printf("Inputs: size=%.2f, weight=%.2f\n", size, weight);
        System.out.printf("Fuzzified: size Small=%.3f, size Large=%.3f\n", muSizeSmall, muSizeLarge);
        System.out.printf("Fuzzified: weight Light=%.3f, weight Heavy=%.3f\n", muWeightLight, muWeightHeavy);

        // Build fuzzy input structure expected by inference engine
        Map<String, Map<String, Double>> fuzzyInputs = new HashMap<>();
        Map<String, Double> sizeMap = new HashMap<>();
        sizeMap.put("S", muSizeSmall);
        sizeMap.put("L", muSizeLarge);
        fuzzyInputs.put("Size", sizeMap);

        Map<String, Double> weightMap = new HashMap<>();
        weightMap.put("G", muWeightLight);
        weightMap.put("V", muWeightHeavy);
        fuzzyInputs.put("Weight", weightMap);

        // Rules (Mamdani)
        List<FuzzyRule> rules = new ArrayList<>();

        Map<String, String> r1a = new HashMap<>(); r1a.put("Size", "S"); r1a.put("Weight", "G");
        Map<String, String> r1c = new HashMap<>(); r1c.put("Quality", "B");
        rules.add(new FuzzyRule("R1", r1a, r1c));

        Map<String, String> r2a = new HashMap<>(); r2a.put("Size", "S"); r2a.put("Weight", "V");
        Map<String, String> r2c = new HashMap<>(); r2c.put("Quality", "M");
        rules.add(new FuzzyRule("R2", r2a, r2c));

        Map<String, String> r3a = new HashMap<>(); r3a.put("Size", "L"); r3a.put("Weight", "G");
        Map<String, String> r3c = new HashMap<>(); r3c.put("Quality", "M");
        rules.add(new FuzzyRule("R3", r3a, r3c));

        Map<String, String> r4a = new HashMap<>(); r4a.put("Size", "L"); r4a.put("Weight", "V");
        Map<String, String> r4c = new HashMap<>(); r4c.put("Quality", "G");
        rules.add(new FuzzyRule("R4", r4a, r4c));

        MamdaniInferenceEngine engine = new MamdaniInferenceEngine(rules, new MinTNorm(), new MaxSNorm());

        // Inference (aggregated by S-norm inside MamdaniInferenceEngine)
        Map<String, Map<String, Double>> aggregated = engine.infer(fuzzyInputs);
        System.out.println("Aggregated fuzzy output:");
        System.out.println(aggregated);

        Map<String, Double> qualityMap = aggregated.getOrDefault("Quality", Map.of());

        // Centroids for quality sets (as in problem statement)
        double cB = (0.0 + 0.0 + 5.0) / 3.0; // 1.6667
        double cM = (0.0 + 5.0 + 10.0) / 3.0; // 5.0
        double cG = (5.0 + 10.0 + 10.0) / 3.0; // 8.3333

        // Method A: defuzzify using aggregated membership degrees (max-aggregation)
        double numA = 0.0, denA = 0.0;
        double degB = qualityMap.getOrDefault("B", 0.0);
        double degM = qualityMap.getOrDefault("M", 0.0);
        double degG = qualityMap.getOrDefault("G", 0.0);

        numA += degB * cB; denA += degB;
        numA += degM * cM; denA += degM;
        numA += degG * cG; denA += degG;

        double zA = denA == 0.0 ? 0.0 : numA / denA;

        // Method B: sum contributions from each rule (no aggregation) — matches alternate student approach
        double numB = 0.0, denB = 0.0;
        for (FuzzyRule r : rules) {
            double fs = r.evaluateFiringStrength(fuzzyInputs, new MinTNorm());
            String outLabel = r.getConsequent().values().iterator().next();
            double centroid = switch (outLabel) {
                case "B" -> cB;
                case "M" -> cM;
                case "G" -> cG;
                default -> 0.0;
            };
            numB += fs * centroid;
            denB += fs;
            System.out.printf("%s firing strength = %.3f -> contributes %.4f to numerator\n", r.getName(), fs, fs * centroid);
        }
        double zB = denB == 0.0 ? 0.0 : numB / denB;

        System.out.printf("Defuzzified (method A, aggregated max) Z* = %.4f\n", zA);
        System.out.printf("Defuzzified (method B, per-rule sum)   Z* = %.4f\n", zB);

        System.out.println("=== Scenario finished ===");
    }
}

