// package com.example.softcomputing.fuzzy.usecase;

// import java.io.BufferedWriter;
// import java.io.File;
// import java.io.FileWriter;
// import java.nio.file.Paths;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.Comparator;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// import com.example.softcomputing.fuzzy.Defuzzifiers.Centroid;
// import com.example.softcomputing.fuzzy.inference.SugenoInferenceEngine;
// import com.example.softcomputing.fuzzy.operators.MinTNorm;
// import com.example.softcomputing.fuzzy.utils.ProductCSVLoader;
// import com.example.softcomputing.fuzzy.utils.RecommendationFuzzifier;
// import com.example.softcomputing.fuzzy.utils.SugenoRule;
// import com.example.softcomputing.fuzzy.utils.UserCSVLoader;

// /**
// * Recommendation runner that uses a small Sugeno rule set and the existing
// * fuzzification utilities.
// *
// * Assumptions (documented):
// * - Category match is treated as boolean: 1.0 if user's interests include
// * product category, else 0.0
// * - Budget score is user's budget normalized by max user budget
// * - Rating normalized by /5 and popularity normalized by max product
// * selling_times
// * - A compact set of single-antecedent Sugeno rules are used to compute a
// * weighted average score
// */
// public class RecommendationRunner {

// public static void main(String[] args) throws Exception {
// String prodPath = args.length > 0 ? args[0] : resolveCSV("data/product.csv");
// String userPath = args.length > 1 ? args[1] : resolveCSV("data/person.csv");
// String outDir = args.length > 2 ? args[2] : "output";

// List<ProductCSVLoader.Product> products =
// ProductCSVLoader.loadProducts(prodPath);
// List<UserCSVLoader.User> users = UserCSVLoader.loadUsers(userPath);

// if (products.isEmpty() || users.isEmpty()) {
// System.out.println("No products or users loaded");
// return;
// }

// int maxSales =
// products.stream().mapToInt(ProductCSVLoader.Product::getSellingTimes).max().orElse(1);
// double maxBudget =
// users.stream().mapToDouble(UserCSVLoader.User::getBudget).max().orElse(1.0);

// // Build a small Sugeno rule set (single-antecedent rules)
// List<SugenoRule> rules = new ArrayList<>();
// // Interest rules
// rules.add(new SugenoRule("InterestHigh", Map.of("interest", "High"), 0.9));
// rules.add(new SugenoRule("InterestMed", Map.of("interest", "Medium"), 0.6));
// rules.add(new SugenoRule("InterestLow", Map.of("interest", "Low"), 0.2));

// // Category match rules
// rules.add(new SugenoRule("CatHigh", Map.of("categoryMatch", "High"), 1.0));
// rules.add(new SugenoRule("CatMed", Map.of("categoryMatch", "Medium"), 0.6));
// rules.add(new SugenoRule("CatLow", Map.of("categoryMatch", "Low"), 0.1));

// // Rating rules
// rules.add(new SugenoRule("RatingHigh", Map.of("rating", "High"), 0.9));
// rules.add(new SugenoRule("RatingMed", Map.of("rating", "Medium"), 0.6));
// rules.add(new SugenoRule("RatingLow", Map.of("rating", "Low"), 0.2));

// // Popularity rules
// rules.add(new SugenoRule("PopHigh", Map.of("popularity", "High"), 0.8));
// rules.add(new SugenoRule("PopMed", Map.of("popularity", "Medium"), 0.5));
// rules.add(new SugenoRule("PopLow", Map.of("popularity", "Low"), 0.2));

// // Budget rules
// rules.add(new SugenoRule("BudgetHigh", Map.of("budget", "High"), 0.5));
// rules.add(new SugenoRule("BudgetMed", Map.of("budget", "Medium"), 0.3));
// rules.add(new SugenoRule("BudgetLow", Map.of("budget", "Low"), 0.1));

// SugenoInferenceEngine engine = new SugenoInferenceEngine(rules, new
// MinTNorm());
// RecommendationFuzzifier fuzz = new RecommendationFuzzifier();

// File od = new File(outDir);
// if (!od.exists())
// od.mkdirs();

// for (UserCSVLoader.User user : users) {
// List<Map.Entry<ProductCSVLoader.Product, Double>> scored = new ArrayList<>();

// for (ProductCSVLoader.Product p : products) {
// // compute normalized inputs
// double interestScore = user.getInterests().stream().anyMatch(s ->
// s.equalsIgnoreCase(p.getCategory()))
// ? 1.0
// : 0.0; // assumption
// double ubudget = user.getBudget() / maxBudget; // normalized
// double rating = p.getRating() / 5.0;
// double popularity = (double) p.getSellingTimes() / (double) maxSales;
// double pcat = 1.0; // product belongs to its category
// double categoryMatch = 1.0 - Math.abs(pcat - interestScore);

// Map<String, Double> crisp = new HashMap<>();
// crisp.put("interest", interestScore);
// crisp.put("budget", ubudget);
// crisp.put("categoryMatch", categoryMatch);
// crisp.put("rating", rating);
// crisp.put("popularity", popularity);

// Map<String, Map<String, Double>> fuzzyInputs = fuzz.fuzzify(crisp);

// double finalScore = engine.inferAndDefuzzify(fuzzyInputs, new Centroid());

// scored.add(Map.entry(p, finalScore));
// }

// // sort descending by score
// Collections.sort(scored, Comparator.comparingDouble(e -> -e.getValue()));

// // write output file
// File out = new File(od, String.format("user_%d_recommendations.csv",
// user.getUserId()));
// try (BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
// bw.write("product_id,name,score\n");
// for (Map.Entry<ProductCSVLoader.Product, Double> e : scored) {
// ProductCSVLoader.Product p = e.getKey();
// bw.write(String.format("%d,\"%s\",%.4f\n", p.getProductId(), p.getName(),
// e.getValue()));
// }
// }

// System.out.println("Wrote recommendations for user " + user.getUserId() + "
// -> " + out.getAbsolutePath());
// }

// System.out.println("All done.");
// }

// private static String resolveCSV(String resourcePath) {
// ClassLoader cl = Thread.currentThread().getContextClassLoader();
// java.net.URL res = cl.getResource(resourcePath);
// if (res == null) {
// throw new IllegalStateException("CSV resource not found: " + resourcePath);
// }
// try {
// return Paths.get(res.toURI()).toString();
// } catch (Exception e) {
// throw new RuntimeException("Failed to resolve CSV path: " + resourcePath, e);
// }
// }

// }
