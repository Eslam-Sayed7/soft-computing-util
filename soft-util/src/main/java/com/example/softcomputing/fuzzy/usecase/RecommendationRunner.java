package com.example.softcomputing.fuzzy.usecase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.softcomputing.fuzzy.inference.SugenoInferenceEngine;
import com.example.softcomputing.fuzzy.operators.MinTNorm;
import com.example.softcomputing.fuzzy.utils.FuzzyRule;

public class RecommendationRunner {

    public static void main(String[] args) throws Exception {
        String prodPath = args.length > 0 ? args[0] : resolveCSV("data/product.csv");
        String userPath = args.length > 1 ? args[1] : resolveCSV("data/person.csv");
        String outDir = args.length > 2 ? args[2] : "output";

        List<ProductCSVLoader.Product> products
                = ProductCSVLoader.loadProducts(prodPath);
        List<UserCSVLoader.User> users = UserCSVLoader.loadUsers(userPath);

        if (products.isEmpty() || users.isEmpty()) {
            System.out.println("No products or users loaded");
            return;
        }

        int maxSales
                = products.stream().mapToInt(ProductCSVLoader.Product::getSellingTimes).max().orElse(1);
        double maxBudget
                = users.stream().mapToDouble(UserCSVLoader.User::getBudget).max().orElse(1.0);

        // Build a small Sugeno-style rule set (single-antecedent rules)
        List<FuzzyRule> rules = new ArrayList<>();
        // Interest rules
        rules.add(new FuzzyRule("InterestHigh", Map.of("interest", "High"), 0.9));
        rules.add(new FuzzyRule("InterestMed", Map.of("interest", "Medium"), 0.6));
        rules.add(new FuzzyRule("InterestLow", Map.of("interest", "Low"), 0.2));

        // Category match rules
        rules.add(new FuzzyRule("CatHigh", Map.of("categoryMatch", "High"), 1.0));
        rules.add(new FuzzyRule("CatMed", Map.of("categoryMatch", "Medium"), 0.6));
        rules.add(new FuzzyRule("CatLow", Map.of("categoryMatch", "Low"), 0.1));

        // Rating rules
        rules.add(new FuzzyRule("RatingHigh", Map.of("rating", "High"), 0.9));
        rules.add(new FuzzyRule("RatingMed", Map.of("rating", "Medium"), 0.6));
        rules.add(new FuzzyRule("RatingLow", Map.of("rating", "Low"), 0.2));

        // Popularity rules
        rules.add(new FuzzyRule("PopHigh", Map.of("popularity", "High"), 0.8));
        rules.add(new FuzzyRule("PopMed", Map.of("popularity", "Medium"), 0.5));
        rules.add(new FuzzyRule("PopLow", Map.of("popularity", "Low"), 0.2));

        // Budget rules
        rules.add(new FuzzyRule("BudgetHigh", Map.of("budget", "High"), 0.5));
        rules.add(new FuzzyRule("BudgetMed", Map.of("budget", "Medium"), 0.3));
        rules.add(new FuzzyRule("BudgetLow", Map.of("budget", "Low"), 0.1));

        SugenoInferenceEngine engine = new SugenoInferenceEngine(rules, new MinTNorm());
        RecommendationFuzzifier fuzz = new RecommendationFuzzifier();

        File od = new File(outDir);
        if (!od.exists()) {
            od.mkdirs();
        }

        for (UserCSVLoader.User user : users) {
            List<Map.Entry<ProductCSVLoader.Product, Double>> scored = new ArrayList<>();

            for (ProductCSVLoader.Product p : products) {
                // compute normalized inputs
                double interestScore = user.getInterests().stream().anyMatch(s
                        -> s.equalsIgnoreCase(p.getCategory()))
                        ? 1.0
                        : 0.0; // assumption
                double ubudget = user.getBudget() / maxBudget; // normalized
                double rating = p.getRating() / 5.0;
                double popularity = (double) p.getSellingTimes() / (double) maxSales;
                double pcat = 1.0; // product belongs to its category
                double categoryMatch = 1.0 - Math.abs(pcat - interestScore);

                Map<String, Double> crisp = new HashMap<>();
                crisp.put("interest", interestScore);
                crisp.put("budget", ubudget);
                crisp.put("categoryMatch", categoryMatch);
                crisp.put("rating", rating);
                crisp.put("popularity", popularity);

                Map<String, Map<String, Double>> fuzzyInputs = fuzz.fuzzify(crisp);

                // compute weighted average using SugenoInferenceEngine
                double finalScore = engine.computeWeightedAverage(fuzzyInputs);

                scored.add(Map.entry(p, finalScore));
            }

            // sort descending by score
            Collections.sort(scored, Comparator.comparingDouble(e -> -e.getValue()));

            // write output file
            File out = new File(od, String.format("user_%d_recommendations.csv",
                    user.getUserId()));
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
                bw.write("product_id,name,score\n");
                for (Map.Entry<ProductCSVLoader.Product, Double> e : scored) {
                    ProductCSVLoader.Product p = e.getKey();
                    bw.write(String.format("%d,\"%s\",%.4f\n", p.getProductId(), p.getName(),
                            e.getValue()));
                }
            }

            System.out.println("Wrote recommendations for user " + user.getUserId() + " -> " + out.getAbsolutePath());
        }

        System.out.println("All done");
    }

    private static String resolveCSV(String resourcePath) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        java.net.URL res = cl.getResource(resourcePath);
        if (res == null) {
            throw new IllegalStateException("CSV resource not found: " + resourcePath);
        }
        try {
            return Paths.get(res.toURI()).toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to resolve CSV path: " + resourcePath, e);
        }
    }

}
