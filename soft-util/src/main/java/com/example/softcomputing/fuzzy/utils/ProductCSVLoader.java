package com.example.softcomputing.fuzzy.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads product data from CSV file
 */
public class ProductCSVLoader {
    
    public static class Product {
        private int productId;
        private String name;
        private String category;
        private double rating;
        private int sellingTimes;
        
        public Product(int productId, String name, String category, double rating, int sellingTimes) {
            this.productId = productId;
            this.name = name;
            this.category = category;
            this.rating = rating;
            this.sellingTimes = sellingTimes;
        }
        
        public int getProductId() { return productId; }
        public String getName() { return name; }
        public String getCategory() { return category; }
        public double getRating() { return rating; }
        public int getSellingTimes() { return sellingTimes; }
        
        /**
         * Convert product to crisp input map for fuzzification
         */
        public Map<String, Double> toCrispInput() {
            Map<String, Double> input = new HashMap<>();
            input.put("rating", this.rating);
            input.put("selling_times", (double) this.sellingTimes);
            return input;
        }
        
        @Override
        public String toString() {
            return String.format("Product{id=%d, name='%s', category='%s', rating=%.1f, sellingTimes=%d}",
                    productId, name, category, rating, sellingTimes);
        }
    }
    
    /**
     * Load products from CSV file
     */
    public static List<Product> loadProducts(String csvPath) throws IOException {
        List<Product> products = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            String header = br.readLine(); // Skip header
            String line;
            
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 5) {
                    int id = Integer.parseInt(tokens[0].trim());
                    String name = tokens[1].trim();
                    String category = tokens[2].trim();
                    double rating = Double.parseDouble(tokens[3].trim());
                    int sellingTimes = Integer.parseInt(tokens[4].trim());
                    
                    products.add(new Product(id, name, category, rating, sellingTimes));
                }
            }
        }
        
        return products;
    }
    
    /**
     * Load a specific number of products
     */
    public static List<Product> loadProducts(String csvPath, int maxProducts) throws IOException {
        List<Product> allProducts = loadProducts(csvPath);
        return allProducts.subList(0, Math.min(maxProducts, allProducts.size()));
    }
}
