package com.example.softcomputing.fuzzy.Fuzzfication;

import com.example.softcomputing.fuzzy.ProductFuzzifier;
import com.example.softcomputing.fuzzy.utils.FuzzySet;
import com.example.softcomputing.fuzzy.utils.ProductCSVLoader;
import com.example.softcomputing.fuzzy.utils.ProductCSVLoader.Product;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Demo: Complete fuzzification pipeline for product recommendation
 */
public class FuzzificationDemo {
    
    public static void main(String[] args) {
        try {
            // Step 1: Load products from CSV
            // Default path works when running from project root
            String csvPath = args.length > 0 ? args[0] : 
                "soft-util/src/main/java/com/example/softcomputing/fuzzy/data/product.csv";
            int maxProducts = args.length > 1 ? Integer.parseInt(args[1]) : 3;
            
            System.out.println("Loading products from: " + csvPath);
            List<Product> products = ProductCSVLoader.loadProducts(csvPath, maxProducts);
            System.out.println("Loaded " + products.size() + " products\n");
            
            System.out.println("========================================");
            System.out.println("FUZZIFICATION DEMO - Product Recommendation");
            System.out.println("========================================\n");
            
            // Step 2: Create fuzzifier
            ProductFuzzifier fuzzifier = new ProductFuzzifier();
            
            // Step 3: Fuzzify each product
            for (Product product : products) {
                System.out.println("Product: " + product.getName());
                System.out.println("  Category: " + product.getCategory());
                System.out.println("  Rating: " + product.getRating());
                System.out.println("  Selling Times: " + product.getSellingTimes());
                System.out.println();
                
                // Fuzzify numeric features
                Map<String, Double> crispInput = product.toCrispInput();
                List<String> features = Arrays.asList("rating", "selling_times");
                FuzzySet fuzzySet = fuzzifier.fuzzify(crispInput, features);
                
                System.out.println("  Fuzzified Values:");
                Map<String, Double> membershipValues = fuzzySet.getMembershipValues();
                for (Map.Entry<String, Double> entry : membershipValues.entrySet()) {
                    System.out.printf("    %-25s : %.3f%n", entry.getKey(), entry.getValue());
                }
                
                // Fuzzify category separately
                Map<String, Double> categoryFuzzy = fuzzifier.fuzzifyCategory(product.getCategory());
                System.out.println("  Category Fuzzy Values:");
                for (Map.Entry<String, Double> entry : categoryFuzzy.entrySet()) {
                    System.out.printf("    Category_%-15s : %.1f%n", entry.getKey(), entry.getValue());
                }
                
                System.out.println("\n" + "=".repeat(60) + "\n");
            }
            
            System.out.println("\n✓ Fuzzification complete!");
        } catch(Exception e){
            System.err.println(e);
        }
    } 
}
