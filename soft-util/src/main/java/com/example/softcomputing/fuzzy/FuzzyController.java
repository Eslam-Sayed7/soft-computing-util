package com.example.softcomputing.fuzzy;

public class FuzzyController {
    
    public Fuzzifier fuzzifier;
    public Defuzzifier defuzzifier;
    public InferenceEngine inferenceEngine;

    public FuzzyController(Fuzzifier fuzzifier, Defuzzifier defuzzifier, InferenceEngine inferenceEngine) {
        this.fuzzifier = fuzzifier;
        this.defuzzifier = defuzzifier;
        this.inferenceEngine = inferenceEngine;
    }
    
}
