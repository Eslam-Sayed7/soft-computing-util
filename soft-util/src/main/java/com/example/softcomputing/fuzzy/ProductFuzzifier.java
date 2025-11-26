package com.example.softcomputing.fuzzy;

import com.example.softcomputing.fuzzy.membershipFuns.Triangular;
import com.example.softcomputing.fuzzy.utils.FuzzySet;
import com.example.softcomputing.fuzzy.utils.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Concrete implementation of Fuzzifier for product recommendation system.
 * Fuzzifies category, rating, and selling_times attributes.
 */
public class ProductFuzzifier implements Fuzzifier {
    
    // Membership functions for rating (0-5 scale)
    private final Map<String, MembershipFunction> ratingMFs;
    
    // Membership functions for selling_times (0-3000 scale)
    private final Map<String, MembershipFunction> sellingTimesMFs;
    
    // Category fuzzy mappings
    private final Map<String, Map<String, Double>> categoryFuzzyMap;
    
    public ProductFuzzifier() {
        // Initialize rating membership functions
        ratingMFs = new HashMap<>();
        ratingMFs.put("Low", new Triangular(new Point(0, 1), new Point(2.5, 1), new Point(4, 0)));
        ratingMFs.put("Medium", new Triangular(new Point(2.5, 0), new Point(4, 1), new Point(4.5, 0)));
        ratingMFs.put("High", new Triangular(new Point(4, 0), new Point(4.5, 1), new Point(5, 1)));
        
        // Initialize selling times membership functions
        sellingTimesMFs = new HashMap<>();
        sellingTimesMFs.put("Low", new Triangular(new Point(0, 1), new Point(500, 1), new Point(1000, 0)));
        sellingTimesMFs.put("Medium", new Triangular(new Point(500, 0), new Point(1000, 1), new Point(2000, 0)));
        sellingTimesMFs.put("High", new Triangular(new Point(1000, 0), new Point(2000, 1), new Point(3000, 1)));
        
        // Initialize category fuzzy mappings
        categoryFuzzyMap = new HashMap<>();
        categoryFuzzyMap.put("Computers", Map.of("Electronics", 1.0, "Home", 0.0, "Wearables", 0.0));
        categoryFuzzyMap.put("TV", Map.of("Electronics", 0.8, "Home", 0.2, "Wearables", 0.0));
        categoryFuzzyMap.put("Headphones", Map.of("Electronics", 1.0, "Home", 0.0, "Wearables", 0.3));
        categoryFuzzyMap.put("Clothes", Map.of("Electronics", 0.0, "Home", 0.0, "Wearables", 1.0));
        categoryFuzzyMap.put("Furniture", Map.of("Electronics", 0.0, "Home", 1.0, "Wearables", 0.0));
        categoryFuzzyMap.put("Mobile", Map.of("Electronics", 1.0, "Home", 0.0, "Wearables", 0.0));
        categoryFuzzyMap.put("Sports", Map.of("Electronics", 0.0, "Home", 0.0, "Wearables", 0.8));
        categoryFuzzyMap.put("Home Appliances", Map.of("Electronics", 0.2, "Home", 0.8, "Wearables", 0.0));
        categoryFuzzyMap.put("Kitchen", Map.of("Electronics", 0.0, "Home", 1.0, "Wearables", 0.0));
        categoryFuzzyMap.put("Books", Map.of("Electronics", 0.0, "Home", 0.2, "Wearables", 0.0));
    }
    
    @Override
    public FuzzySet fuzzify(Map<String, Double> crispInput, List<String> features) {
        Map<String, Double> membershipValues = new HashMap<>();
        
        for (String feature : features) {
            Double value = crispInput.get(feature);
            if (value == null) continue;
            
            switch (feature.toLowerCase()) {
                case "rating":
                    fuzzifyFeature(value, ratingMFs, "Rating", membershipValues);
                    break;
                case "selling_times":
                    fuzzifyFeature(value, sellingTimesMFs, "SellingTimes", membershipValues);
                    break;
                case "category":
                    // Category is handled differently - it's a string, not a double
                    // This would need special handling in practice
                    break;
            }
        }
        
        FuzzySet fuzzySet = new FuzzySet();
        fuzzySet.setMembershipValues(membershipValues);
        return fuzzySet;
    }
    
    /**
     * Fuzzify a categorical feature (like category name)
     */
    public Map<String, Double> fuzzifyCategory(String categoryName) {
        return categoryFuzzyMap.getOrDefault(categoryName, 
            Map.of("Electronics", 0.0, "Home", 0.0, "Wearables", 0.0));
    }
    
    /**
     * Helper method to fuzzify a single numeric feature
     */
    private void fuzzifyFeature(double value, Map<String, MembershipFunction> mfs, 
                                String featurePrefix, Map<String, Double> output) {
        for (Map.Entry<String, MembershipFunction> entry : mfs.entrySet()) {
            String label = entry.getKey();
            MembershipFunction mf = entry.getValue();
            double degree = mf.apply(value);
            output.put(featurePrefix + "_" + label, degree);
        }
    }
    
    /**
     * Get rating membership functions
     */
    public Map<String, MembershipFunction> getRatingMFs() {
        return ratingMFs;
    }
    
    /**
     * Get selling times membership functions
     */
    public Map<String, MembershipFunction> getSellingTimesMFs() {
        return sellingTimesMFs;
    }
}
