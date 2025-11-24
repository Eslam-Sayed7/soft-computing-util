package com.example.softcomputing.fuzzy;

import java.util.List;
import java.util.Map;

import com.example.softcomputing.fuzzy.utils.FuzzySet;

public interface Fuzzifier {

    FuzzySet fuzzify(Map<String, Double> crispInput , List<String> features);


    /*
        features: Budget , Interst
        crispInput: Interest: 0.8 , Budget: 0.6 
    */

    
}
